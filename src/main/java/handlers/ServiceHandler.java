package handlers;

import dataAccess.ConnectionProvider;
import dataAccess.PasswordRepository;
import encryption.LocalPasswordGenerator;
import encryption.symmetricAlgorithms.SymmetricAlgorithm;
import utils.consoleReaders.ConsoleReader;
import utils.consoleReaders.DefaultConsoleReader;
import utils.consoleReaders.PasswordConsoleReader;
import utils.data.ServiceData;

import java.sql.SQLException;
import java.util.ArrayList;

public class ServiceHandler {
//    private final PassphraseHandler passphraseHandler = new PassphraseHandler();
    private final ConnectionProvider connectionProvider = new ConnectionProvider();
    private PasswordRepository passwordRepository = null;

    public ServiceData saveService(
            String serviceName,
            String contentFolder,
            String passphrase,
            boolean isVisible,
            LocalPasswordGenerator passwordGenerator,
            SymmetricAlgorithm algorithm) throws SQLException, ClassNotFoundException {
        if (passwordRepository == null)
            passwordRepository = new PasswordRepository(connectionProvider.connect(contentFolder));

        if (serviceExists(serviceName)) throw new RuntimeException("Service " + serviceName + " already exists");

        ConsoleReader passwordReader;
        ConsoleReader loginReader = new DefaultConsoleReader();
        if (isVisible) passwordReader = new DefaultConsoleReader();
        else passwordReader = new PasswordConsoleReader();

        String login = loginReader.readLine("Enter your login: ");
        String password;
        if (passwordGenerator == null) {
            password = passwordReader.readLine("Enter your password: ");
        } else {
            password = passwordGenerator.generatePassword();
        }


        String encryptedLogin = algorithm.encrypt(login, passphrase);
        String encryptedPassword = algorithm.encrypt(password, passphrase);

        passwordRepository.createPasswordTable();
        passwordRepository.saveService(serviceName, encryptedLogin, encryptedPassword);

        return new ServiceData(serviceName, login, password);
    }

    public ServiceData getService(
            String serviceName,
            String contentFolder,
            String passphrase,
            SymmetricAlgorithm algorithm) throws SQLException, ClassNotFoundException {
        if (passwordRepository == null)
            passwordRepository = new PasswordRepository(connectionProvider.connect(contentFolder));

        serviceName = getServiceByIndex(serviceName, contentFolder);
        if (!serviceExists(serviceName)) {
            throw new RuntimeException("Service " + serviceName + " doesn't exist");
        }

        ServiceData encryptedServiceData = passwordRepository.getService(serviceName);

        String login = algorithm.decrypt(encryptedServiceData.login(), passphrase);
        String password = algorithm.decrypt(encryptedServiceData.password(), passphrase);

        return new ServiceData(serviceName, login, password);
    }

    public ArrayList<String> getAllServices(
            String contentFolder) throws SQLException, ClassNotFoundException {
        if (passwordRepository == null)
            passwordRepository = new PasswordRepository(connectionProvider.connect(contentFolder));

        return passwordRepository.getAllServices();
    }

    public void updateServices(String oldPassphrase, String newPassphrase, String contentFolder) throws SQLException, ClassNotFoundException {

    }

    public void removeService(String serviceName, String contentFolder) throws SQLException, ClassNotFoundException {
        if (passwordRepository == null)
            passwordRepository = new PasswordRepository(connectionProvider.connect(contentFolder));

        if (!serviceExists(serviceName)) throw new RuntimeException("Service " + serviceName + " doesn't exist");

        passwordRepository.removeService(serviceName);
    }

    private boolean serviceExists(String serviceName) throws SQLException {
        ServiceData serviceData = passwordRepository.getService(serviceName);
        return serviceData != null;
    }


    private String getServiceByIndex(String serviceName, String contentFolder) throws SQLException, ClassNotFoundException {
        if (serviceName.matches("\\d+")) {
            var services = getAllServices(contentFolder);
            int index = Integer.parseInt(serviceName);
            if (index <= services.size() && index > 0) {
                serviceName = services.get(index - 1);
            } else if (!services.contains(serviceName)) {
                throw new RuntimeException("Wrong service index: " + serviceName);
            }
        }

        return serviceName;
    }


}
