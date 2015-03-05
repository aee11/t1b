package FlightSearchEngine;

/**
 * Created by Alexander on 23.2.2015.
 */
public class Booking {
    private final String[] names;
    private final String email;
    private final int numSeats;
    private final FlightTrip flightTrip;

    public Booking(String[] names, String email, int numSeats, FlightSearchEngine.FlightTrip flightTrip) {
        this.names = names;
        this.email = email;
        this.numSeats = numSeats;
        this.flightTrip = flightTrip;
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

    /**
     * @return
     */
    public String[] getNames() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public String getEmail() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public int getNumSeats() {
        // TODO implement here
        return 0;
    }

    /**
     * @return
     */
    public int getFlightTrip() {
        // TODO implement here
        return 0;
    }
}
