package FlightSearchEngine;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

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
                "        '50000'," +
                "        '100');";
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
                "        '30000'," +
                "        '20');";
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
                "        '25000'," +
                "        '5');";
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
                "        'Copenhagen'," +
                "        'Reykjavik'," +
                "        '30000'," +
                "        '30');";
        stmt.executeUpdate(sql);
        stmt.close();
        flightEngine = new FlightEngine(mockDBConn);
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void testGetResults() throws Exception {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT * FROM Flights;" );
        while ( rs.next() ) {
            int flightNumber = rs.getInt("flightNumber");
            String  airline = rs.getString("airline");
            String  departureTime = rs.getString("departureTime");
            String  arrivalTime = rs.getString("arrivalTime");
            String  departureLocation = rs.getString("departureLocation");
            String  arrivalLocation = rs.getString("arrivalLocation");
            int price = rs.getInt("price");
            int seatsAvailable = rs.getInt("seatsAvailable");
            System.out.println( "flightNumber = " + flightNumber );
            System.out.println( "airline = " + airline );
            System.out.println( "departureTime = " + departureTime );
            System.out.println( "arrivalTime = " + arrivalTime );
            System.out.println( "departureLocation = " + departureLocation);
            System.out.println( "arrivalLocation = " + arrivalLocation);
            System.out.println( "price = " + price);
            System.out.println( "seatsAvailable = " + seatsAvailable);
            System.out.println();
        }
        LocalDate departureDate =  LocalDate.of(2015,3,30);
        FlightQuery query = new FlightQuery(departureDate,"Reykjavik","London");
        FlightTrip[] results = flightEngine.getResults(query);
        assertEquals(2,results.length);
    }
}