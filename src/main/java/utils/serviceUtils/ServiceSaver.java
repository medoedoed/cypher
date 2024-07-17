package utils.serviceUtils;

import encryption.LocalPasswordGenerator;
import encryption.symmetricAlgorithms.SymmetricAlgorithm;
import utils.data.ServiceData;
import utils.handlers.AgreementHandler;
import utils.handlers.PassphraseHandler;
import utils.consoleReaders.ConsoleReader;
import utils.consoleReaders.DefaultConsoleReader;
import utils.consoleReaders.PasswordConsoleReader;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class ServiceSaver {
    private final PassphraseHandler passphraseHandler = new PassphraseHandler();
    private final ServiceWriter serviceWriter = new ServiceWriter();

    public ServiceData saveService(
            String serviceName,
            String contentFolder,
            boolean isVisible,
            LocalPasswordGenerator generator,
            SymmetricAlgorithm encryptor) throws IOException, NoSuchAlgorithmException {
        if (serviceExists(serviceName, contentFolder)) {
            if (!new AgreementHandler().yesNoQuestion(
                    "Service \"" + serviceName + "\" already exists. " +
                    "Do you want to overwrite it? (y/n) ")) {
                return null;
            }
        }

        var defaultReader = new DefaultConsoleReader();
        ConsoleReader passwordReader;
        if (isVisible) passwordReader = new DefaultConsoleReader();
        else passwordReader = new PasswordConsoleReader();

        var passphrase = passphraseHandler.getCurrentPassphrase(contentFolder, isVisible);
        if (passphrase == null) throw new RuntimeException("Incorrect passphrase");

        var login = defaultReader.readLine("Enter login: ");

        String password;

        if (generator == null) password = passwordReader.readLine("Enter password: ");
        else password = generator.generatePassword();

        serviceWriter.writeService(
                encryptor.encrypt(login, passphrase) + "\n" +
                        encryptor.encrypt(password, passphrase) + "\n",
                serviceName,
                contentFolder);


        return new ServiceData(login, password);
    }

    public boolean serviceExists(String serviceName, String contentFolder) {
        return new File(contentFolder + File.separator + serviceName).exists();
    }

}
