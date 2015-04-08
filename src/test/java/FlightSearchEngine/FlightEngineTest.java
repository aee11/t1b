package FlightSearchEngine;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class FlightEngineTest {

    FlightEngine flightEngine;
    Connection conn;

    @Before
    public void setUp() throws Exception {
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
                "        '2015-03-30 09:20:00', " +
                "        '2015-03-30 14:20:00', " +
                "        'Reykjavik'," +
                "        'Madrid'," +
                "        50000," +
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
                "        101, " +
                "        'FI', " +
                "        '2015-03-30 06:20:00', " +
                "        '2015-03-30 09:20:00', " +
                "        'Reykjavik'," +
                "        'London'," +
                "        30000," +
                "        20);";
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
                "        300, " +
                "        'WW', " +
                "        '2015-03-30 09:20:00', " +
                "        '2015-03-30 12:20:00', " +
                "        'Reykjavik'," +
                "        'London'," +
                "        25000," +
                "        5);";
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
                "        301, " +
                "        'WW', " +
                "        '2015-03-30 09:10:00', " +
                "        '2015-03-30 12:55:00', " +
                "        'Reykjavik'," +
                "        'Copenhagen'," +
                "        30000," +
                "        30);";
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
                "        255, " +
                "        'KF', " +
                "        '2015-03-30 09:10:00', " +
                "        '2015-03-30 12:55:00', " +
                "        'Copenhagen'," +
                "        'Reykjavik'," +
                "        30000," +
                "        15);";
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
                "        301, " +
                "        'ER', " +
                "        '2015-03-30 14:10:00', " +
                "        '2015-03-30 15:55:00', " +
                "        'Reykjavik'," +
                "        'Akureyri'," +
                "        30000," +
                "        15);";
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
                "        302, " +
                "        'ER', " +
                "        '2015-04-15 12:10:00', " +
                "        '2015-04-15 15:55:00', " +
                "        'Akureyri'," +
                "        'Reykjavik'," +
                "        30000," +
                "        15);";
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
                "        342, " +
                "        'LO', " +
                "        '2015-04-15 18:10:00', " +
                "        '2015-04-15 22:55:00', " +
                "        'Reykjavik'," +
                "        'Copenhagen'," +
                "        45000," +
                "        15);";
        stmt.executeUpdate(sql);
        stmt.close();
        flightEngine = new FlightEngine(mockDBConn);
    }

    @After
    public void tearDown() throws Exception {
        Statement stmt = conn.createStatement();
        String sql = "Drop table Flights;";
        stmt.executeUpdate(sql);
        conn.close();

    }

    @Test
    public void testGetResultsFlightNumber() throws Exception {
        LocalDate departureDate =  LocalDate.of(2015,3,30);
        FlightQuery query = new FlightQuery(departureDate,"Reykjavik","Copenhagen");
        List<FlightTrip> rs = flightEngine.getResults(query);
        FlightTrip departureFlightTrip = rs.get(0);
        List<Flight> departureFlights = departureFlightTrip.getDepartureFlights();
        Flight departureFlight = departureFlights.get(0);

        assertNotNull(departureFlight.getFlightNumber());
        assertEquals(301,departureFlight.getFlightNumber());

    }

    @Test
    public void testGetResultsAirline() throws Exception {
        LocalDate departureDate =  LocalDate.of(2015,3,30);
        FlightQuery query = new FlightQuery(departureDate,"Reykjavik","Copenhagen");
        List<FlightTrip> rs = flightEngine.getResults(query);
        FlightTrip departureFlightTrip = rs.get(0);
        List<Flight> departureFlights = departureFlightTrip.getDepartureFlights();
        Flight departureFlight = departureFlights.get(0);

        assertNotNull(departureFlight.getAirline());
        assertEquals("WW",departureFlight.getAirline());
    }

    @Test
    public void testGetDepartureTime() throws Exception {
        LocalDate departureDate =  LocalDate.of(2015,3,30);
        FlightQuery query = new FlightQuery(departureDate,"Reykjavik","Copenhagen");
        List<FlightTrip> rs = flightEngine.getResults(query);
        FlightTrip departureFlightTrip = rs.get(0);
        List<Flight> departureFlights = departureFlightTrip.getDepartureFlights();
        Flight departureFlight = departureFlights.get(0);

        assertNotNull(departureFlight.getDepartureTime());
        assertEquals("2015-03-30T09:10",departureFlight.getDepartureTime().toString());
    }

    @Test
    public void testGetArrivalTime() throws Exception {
        LocalDate departureDate =  LocalDate.of(2015,3,30);
        FlightQuery query = new FlightQuery(departureDate,"Reykjavik","Copenhagen");
        List<FlightTrip> rs = flightEngine.getResults(query);
        FlightTrip departureFlightTrip = rs.get(0);
        List<Flight> departureFlights = departureFlightTrip.getDepartureFlights();
        Flight departureFlight = departureFlights.get(0);

        assertNotNull(departureFlight.getArrivalTime());
        assertEquals("2015-03-30T12:55",departureFlight.getArrivalTime().toString());
    }

    @Test
    public void testGetDepartureLocation() throws Exception {
        LocalDate departureDate =  LocalDate.of(2015,3,30);
        FlightQuery query = new FlightQuery(departureDate,"Reykjavik","Copenhagen");
        List<FlightTrip> rs = flightEngine.getResults(query);
        FlightTrip departureFlightTrip = rs.get(0);
        List<Flight> departureFlights = departureFlightTrip.getDepartureFlights();
        Flight departureFlight = departureFlights.get(0);

        assertNotNull(departureFlight.getDepartureLocation());
        assertEquals("Reykjavik",departureFlight.getDepartureLocation());
    }

    @Test
    public void testGetArrivalLocation() throws Exception {
        LocalDate departureDate =  LocalDate.of(2015,3,30);
        FlightQuery query = new FlightQuery(departureDate,"Reykjavik","Copenhagen");
        List<FlightTrip> rs = flightEngine.getResults(query);
        FlightTrip departureFlightTrip = rs.get(0);
        List<Flight> departureFlights = departureFlightTrip.getDepartureFlights();
        Flight departureFlight = departureFlights.get(0);

        assertNotNull(departureFlight.getArrivalLocation());
        assertEquals("Copenhagen",departureFlight.getArrivalLocation());
    }

    @Test
    public void testGetPrice() throws Exception {
        LocalDate departureDate =  LocalDate.of(2015,3,30);
        FlightQuery query = new FlightQuery(departureDate,"Reykjavik","Copenhagen");
        List<FlightTrip> rs = flightEngine.getResults(query);
        FlightTrip departureFlightTrip = rs.get(0);
        List<Flight> departureFlights = departureFlightTrip.getDepartureFlights();
        Flight departureFlight = departureFlights.get(0);

        assertNotNull(departureFlight.getPrice());
        assertEquals(30000,departureFlight.getPrice());
    }

    @Test
    public void testGetSeatsAvailable() throws Exception {
        LocalDate departureDate =  LocalDate.of(2015,3,30);
        FlightQuery query = new FlightQuery(departureDate,"Reykjavik","Copenhagen");
        List<FlightTrip> rs = flightEngine.getResults(query);
        FlightTrip departureFlightTrip = rs.get(0);
        List<Flight> departureFlights = departureFlightTrip.getDepartureFlights();
        Flight departureFlight = departureFlights.get(0);

        assertNotNull(departureFlight.getSeatsAvailable());
        assertEquals(30,departureFlight.getSeatsAvailable());
    }
    @Test
    public void testGetResults() throws Exception {
        LocalDate departureDate =  LocalDate.of(2015,3,30);
        FlightQuery query = new FlightQuery(departureDate,"Reykjavik","London");
        List<FlightTrip> results = flightEngine.getResults(query);
        assertEquals(2,results.size());
    }

    @Test
    public void testLayoverOneWay() {
        LocalDate departureDate =  LocalDate.of(2015,3,30);
        FlightQuery query = new FlightQuery(departureDate,"Copenhagen","Akureyri");
        List<FlightTrip> results = flightEngine.getResults(query);
        assertNotNull(results);
        for (FlightTrip trip : results) {
            assertEquals(2, trip.getDepartureFlights().size());
        }
    }

    @Test
    public void testTwoWayFlightWithLayoverBothWays() {
        LocalDate departureDate =  LocalDate.of(2015,3,30);
        LocalDate arrivalDate =  LocalDate.of(2015,4,15);
        FlightQuery query = new FlightQuery(departureDate, arrivalDate, "Copenhagen", "Akureyri");
        List<FlightTrip> results = flightEngine.getResults(query);
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(2, results.get(0).getDepartureFlights().size());
        assertEquals(2, results.get(0).getReturnFlights().size());
    }
}