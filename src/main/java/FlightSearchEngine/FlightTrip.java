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
    private List<Flight> departureFlights;

    /**
     *
     */
    private List<Flight> returnFlights;

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
    public FlightTrip(List<Flight> departureFlights, List<Flight> returnFlights) {
        this.departureFlights = departureFlights;
        this.returnFlights = returnFlights;
    }

    /**
     * @param departureFlights
     */
    public FlightTrip(List<Flight> departureFlights) {
        this.departureFlights = departureFlights;
    }

    /**
     * @return
     */
    public List<Flight> getDepartureFlights() {
        return departureFlights;
    }

    /**
     * @return
     */
    public List<Flight> getReturnFlights() {
        return returnFlights;
    }

    /**
     * @return
     */
    public int getTotalPrice() {
        int departurePrice = 0;
        for(Flight temp : departureFlights){
            departurePrice += temp.getPrice();
        }
        int returnPrice = 0;
        for(Flight temp : returnFlights){
            returnPrice += temp.getPrice();
        }
        return departurePrice + returnPrice;
    }

    /**
     * @param o
     * @return
     */
    public int compareTo(Object o) {
        if (!(o instanceof FlightTrip))
            throw new ClassCastException("A FlightTrip object expected.");
        int otherPrice = ((FlightTrip) o).getTotalPrice();
        return this.getTotalPrice() - otherPrice;
    }

}
