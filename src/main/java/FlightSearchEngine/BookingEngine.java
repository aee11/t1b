package FlightSearchEngine;

/**
 * Created by Gisli on 05/03/15.
 */

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

import static FlightSearchEngine.Tables.*;
import static javax.mail.Message.RecipientType;
/**
 *
 */
public class BookingEngine {
    private Connection conn;
    private DSLContext create;

    /**
     *
     */
    public BookingEngine(DatabaseConnection dbConn) {
        setupDbConnection(dbConn);
    }

    private void setupDbConnection(DatabaseConnection dbConn) {
        conn = dbConn.getConnection();
        create = DSL.using(conn, SQLDialect.SQLITE);
    }

    /**
     * @param booking
     * @return
     */
    public boolean bookFlightTrip(Booking booking) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        try {
            conn.setAutoCommit(false);
            // Create booking
            create.insertInto(BOOKINGS,
                    BOOKINGS.EMAIL)
                    .values(booking.getEmail())
                    .execute();
            // Get booking id
            Record result = create.select(BOOKINGS.BOOKINGID)
                    .from(BOOKINGS)
                    .orderBy(BOOKINGS.BOOKINGID.desc())
                    .limit(1)
                    .fetchOne();
            int bookingId = (int) result.getValue(BOOKINGS.BOOKINGID);
            // Insert names into Passenger table
            for (String passengerName : booking.getNames()) {
                create.insertInto(PASSENGERS,
                        PASSENGERS.BOOKINGID,PASSENGERS.NAME)
                        .values(bookingId, passengerName)
                        .execute();
            }
            // Book seats on departure flights
            for (Flight departureFlight : booking.getFlightTrip().getDepartureFlights()) {
                String depTime = departureFlight.getDepartureTime().format(dateFormatter);
                Record seatsResult = create
                        .select(FLIGHTS.SEATSAVAILABLE)
                        .from(FLIGHTS)
                        .where(FLIGHTS.FLIGHTNUMBER.equal(departureFlight.getFlightNumber())
                                .and(FLIGHTS.AIRLINE.equal(departureFlight.getAirline()))
                                .and(FLIGHTS.DEPARTURETIME.startsWith(depTime)))
                        .fetchOne();
                int seatsAvailable = (int) seatsResult.getValue(FLIGHTS.SEATSAVAILABLE);
                if (seatsAvailable < booking.getNumSeats()) throw new Exception("Not enough seats available");
                create.update(FLIGHTS)
                        .set(FLIGHTS.SEATSAVAILABLE, seatsAvailable - booking.getNumSeats()).execute();
                create.insertInto(BOOKEDFLIGHTS,
                        BOOKEDFLIGHTS.BOOKINGID,BOOKEDFLIGHTS.FLIGHTNUMBER,BOOKEDFLIGHTS.DEPARTURETIME,BOOKEDFLIGHTS.AIRLINE)
                        .values(bookingId,departureFlight.getFlightNumber(),depTime,departureFlight.getAirline()).execute();
            }
            // Book seats on return flights
            if (booking.getFlightTrip().getReturnFlights() != null) {
                for (Flight returnFlight : booking.getFlightTrip().getReturnFlights()) {
                    String depTime = returnFlight.getDepartureTime().format(dateFormatter);
                    Record seatsResult = create
                            .select(FLIGHTS.SEATSAVAILABLE)
                            .from(FLIGHTS)
                            .where(FLIGHTS.FLIGHTNUMBER.equal(returnFlight.getFlightNumber())
                                    .and(FLIGHTS.AIRLINE.equal(returnFlight.getAirline()))
                                    .and(FLIGHTS.DEPARTURETIME.startsWith(depTime)))
                            .fetchOne();
                    int seatsAvailable = (int) seatsResult.getValue(FLIGHTS.SEATSAVAILABLE);
                    if (seatsAvailable < booking.getNumSeats()) throw new Exception("Not enough seats available");
                    create.update(FLIGHTS)
                            .set(FLIGHTS.SEATSAVAILABLE, seatsAvailable - booking.getNumSeats()).execute();
                    create.insertInto(BOOKEDFLIGHTS,
                            BOOKEDFLIGHTS.BOOKINGID,BOOKEDFLIGHTS.FLIGHTNUMBER,BOOKEDFLIGHTS.DEPARTURETIME,BOOKEDFLIGHTS.AIRLINE)
                            .values(bookingId, returnFlight.getFlightNumber(), depTime, returnFlight.getAirline()).execute();
                }
            }
            conn.commit();
            booking.setBookingId(bookingId);
            sendEmail(booking);
        } catch (Exception e) {
            System.err.println( "bookFlightTrip error: " + e.getMessage() );
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            return false;
        }
        return true;
    }

    /**
     * @param booking
     * @return
     */
    private boolean sendEmail(Booking booking) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        String[] to = {booking.getEmail()};
        String host = "smtp.gmail.com";
        Properties props = System.getProperties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host",host);
        props.put("mail.smtp.user","t1bhbv401g@gmail.com");
        props.put("mail.smtp.password","musarrindill");
        props.put("mail.smtp.port", 587);
        props.put("mail.smtp.auth","true");
        Session session = Session.getDefaultInstance(props,null);
        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            mimeMessage.setFrom(new InternetAddress("t1bhbv401g@gmail.com"));
            InternetAddress[] toAddress = new InternetAddress[to.length];
            for(int i = 0;i<to.length;i++){
                toAddress[i] = new InternetAddress(to[i]);
            }
            for(int i = 0;i<toAddress.length;i++){
                mimeMessage.addRecipient(RecipientType.TO, toAddress[i]);
            }
            String names = "";
            for (String name : booking.getNames()) {
                //for(int i = 0;i<booking.getNames().size();i++){
                names = names + " " + name;
            }
            String mailMessage = "<h1>Flight ticket</h1><table><tr style=><th>Airline</th><th>Flight number</th><th>Departure location</th><th>Arrival location</th><th>Departure time</th><th>Name</th></tr>";
            List<Flight> departureFlights = booking.getFlightTrip().getDepartureFlights();
            List<Flight> returnFlights = booking.getFlightTrip().getReturnFlights();
            for (Flight dep : departureFlights ) {
                String air = dep.getAirline();
                int flN = dep.getFlightNumber();
                String depLoc = dep.getDepartureLocation();
                String depTime = dep.getDepartureTime().format(dateFormatter);
                String arrLoc = dep.getArrivalLocation();
                for (String name : booking.getNames()) {
                    mailMessage += "<tr><td>"+air+"</td><td>"+flN+"</td><td>" + depLoc + "</td><td>" + arrLoc + "</td><td>" + depTime + "</td><td>" +  name + "</td></tr>";
                }

            }
            if (returnFlights != null) {
                for (Flight ret : returnFlights) {
                    String air = ret.getAirline();
                    int flN = ret.getFlightNumber();
                    String depLoc = ret.getDepartureLocation();
                    String depTime = ret.getDepartureTime().format(dateFormatter);
                    String arrLoc = ret.getArrivalLocation();
                    String arrTime = ret.getArrivalTime().format(dateFormatter);

                    for (String name : booking.getNames()) {
                        mailMessage += "<tr><td>" + air + "</td><td>" + flN + "</td><td>" + depLoc + "</td><td>" + arrLoc + "</td><td>" + depTime + "</td><td>" + name + "</td></tr>";
                    }
                }
            }
            mimeMessage.setSubject("Flight");
            mimeMessage.setContent(mailMessage + "</table>","text/html; charset=\"UTF-8\"");
            Transport transport = session.getTransport("smtp");
            transport.connect(host,"t1bhbv401g@gmail.com","musarrindill");
            transport.sendMessage(mimeMessage,mimeMessage.getAllRecipients());
            transport.close();
            return true;
        } catch(MessagingException me){
            me.printStackTrace();
            //gisttor@gmail.com

        }
        return false;
    }

    public boolean deleteBooking(Booking booking) {
        try {
            conn.setAutoCommit(false);
            create.delete(BOOKEDFLIGHTS)
                    .where(BOOKEDFLIGHTS.BOOKINGID.equal(booking.getBookingId())).execute();
            create.delete(PASSENGERS)
                    .where(PASSENGERS.BOOKINGID.equal(booking.getBookingId())).execute();
            create.delete(BOOKINGS)
                    .where(BOOKINGS.BOOKINGID.equal(booking.getBookingId())).execute();
            conn.commit();
        } catch (Exception e) {
            System.err.println( e.getMessage() );
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            return false;
        }
        booking.deleteBooking();
        return true;
    }

}
