package FlightSearchEngine;

import java.util.Date;

/**
 * Created by Alexander on 18.2.2015.
 */
public class FlightQuery {
    private String flightQuery;

    public FlightQuery setDepartureTime(Date time) {
        return this;
    }

    public FlightQuery setReturnTime(Date time) {
        return this;
    }

    public FlightQuery setFrom(String location) {
        return this;
    }

    public FlightQuery setTo(String location) {
        return this;
    }

    public FlightQuery sortBy(String field) // default: by price
    {
        return this;
    }

    public FlightQuery allowLayover(Boolean layoverAllowed) {
        return this;
    }

    public FlightQuery setLayoverTime(int minTime, int maxTime) {
        return this;
    }

    public FlightQuery setOnlyNightFlights(Boolean nightFlightsOnly) {
        return this;
    }

    public FlightQuery setSeatsRequired(int numSeats) {
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
