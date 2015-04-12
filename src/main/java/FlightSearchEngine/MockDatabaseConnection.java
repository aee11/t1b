package FlightSearchEngine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Alexander on 6.3.2015.
 */
public class MockDatabaseConnection implements DatabaseConnection {
    @Override
    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:sqlite:mockDb.db");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
