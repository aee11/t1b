package FlightSearchEngine;

import java.util.Date;

/**
 * Created by Alexander on 18.2.2015.
 */

import java.util.*;

/**
 *
 */
public class FlightTrip implements Comparable {

    /**
     *
     */
    private Flight[] departureFlights;

    /**
     *
     */
    private Flight[] returnFlights;

    /**
     *
     */
    public Comparator DURATION_COMPARATOR;

    /**
     *
     */
    public Comparator DEPARTURE_TIME_COMPARATOR;

    /**
     *
     */
    public Comparator ARRIVAL_TIME_COMPARATOR;

    /**
     * @param departureFlights
     * @param returnFlights
     */
    public FlightTrip(Flight[] departureFlights, Flight[] returnFlights) {
        // TODO implement here
    }

    /**
     * @return
     */
    public Flight[] getDepartureFlights() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public Flight[] getReturnFlights() {
        // TODO implement here
        return null;
    }

    /**
     * @param currency
     * @return
     */
    public int getTotalPrice(String currency) {
        // TODO implement here
        return 0;
    }

    /**
     * @param o
     * @return
     */
    public int compareTo(Object o) {
        // TODO implement here
        return 0;
    }

}
