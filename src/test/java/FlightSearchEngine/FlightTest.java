package FlightSearchEngine;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class FlightTest {

    int flightNumber1 = 1;
    LocalDateTime departureTime1 = LocalDateTime.of(2015,3,5,15,30);
    LocalDateTime arrivalTime1 = LocalDateTime.of(2015,3,5,20,20);
    int price1 = 14000;
    String departureLocation1 = "Reykjavik";
    String arrivalLocation1 = "London";
    String airline1 = "FI";
    int seatsAvailable1 = 100;
    Flight fl;

    @Before
    public void setUp() throws Exception {
        fl = new Flight(flightNumber1,departureTime1,arrivalTime1,price1,departureLocation1,arrivalLocation1,airline1,seatsAvailable1);
    }

    @After
    public void tearDown() throws Exception {
        fl = null;
    }

    @Test
    public void testGetFlightId() throws Exception {;
        assertEquals(flightNumber1, fl.getFlightId());

    }

    @Test
    public void testGetDepartureTime() throws Exception {
        assertEquals(departureTime1,fl.getDepartureTime());
    }

    @Test
    public void testGetArrivalTime() throws Exception {
        assertEquals(arrivalTime1,fl.getArrivalTime());
    }

    @Test
    public void testGetPrice() throws Exception {
        assertEquals(price1,fl.getPrice());
    }

    @Test
    public void testGetDepartureLocation() throws Exception {
        assertEquals(departureLocation1,fl.getDepartureLocation());
    }

    @Test
    public void testGetArrivalLocation() throws Exception {
        assertEquals(arrivalLocation1,fl.getArrivalLocation());
    }

    @Test
    public void testGetAirline() throws Exception {
        assertEquals(airline1,fl.getAirline());
    }

    @Test
    public void testGetSeatsAvailable() throws Exception {
        assertEquals(seatsAvailable1,fl.getSeatsAvailable());
    }
}