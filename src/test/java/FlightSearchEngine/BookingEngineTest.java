package FlightSearchEngine;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class BookingEngineTest {
    Booking booking;
    Booking almostFullBooking;
    Connection conn;
    BookingEngine BE;
    @Before
    public void setUp() throws Exception {
        // Departure flight
        String airline1 = "FI";
        int flightNumber1 = 100;
        LocalDateTime departureTime1 = LocalDateTime.of(2015,5,5,15,30);
        LocalDateTime arrivalTime1 = LocalDateTime.of(2015,5,5,20,20);
        int price1 = 14000;
        String departureLocation1 = "Reykjavik";
        String arrivalLocation1 = "London";
        int seatsAvailable1 = 100;
        Flight departureFlight = new Flight(flightNumber1,departureTime1,arrivalTime1,price1,departureLocation1,arrivalLocation1,airline1,seatsAvailable1);
        List<Flight> departureFlights = new ArrayList<>();
        departureFlights.add(departureFlight);

        // Return flight
        String airline2 = "FI";
        int flightNumber2 = 101;
        LocalDateTime departureTime2 = LocalDateTime.of(2015,5,10,13,20);
        LocalDateTime arrivalTime2 = LocalDateTime.of(2015,5,10,19,05);
        int price2 = 11000;
        String departureLocation2 = "London";
        String arrivalLocation2 = "Reykjavik";
        int seatsAvailable2 = 100;
        Flight returnFlight = new Flight(flightNumber2,departureTime2,arrivalTime2,price2,departureLocation2,arrivalLocation2,airline2,seatsAvailable2);
        List<Flight> returnFlights = new ArrayList<>();
        returnFlights.add(returnFlight);

        // Almost full departure flight
        String airline3 = "FI";
        int flightNumber3 = 102;
        LocalDateTime departureTime3 = LocalDateTime.of(2015,5,5,17,30);
        LocalDateTime arrivalTime3 = LocalDateTime.of(2015,5,5,22,20);
        int price3 = 11000;
        String departureLocation3 = "Reykjavik";
        String arrivalLocation3 = "London";
        int seatsAvailable3 = 2;
        Flight departureFlight3 = new Flight(flightNumber3,departureTime3,arrivalTime3,price3,departureLocation3,arrivalLocation3,airline3,seatsAvailable3);
        List<Flight> departureFlights3 = new ArrayList<>();
        departureFlights3.add(departureFlight3);


        FlightTrip flightTripOneWay = new FlightTrip(departureFlights, null);
        FlightTrip flightTripTwoWay = new FlightTrip(departureFlights, returnFlights);
        FlightTrip almostFullOneWay = new FlightTrip(departureFlights3, null);
        List<String> names = new ArrayList<>();
        names.add("Jón");
        names.add("Páll");
        names.add("Móses");
        booking = new Booking(names, "jonjonsson@jon.is", 3, flightTripOneWay);
        almostFullBooking = new Booking(names, "janesmith@smith.com", 3, almostFullOneWay);

        DatabaseConnection mockDBConn = new MockDatabaseConnection();
        conn = mockDBConn.getConnection();
        Statement stmt = conn.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS Flights (" +
                "        flightNumber," +
                "        airline," +
                "        departureTime," +
                "        arrivalTime," +
                "        departureLocation," +
                "        arrivalLocation," +
                "        price," +
                "        seatsAvailable)";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO Flights (" +
                "        flightNumber," +
                "        airline," +
                "        departureTime," +
                "        arrivalTime," +
                "        departureLocation," +
                "        arrivalLocation," +
                "        price," +
                "        seatsAvailable) " +
                "VALUES (" +
                "        100, " +
                "        'FI', " +
                "        '2015-05-05 15:30:00', " +
                "        '2015-05-05 20:20:00', " +
                "        'Reykjavik'," +
                "        'London'," +
                "        14000," +
                "        100);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO Flights (" +
                "        flightNumber," +
                "        airline," +
                "        departureTime," +
                "        arrivalTime," +
                "        departureLocation," +
                "        arrivalLocation," +
                "        price," +
                "        seatsAvailable) " +
                "VALUES (" +
                "        102, " +
                "        'FI', " +
                "        '2015-05-05 17:30:00', " +
                "        '2015-05-05 22:20:00', " +
                "        'Reykjavik'," +
                "        'London'," +
                "        11000," +
                "        2);";
        stmt.executeUpdate(sql);
        sql = "CREATE TABLE IF NOT EXISTS Bookings (\n" +
                "        bookingId integer primary key autoincrement,\n" +
                "        email)";
        stmt.executeUpdate(sql);
        sql = "CREATE TABLE IF NOT EXISTS BookedFlights (\n" +
                "        bookingId integer references Bookings(bookingId),\n" +
                "        airline,\n" +
                "        flightNumber,\n" +
                "        departureTime,\n" +
                "        foreign key (airline, flightNumber, departureTime) \n" +
                "        references Flights(airline, flightNumber, departureTime))";
        stmt.executeUpdate(sql);
        sql = "CREATE TABLE IF NOT EXISTS Passengers (\n" +
                "        bookingId integer references Bookings(bookingId),\n" +
                "        name)";
        stmt.executeUpdate(sql);
        BE = new BookingEngine(mockDBConn);
    }

    @After
    public void tearDown() throws Exception {
        booking = null;
        almostFullBooking = null;
        BE = null;
        Statement stmt = conn.createStatement();
        String sql = "Drop table Flights;";
        stmt.executeUpdate(sql);
        sql = "Drop table Bookings;";
        stmt.executeUpdate(sql);
        sql = "Drop table Passengers;";
        stmt.executeUpdate(sql);
        sql = "Drop table BookedFlights;";
        stmt.executeUpdate(sql);
        conn.close();
    }

    @Test
    public void testBookFlightTrip() throws Exception {
        Boolean isBooked = BE.bookFlightTrip(booking);
        //System.out.println("1");
        Statement stmt = conn.createStatement();
        String sql = "select * from Bookings";
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()) {
            int id = rs.getInt("bookingId");
            String email = rs.getString("email");
            //System.out.println("id :" + id);
            //System.out.println("email: " + email);
        }
        assertTrue(isBooked);
    }

    @Test
    public void testBookFullFlight() throws Exception {
        assertFalse(BE.bookFlightTrip(almostFullBooking));
    }

    @Test
    public void testDeleteBooking () throws Exception {
        Boolean isBooked = BE.bookFlightTrip(booking);
        //System.out.println("Booking...");
        Statement stmt = conn.createStatement();
        String sql = "select * from Bookings";
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()) {
            int id = rs.getInt("bookingId");
            String email = rs.getString("email");
            //System.out.println("id :" + id);
            //System.out.println("email: " + email);
        }
        assertTrue(isBooked);
        //System.out.println("BookingId: " + booking.getBookingId());
        //System.out.println("Deleting...");
        Boolean isDeleted = BE.deleteBooking(booking);
        rs = stmt.executeQuery(sql);
        while(rs.next()) {
            int id = rs.getInt("bookingId");
            String email = rs.getString("email");
            //System.out.println("id :" + id);
            //System.out.println("email: " + email);
        }
        assertTrue(isDeleted);
    }
}