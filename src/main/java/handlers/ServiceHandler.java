package handlers;

import dataAccess.ConnectionProvider;
import dataAccess.PasswordRepository;
import encryption.LocalPasswordGenerator;
import encryption.symmetricAlgorithms.SymmetricAlgorithm;
import utils.consoleReaders.ConsoleReader;
import utils.consoleReaders.DefaultConsoleReader;
import utils.consoleReaders.PasswordConsoleReader;
import utils.data.ServiceData;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class ServiceHandler {
    private final PassphraseHandler passphraseHandler = new PassphraseHandler();
    private final ConnectionProvider connectionProvider = new ConnectionProvider();
    private PasswordRepository passwordRepository = null;

    public ServiceData saveService(
            String serviceName,
            String contentFolder,
            boolean isVisible,
            LocalPasswordGenerator passwordGenerator,
            SymmetricAlgorithm algorithm) throws IOException, NoSuchAlgorithmException, SQLException, ClassNotFoundException {
        if (passwordRepository != null)
            passwordRepository = new PasswordRepository(connectionProvider.connect(contentFolder));

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

        String passphrase = passphraseHandler.getCurrentPassphrase(contentFolder, isVisible);

        String encryptedLogin = algorithm.encrypt(login, passphrase);
        String encryptedPassword = algorithm.encrypt(password, passphrase);

        passwordRepository.saveService(serviceName, encryptedLogin, encryptedPassword);

        return new ServiceData(login, password);
    }

    public ServiceData getService(
            String serviceName,
            String contentFolder,
            boolean isVisible,
            SymmetricAlgorithm algorithm) throws IOException, NoSuchAlgorithmException, SQLException, ClassNotFoundException {
        if (passwordRepository != null)
            passwordRepository = new PasswordRepository(connectionProvider.connect(contentFolder));

        String passphrase = passphraseHandler.getCurrentPassphrase(contentFolder, isVisible);

        ServiceData encryptedServiceData = passwordRepository.getService(serviceName);

        String login = algorithm.decrypt(encryptedServiceData.login(), passphrase);
        String password = algorithm.encrypt(encryptedServiceData.password(), passphrase);

        return new ServiceData(login, password);
    }
}
