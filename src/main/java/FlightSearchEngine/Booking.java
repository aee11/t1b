package FlightSearchEngine;

import java.util.List;

/**
 * Created by Alexander on 23.2.2015.
 */
public class Booking {
    private final List<String> names;
    private final String email;
    private final int numSeats;
    private final FlightTrip flightTrip;
    private Integer bookingId;

    public Booking(List<String> names, String email, int numSeats, FlightSearchEngine.FlightTrip flightTrip) {
        this.names = names;
        this.email = email;
        this.numSeats = numSeats;
        this.flightTrip = flightTrip;
    }

    /**
     * @return
     */
    public List<String> getNames() {
        return names;
    }

    /**
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return
     */
    public int getNumSeats() {
        return numSeats;
    }

    /**
     * @return
     */
    public FlightTrip getFlightTrip() {
        return flightTrip;
    }

    void setBookingId(int id) {
        if (bookingId == null) {
            bookingId = id;
        } else {
            throw new IllegalStateException();
        }
    }

    int getBookingId() {
        return bookingId;
    }

    void deleteBooking() {
        bookingId = null;
    }
}
