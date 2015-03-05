package FlightSearchEngine;

import java.util.Date;

/**
 * Created by Alexander on 18.2.2015.
 */
public class Flight {
    private int flightNumber;
    private Date departureTime;
    private Date arrivalTime;
    private int price;
    private String departureLocation;
    private String arrivalLocation;
    private String airline;
    private int seatsAvailable;

    public Flight(int flightNumber, Date departureTime, Date arrivalTime, int price, String departureLocation, String arrivalLocation, String airline, int seatsAvailable) {
        this.flightNumber = flightNumber;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.departureLocation = departureLocation;
        this.arrivalLocation = arrivalLocation;
        this.airline = airline;
        this.seatsAvailable = seatsAvailable;
    }

    public int getFlightId() {
        return 0;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public int getPrice() {
        return price;
    }

    public String getDepartureLocation() {
        return departureLocation;
    }

    public String getArrivalLocation() {
        return arrivalLocation;
    }

    public String getAirline() {
        return airline;
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }
}
