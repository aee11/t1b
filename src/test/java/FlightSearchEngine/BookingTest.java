package FlightSearchEngine;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

public class BookingTest {
    Booking booking;
    List<String> names;
    List<Flight> dep;
    List<Flight> ret;
    String email;
    int numberOfSeats;
    FlightTrip flightTrip;

    @Before
    public void setUp() throws Exception {
        int flightNumber1 = 1;
        LocalDateTime departureTime1 = LocalDateTime.of(2015,3,5,15,30);
        LocalDateTime arrivalTime1 = LocalDateTime.of(2015,3,5,20,20);
        int price1 = 14000;
        String departureLocation1 = "Reykjavik";
        String arrivalLocation1 = "London";
        String airline1 = "FI";
        int seatsAvailable1 = 100;
        int flightNumber2 = 2;
        LocalDateTime departureTime2 = LocalDateTime.of(2015,4,5,13,20);
        LocalDateTime arrivalTime2 = LocalDateTime.of(2015,4,5,19,05);
        int price2 = 11000;
        String departureLocation2 = "London";
        String arrivalLocation2 = "Reykjavik";
        String airline2 = "FI";
        int seatsAvailable2 = 100;


        Flight departureFlight = new Flight(flightNumber1,departureTime1,arrivalTime1,price1,departureLocation1,arrivalLocation1,airline1,seatsAvailable1);
        Flight returnFlight = new Flight(flightNumber2,departureTime2,arrivalTime2,price2,departureLocation2,arrivalLocation2,airline2,seatsAvailable2);
        dep.add(departureFlight);
        ret.add(returnFlight);
        flightTrip = new FlightTrip(dep,ret);
        names.add("Jón");
        names.add("Páll");
        names.add("Móses");
        email = "jonjonsson@jon.is";
        numberOfSeats = 3;
        booking = new Booking(names,email,numberOfSeats,flightTrip);
    }

    @After
    public void tearDown() throws Exception {
        booking = null;
    }

    @Test
    public void testGetNames() throws Exception {
        assertEquals(names, booking.getNames());
    }

    @Test
    public void testGetEmail() throws Exception {
        assertEquals(email,booking.getEmail());
    }

    @Test
    public void testGetNumSeats() throws Exception {
        assertEquals(numberOfSeats,booking.getEmail());
    }

    @Test
    public void testGetFlightTrip() throws Exception {
        assertEquals(flightTrip, booking.getFlightTrip());
    }
}