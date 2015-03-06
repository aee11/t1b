package FlightSearchEngine;

/**
 * Created by Gisli on 05/03/15.
 */

import org.jooq.*;
import org.jooq.impl.DSL;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static FlightSearchEngine.Tables.*;

/**
 *
 */
public class FlightEngine {
    private Connection conn;
    private DSLContext create;

    public FlightEngine(DatabaseConnection dbConn) {
        setupDbConnection(dbConn);
    }

    private void setupDbConnection(DatabaseConnection dbConn) {
        conn = dbConn.getConnection();
        create = DSL.using(conn, SQLDialect.SQLITE);
    }

    /**
     * @param queryToSearchBy
     * @return
     */
    public List<FlightTrip> getResults(FlightQuery queryToSearchBy) {
        SelectQuery<Record> departureTripQuery = createDepartureQuery(queryToSearchBy);
        // TODO Get returnTripQuery if there is a return flight specified
        Result<Record> result = executeQuery(departureTripQuery);
        List<FlightTrip> flightTrips = createFlightTrips(result);
        return flightTrips;
    }

    /**
     * @param queryToSearchBy
     * @return
     */
    private SelectQuery<Record> createDepartureQuery(FlightQuery queryToSearchBy) {
        SelectQuery<Record> departureTripQuery = create.select().from(FLIGHTS).getQuery();
        addMandatoryConditions(departureTripQuery, queryToSearchBy);
        addOptionalConditions(departureTripQuery, queryToSearchBy);
        return departureTripQuery;
    }

    private void addOptionalConditions(SelectQuery<Record> query, FlightQuery flightQuery) {
        query.addConditions(FLIGHTS.SEATSAVAILABLE.greaterOrEqual(flightQuery.getSeatsRequired()));
    }

    private void addMandatoryConditions(SelectQuery<Record> query, FlightQuery flightQuery) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        String departureTime = flightQuery.getDepartureTime()
                                          .format(dateFormatter);
        query.addConditions(FLIGHTS.DEPARTURETIME.startsWith(departureTime));
        query.addConditions(FLIGHTS.DEPARTURELOCATION.equal(flightQuery.getFromLocation()));
        query.addConditions(FLIGHTS.ARRIVALLOCATION.equal(flightQuery.getToLocation()));
    }

    /**
     * @param query
     * @return
     */
    private Result executeQuery(SelectQuery<Record> query) {
        return query.fetch();
    }

    /**
     * @param queryResults
     * @return
     */
    private List<FlightTrip> createFlightTrips(Result<Record> queryResults) {
        List<FlightTrip> departureFlightTrips = new ArrayList<FlightTrip>();
        DateTimeFormatter dbDateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
        for (Record result : queryResults) {
            int flightNumber = (int) result.getValue(FLIGHTS.FLIGHTNUMBER);
            String sDepartureTime = (String) result.getValue(FLIGHTS.DEPARTURETIME);
            String sArrivalTime = (String) result.getValue(FLIGHTS.ARRIVALTIME);
            String departureLocation = (String) result.getValue(FLIGHTS.DEPARTURELOCATION);
            String arrivalLocation = (String) result.getValue(FLIGHTS.ARRIVALLOCATION);
            int price = (int) result.getValue(FLIGHTS.PRICE);
            String airline = (String) result.getValue(FLIGHTS.AIRLINE);
            int seatsAvailable = (int) result.getValue(FLIGHTS.SEATSAVAILABLE);
            LocalDateTime departureTime = LocalDateTime.parse(sDepartureTime, dbDateFormatter);
            LocalDateTime arrivalTime = LocalDateTime.parse(sArrivalTime, dbDateFormatter);
            Flight flight = new Flight(flightNumber, departureTime, arrivalTime, price,
                                       departureLocation, arrivalLocation, airline, seatsAvailable);
            List<Flight> departureFlights = new ArrayList<Flight>();
            departureFlights.add(flight);
            FlightTrip departureFlightTrip = new FlightTrip(departureFlights);
            departureFlightTrips.add(departureFlightTrip);
        }
        return departureFlightTrips;
    }

    /**
     * @param fromLocation
     * @param toLocation
     * @return
     */
    private FlightTrip[] searchLayoverFlights(String fromLocation, String toLocation) {
        // TODO implement here
        return null;
    }

}
