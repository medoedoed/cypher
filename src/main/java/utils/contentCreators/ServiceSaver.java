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

        var serviceFile = getServiceFile(serviceName, contentFolder);
        var serviceWriter = new FileWriter(serviceFile);
        serviceWriter.write(encryptor.encrypt(login, passphrase) + "\n" + encryptor.encrypt(password, passphrase) + "\n");

        System.out.println(encryptor.encrypt(login, passphrase) + "\n" + encryptor.encrypt(password, passphrase));

        serviceWriter.close();

        return new ServiceData(login, password);
    }

    private File getServiceFile(String serviceName, String contentFolder) throws IOException {
        var serviceFile = new File(contentFolder + File.separator + serviceName);

        if (!serviceFile.getParentFile().exists()) if (!serviceFile.getParentFile().mkdirs())
            throw new RuntimeException("Could not create folder " + serviceFile.getParentFile().getAbsolutePath());


        if (serviceFile.exists()) {
            throw new RuntimeException("Service " + serviceName + " already exists");
//            if (new AgreementHandler().yesNoQuestion("Service already exists: " + serviceFile.getName() + "Want to rewrite password? (y/n)")) {
//                System.out.println("TODO");
//                // TODO
//                //  ???? pls kirill v next time pishi chto nado todo
//            }
//
//            return;
        }

        if (!serviceFile.createNewFile())
            throw new RuntimeException("Could not create file " + serviceFile.getAbsolutePath());
        return serviceFile;
    }

    public boolean serviceExists(String serviceName, String contentFolder) {
        var serviceFile = new File(contentFolder + File.separator + serviceName);
        return serviceFile.exists();
    }

    public void updateService(String serviceName, String contentFolder) {
        //TODO realize updating
    }
}
