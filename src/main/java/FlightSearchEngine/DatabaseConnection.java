package FlightSearchEngine;

import java.sql.Connection;

/**
 * Created by Alexander on 6.3.2015.
 */
public interface DatabaseConnection {
    public Connection getConnection();
}