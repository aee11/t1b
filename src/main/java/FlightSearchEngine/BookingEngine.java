package FlightSearchEngine;

/**
 * Created by Gisli on 05/03/15.
 */

import static FlightSearchEngine.Tables.*;

import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.util.sqlite.SQLiteDSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

import org.jooq.impl.DSL.*;
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
                if (seatsAvailable < booking.getNumSeats()) throw new Exception("Not enough seats");
                create.update(FLIGHTS)
                        .set(FLIGHTS.SEATSAVAILABLE, seatsAvailable - booking.getNumSeats());
                // TODO change datsbase so the following works
//                create.insertInto(BOOKEDFLIGHTS,
//                        BOOKEDFLIGHTS.BOOKINGID,BOOKEDFLIGHTS.FLIGHTNUMBER,BOOKEDFLIGHTS.DEPARTURETIME,BOOKEDFLIGHTS.AIRLINE)
//                        .values(bookingId,departureFlight.getFlightNumber(),depTime,departureFlight.getAirline());
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
                    if (seatsAvailable < booking.getNumSeats()) throw new Exception("Not enough seats");
                    create.update(FLIGHTS)
                            .set(FLIGHTS.SEATSAVAILABLE, seatsAvailable - booking.getNumSeats());
                    // TODO change datsbase so the following works
                    //                create.insertInto(BOOKEDFLIGHTS,
                    //                        BOOKEDFLIGHTS.BOOKINGID,BOOKEDFLIGHTS.FLIGHTNUMBER,BOOKEDFLIGHTS.DEPARTURETIME,BOOKEDFLIGHTS.AIRLINE)
                    //                        .values(bookingId,returnFlight.getFlightNumber(),depTime,returnFlight.getAirline());
                }
            }
            conn.commit();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
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
        // TODO implement here
        return false;
    }

}
