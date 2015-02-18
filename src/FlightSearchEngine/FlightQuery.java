package FlightSearchEngine;

import java.util.Date;

/**
 * Created by Alexander on 18.2.2015.
 */
public class FlightQuery {
    private String flightQuery;

    public FlightQuery departureTime(Date time) {
        return this;
    }

    public FlightQuery returnTime(Date time) {
        return this;
    }

    public FlightQuery from(String location) {
        return this;
    }

    public FlightQuery to(String location) {
        return this;
    }

    public FlightQuery sortBy(String field) // default: by price
    {
        return this;
    }

    public FlightQuery allowLayover(Boolean layoverAllowed) {
        return this;
    }

    public FlightQuery layoverTime(int minTime, int maxTime) {
        return this;
    }

    public FlightQuery onlyNightFlights(Boolean nightFlightsOnly) {
        return this;
    }

    public FlightQuery seatsRequired(int numSeats) {
        return this;
    }

    public FlightQuery excludeAirlines(String[] airlines) {
        return this;
    }

    public FlightQuery includeAirlines(String[] airlines) {
        return this;
    }

    public FlightTrip[] fetch() {
        return new FlightTrip[0];
    }
}
