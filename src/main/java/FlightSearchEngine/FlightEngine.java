package FlightSearchEngine;

/**
 * Created by Gisli on 05/03/15.
 */

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import static FlightSearchEngine.Tables.*;
import static org.jooq.impl.DSL.*;
/**
 *
 */
public class FlightEngine {
    final String DB_URL = "jdbc:sqlite:flightSearchEngine.db";
    Connection conn;
    DSLContext create;

    public FlightEngine() {
        setupDbConnection();
    }

    private void setupDbConnection() {
        try {
            conn = DriverManager.getConnection(DB_URL);
            create = DSL.using(conn, SQLDialect.SQLITE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        // TODO implement here
        return null;
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
