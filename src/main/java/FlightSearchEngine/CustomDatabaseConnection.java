package FlightSearchEngine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Alexander on 6.3.2015.
 */
public class CustomDatabaseConnection implements DatabaseConnection {
    private Connection conn;
    public CustomDatabaseConnection(Connection conn) {
        this.conn = conn;
    }
    @Override
    public Connection getConnection() {
        return this.conn;
    }
}
