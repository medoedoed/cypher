package dataAccess;

import java.sql.*;
import java.util.ArrayList;

public class PasswordRepository {
    Connection connection;

    public PasswordRepository(Connection connection) {
        this.connection = connection;
    }

    public void createPasswordTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS services (" +
                    "id INTEGER primary key autoincrement ," +
                    "service_name TEXT unique," +
                    "login TEXT," +
                    "password TEXT)");
        }
    }

    public void saveService(String serviceName, String login, String password) throws SQLException {
        String query = "INSERT INTO services (service_name, login, password) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, serviceName);
            statement.setString(2, login);
            statement.setString(3, password);
            statement.executeUpdate();
        }
    }

    public ArrayList<String> getAllServices() throws SQLException {
        ArrayList<String> services = new ArrayList<>();
        String query = "SELECT service_name FROM services";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String name = resultSet.getString("service_name");
                services.add(name);
            }
        }

        return services;
    }
}
