package FlightSearchEngine;

/**
 * Created by Gisli on 05/03/15.
 */

import org.jooq.*;
import org.jooq.impl.DSL;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

import static FlightSearchEngine.Tables.*;
import static org.jooq.impl.DSL.*;
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
    public FlightTrip[] getResults(FlightQuery queryToSearchBy) {
        // TODO implement here

        return null;
    }

    /**
     * @param queryToSearchBy
     * @return
     */
    private String createQuery(FlightQuery queryToSearchBy) {
        SelectQuery<Record> departureTripQuery = create.select().from(FLIGHTS).getQuery();
        addMandatoryConditions(departureTripQuery, queryToSearchBy);
        addOptionalConditions(departureTripQuery, queryToSearchBy);
        return "";
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
    private Result executeQuery(String query) {
        // TODO implement here
        return null;
    }

    /**
     * @param queryResults
     * @return
     */
    private FlightTrip[] createFlightTrips(Result queryResults) {
        // TODO implement here
        return null;
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
