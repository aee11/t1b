package FlightSearchEngine;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

import static org.junit.Assert.*;

public class BookingEngineTest {
Booking booking;
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
        Flight arrivalFlight = new Flight(flightNumber2,departureTime2,arrivalTime2,price2,departureLocation2,arrivalLocation2,airline2,seatsAvailable2);
        Flight[] dep = {departureFlight};
        Flight[] ari = {arrivalFlight};
        FlightTrip flightTrip = new FlightTrip(dep,ari);
        String[] names = {"Jón", "Páll", "Móses"};
        Booking booking = new Booking(names,"jonjonsson@jon.is",3,flightTrip);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testBookFlightTrip() throws Exception {
        BookingEngine BE = new BookingEngine();
        assertTrue(BE.bookFlightTrip(booking));
    }
}