package dataAccess;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider {
    public Connection connect(String databaseDirectory) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection("jdbc:sqlite:" + databaseDirectory + File.separator + "passwords.db");
    }


}
