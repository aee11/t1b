package FlightSearchEngine;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class BookingTest {

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
    Flight arrivalFlight = new Flight(flightNumber2,departureTime2,arrivalTime2,price2,departureLocation2,arrivalLocation2,airline2,seatsAvailable2);
    Flight[] dep = {departureFlight};
    Flight[] ari = {arrivalFlight};
    FlightTrip flightTrip = new FlightTrip(dep,ari);
    String[] names = {"Jón", "Páll", "Móses"};
    String email = "jonjonsson@jon.is";
    int numberOfSeats = 3;
    Booking booking;

    @Before
    public void setUp() throws Exception {
        booking = new Booking(names,email,numberOfSeats,flightTrip);
    }

    @After
    public void tearDown() throws Exception {
        booking = null;
    }

    @Test
    public void testGetNames() throws Exception {
        assertArrayEquals(names, booking.getNames());
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