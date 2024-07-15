package utils.contentCreators;

import encryption.LocalPasswordGenerator;
import encryption.symmetricAlgorithms.SymmetricAlgorithm;
import utils.data.ServiceData;
import utils.handlers.AgreementHandler;
import utils.handlers.PassphraseHandler;
import utils.readers.ConsoleReader;
import utils.readers.DefaultConsoleReader;
import utils.readers.PasswordConsoleReader;

import java.io.File;
import java.io.FileWriter;
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
            if (new AgreementHandler().yesNoQuestion("Service \"" + serviceName + "\" already exists. Do you want to overwrite it? (y/n) ")) {
                updateService(serviceName, contentFolder);
            } else {
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

        var serviceData = new ServiceData(login, password);

        serviceWriter.writeService(
                encryptor.encrypt(login, passphrase) + "\n" + encryptor.encrypt(password, passphrase) + "\n",
                serviceName,
                contentFolder);

        System.out.println(encryptor.encrypt(login, passphrase) + "\n" + encryptor.encrypt(password, passphrase));

        serviceWriter.close();

        return serviceData;
    }

    public boolean serviceExists(String serviceName, String contentFolder) {
        return new File(contentFolder + File.separator + serviceName).exists();
    }

    public void updateService(String serviceName, String contentFolder) {
        //TODO realize updating
    }
}
