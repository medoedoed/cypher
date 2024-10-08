package dataAccess;

import utils.data.ServiceData;

import java.sql.*;
import java.util.ArrayList;

public class PasswordRepository {
    Connection connection;

    public PasswordRepository(Connection connection) {
        this.connection = connection;
    }

    public PasswordRepository() {}

    public void connect(Connection connection) {
        this.connection = connection;
    }

    public void createPasswordTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS services (" +
                    "id INTEGER primary key autoincrement ," +
                    "service_name TEXT unique," +
                    "login TEXT," +
                    "password TEXT)");
        } catch (Exception e) {
            throw new RuntimeException("Can't get connection to database. Try 'cypher init'.");
        }
    }

    public void saveService(String serviceName, String login, String password) throws SQLException {
        String query = "INSERT INTO services (service_name, login, password) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, serviceName);
            statement.setString(2, login);
            statement.setString(3, password);
            statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Can't get connection to database. Try 'cypher init'.");
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
        } catch (Exception e) {
            throw new RuntimeException("Can't get connection to database. Try 'cypher init'.");
        }

        return services;
    }

    public ServiceData getService(String serviceName) throws SQLException {
        String query = "SELECT login, password FROM services WHERE service_name = ?";
        String login = null;
        String password = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, serviceName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    login = resultSet.getString("login");
                    password = resultSet.getString("password");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Can't get connection to database. Try 'cypher init'.");
        }

        if (login == null || password == null) {
            return null;
        }

        return new ServiceData(serviceName, login, password);
    }

    public void removeService(String serviceName) throws SQLException {
        String query = "DELETE FROM services WHERE service_name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, serviceName);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Can't get connection to database. Try 'cypher init'.");
        }
    }
}
