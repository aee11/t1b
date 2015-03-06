package FlightSearchEngine;

import java.time.LocalDate;

/**
 * Created by Alexander on 18.2.2015.
 */

/**
 *
 */
public class FlightQuery {


    /**
     *
     */
    private LocalDate departureTime;

    /**
     *
     */
    private LocalDate returnTime;

    /**
     *
     */
    private String fromLocation;

    /**
     *
     */
    private String toLocation;

    /**
     *
     */
    private boolean layoverAllowed = true;

    /**
     *
     */
    private int connectionTimeMin;

    /**
     *
     */
    private int connectionTimeMax;

    /**
     *
     */
    private boolean nightFlightsOnly;

    /**
     *
     */
    private int seatsRequired;

    /**
     *
     */
    private String[] excludedAirlines;

    /**
     *
     */
    private String[] includedAirlines;


    /**
     * @param departureTime
     * @param fromLocation
     * @param toLocation
     */
    public FlightQuery(LocalDate departureTime, String fromLocation, String toLocation) {
        this.departureTime = departureTime;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
    }

    /**
     * @param departureTime
     * @param returnTime
     * @param fromLocation
     * @param toLocation
     */
    public FlightQuery(LocalDate departureTime, LocalDate returnTime, String fromLocation, String toLocation) {
        this.departureTime = departureTime;
        this.returnTime = returnTime;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
    }

    /**
     * @param layoverAllowed
     * @return
     */
    public void setLayoverAllowed(boolean layoverAllowed ) {
        this.layoverAllowed = layoverAllowed;
    }

    /**
     * @param minTime Sets the minimum connection time between two flights in minutes.
     *                Allowed values in range 60..1200
     * @param maxTime Sets the maximum connection time between two flights in minutes.
     *                Allowed values in range 60..1200
     * @throws IllegalArgumentException If minTime or maxTime are not
     *                                  within correct range.
     */
    public void setConnectionTime(int minTime, int maxTime ) {
        if (minTime < 60) {
            throw new IllegalArgumentException(minTime + " is not in range 60..20*60");
        }
        if (maxTime > 20*60) {
            throw new IllegalArgumentException(maxTime + " is not in range 60..20*60");
        }
        this.connectionTimeMin = minTime;
        this.connectionTimeMax = maxTime;
    }

    /**
     * @param nightFlightsOnly
     * @return
     */
    public void setNightFlightsOnly(boolean nightFlightsOnly ) {
        this.nightFlightsOnly = nightFlightsOnly;
    }

    /**
     * @param numSeats
     * @return
     */
    public void setSeatsRequired(int numSeats ) {
        this.seatsRequired = numSeats;
    }

    /**
     * @param airlines
     * @return
     */
    public void setExcludedAirlines(String[] airlines ) {
        excludedAirlines = airlines;
    }

    /**
     * @param airlines
     * @return
     */
    public void setIncludedAirlines(String[] airlines ) {
        includedAirlines = airlines;
    }

    /**
     * @return
     */
    public LocalDate getDepartureTime() {
        return departureTime;
    }

    /**
     * @return
     */
    public LocalDate getReturnTime() {
        return returnTime;
    }

    /**
     * @return
     */
    public String getFromLocation() {
        return fromLocation;
    }

    /**
     * @return
     */
    public String getToLocation() {
        return toLocation;
    }

    /**
     * @return
     */
    public boolean getLayoverAllowed() {
        return layoverAllowed;
    }

    /**
     * @return
     */
    public int getConnectionTimeMin() {
        return connectionTimeMin;
    }

    /**
     * @return
     */
    public int getConnectionTimeMax() {
        return connectionTimeMax;
    }

    /**
     * @return
     */
    public boolean getNightFlightsOnly() {
        return nightFlightsOnly;
    }

    /**
     * @return
     */
    public int getSeatsRequired() {
        return seatsRequired;
    }

    /**
     * @return
     */
    public String[] getExcludedAirlines() {
        return excludedAirlines;
    }

    /**
     * @return
     */
    public String[] getIncludedAirlines() {
        return includedAirlines;
    }

}