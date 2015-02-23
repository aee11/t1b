package FlightSearchEngine;

/**
 * Created by Alexander on 23.2.2015.
 */
public class Booking {
    private final String[] names;
    private final String email;
    private final int numSeats;

    public Booking(String[] names, String email, int numSeats) {
        this.names = names;
        this.email = email;
        this.numSeats = numSeats;
    }

    public Boolean bookFlightTrip(FlightTrip tripToBook) {
        return false;
    }

    public Boolean bookFlightTrip(int[] flightIds) {
        return false;
    }

    public Boolean bookFlightTrip(Flight[] flightsToBook) {
        return false;
    }
}
