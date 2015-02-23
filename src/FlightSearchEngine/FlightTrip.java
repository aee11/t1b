package FlightSearchEngine;

import java.util.Date;

/**
 * Created by Alexander on 18.2.2015.
 */
public class FlightTrip implements Comparable {
    private Flight[] departureFlights;
    private Flight[] returnFlights;
//  TODO: Add different comparators (e.g. duration, departure and arrival time)

    public FlightTrip(Flight[] departureFlights, Flight[] returnFlights) {

    }

    public int[] getFlightIds() {
        return null;
    }

    public Flight[] getDepartureFlights() {
        return departureFlights;
    }

    public Flight[] getReturnFlights() {
        return returnFlights;
    }

    public Date[] getTimes() {
        return null;
    }

    public int[] getNumberOfStops() {
        return null;
    }

    public int[] getFlightDurations() {
        return null;
    }

    public String[] getLocations() {
        return null;
    }

    public int getTotalPrice(String currency) {
        return 0;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

}
