package FlightSearchEngine;

import java.util.Date;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.DSL.*;

/**
 * Created by Alexander on 18.2.2015.
 */

import java.util.*;

/**
 *
 */
public class FlightQuery {


    /**
     *
     */
    private Date departureTime;

    /**
     *
     */
    private Date returnTime;

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
    private boolean layoverAllowed;

    /**
     *
     */
    private int layoverTimeMin;

    /**
     *
     */
    private int layoverTimeMax;

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
    public FlightQuery(Date departureTime, String fromLocation, String toLocation) {
        // TODO implement here
    }

    /**
     * @param departureTime
     * @param returnTime
     * @param fromLocation
     * @param toLocation
     */
    public FlightQuery(Date departureTime, Date returnTime, String fromLocation, String toLocation) {
        // TODO implement here
    }

    /**
     * @param layoverAllowed
     * @return
     */
    public void setLayoverAllowed(boolean layoverAllowed ) {
        // TODO implement here
        return null;
    }

    /**
     * @param minTime
     * @param maxTime
     * @return
     */
    public void setLayoverTime(int minTime , int maxTime ) {
        // TODO implement here
        return null;
    }

    /**
     * @param nightFlightsOnly
     * @return
     */
    public void setNightFlightsOnly(boolean nightFlightsOnly ) {
        // TODO implement here
        return null;
    }

    /**
     * @param numSeats
     * @return
     */
    public void setSeatsRequired(int numSeats ) {
        // TODO implement here
        return null;
    }

    /**
     * @param airlines
     * @return
     */
    public void setExcludedAirlines(String[] airlines ) {
        // TODO implement here
        return null;
    }

    /**
     * @param airlines
     * @return
     */
    public void setIncludedAirlines(String[] airlines ) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public Date getDepartureTime() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public Date getReturnTime() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public String getFromLocation() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public String getToLocation() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public boolean getLayoverAllowed() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public int getLayoverTimeMin() {
        // TODO implement here
        return 0;
    }

    /**
     * @return
     */
    public int getLayoverTimeMax() {
        // TODO implement here
        return 0;
    }

    /**
     * @return
     */
    public int getNightFlightsOnly() {
        // TODO implement here
        return 0;
    }

    /**
     * @return
     */
    public int getSeatsRequired() {
        // TODO implement here
        return 0;
    }

    /**
     * @return
     */
    public String[] getExcludedAirlines() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public String[] getIncludedAirlines() {
        // TODO implement here
        return null;
    }

}