package FlightSearchEngine;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class FlightTripTest {
    FlightTrip flightTrip;
    List<Flight> departureFlights;
    List<Flight> returnFlights;
    int totalPrice;
    FlightTrip flightTrip2;

    @Before
    public void setUp() throws Exception {
        // Create data for departure flight
        int flightNumber1 = 1;
        LocalDateTime departureTime1 = LocalDateTime.of(2015,3,5,15,30);
        LocalDateTime arrivalTime1 = LocalDateTime.of(2015,3,5,20,20);
        int price1 = 14000;
        String departureLocation1 = "Reykjavik";
        String arrivalLocation1 = "London";
        String airline1 = "FI";
        int seatsAvailable1 = 100;
        // Create data for return flight
        int flightNumber2 = 2;
        LocalDateTime departureTime2 = LocalDateTime.of(2015,4,5,13,20);
        LocalDateTime arrivalTime2 = LocalDateTime.of(2015,4,5,19,05);
        int price2 = 11000;
        String departureLocation2 = "London";
        String arrivalLocation2 = "Reykjavik";
        String airline2 = "FI";
        int seatsAvailable2 = 100;

        // Create Flights
        Flight departureFlight = new Flight(flightNumber1,departureTime1,arrivalTime1,price1,departureLocation1,arrivalLocation1,airline1,seatsAvailable1);
        Flight returnFlight = new Flight(flightNumber2,departureTime2,arrivalTime2,price2,departureLocation2,arrivalLocation2,airline2,seatsAvailable2);
        departureFlights = new ArrayList<Flight>();
        departureFlights.add(departureFlight);
        returnFlights = new ArrayList<Flight>();
        returnFlights.add(returnFlight);
        totalPrice = price1 + price2;
        flightTrip = new FlightTrip(departureFlights,returnFlights);
        // Create cheaper Flights
        int price3 = 10000;
        int price4 = 10000;
        Flight departureFlight2 = new Flight(flightNumber1,departureTime1,arrivalTime1,price3,departureLocation1,arrivalLocation1,airline1,seatsAvailable1);
        Flight returnFlight2 = new Flight(flightNumber2,departureTime2,arrivalTime2,price4,departureLocation2,arrivalLocation2,airline2,seatsAvailable2);
        List<Flight> departureFlights2 = new ArrayList<Flight>();
        departureFlights2.add(departureFlight2);
        List<Flight> returnFlights2 = new ArrayList<Flight>();
        returnFlights2.add(returnFlight2);
        flightTrip2 = new FlightTrip(departureFlights2, returnFlights2);
    }

    @After
    public void tearDown() throws Exception {
        flightTrip = null;
        departureFlights = null;
        returnFlights = null;
        totalPrice = 0;
        flightTrip2 = null;
    }

    @Test
    public void testGetDepartureFlights() throws Exception {
        assertEquals(departureFlights,flightTrip.getDepartureFlights());
    }

    @Test
    public void testGetReturnFlights() throws Exception {
        assertEquals(returnFlights,flightTrip.getReturnFlights());
    }

    @Test
    public void testGetTotalPrice() throws Exception {
        assertEquals(totalPrice,flightTrip.getTotalPrice());
    }

    @Test
    public void testSorting() throws Exception {
        List<FlightTrip> trips = new ArrayList<FlightTrip>();
        trips.add(flightTrip);
        trips.add(flightTrip2);
        Collections.sort(trips);
        assertTrue(trips.get(0).getTotalPrice() <= trips.get(1).getTotalPrice());
    }
}