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
    public ServiceData saveService(
            String serviceName,
            String contentFolder,
            boolean isVisible,
            LocalPasswordGenerator generator,
            SymmetricAlgorithm encryptor) throws IOException,

            NoSuchAlgorithmException {
        ConsoleReader passwordReader;
        if (isVisible) passwordReader = new PasswordConsoleReader();
        else passwordReader = new DefaultConsoleReader();

        var serviceFile = getServiceFile(serviceName, contentFolder);

        var passphrase = new PassphraseHandler().getCurrentPassphrase(contentFolder, isVisible);
        var login = new DefaultConsoleReader().readLine("Enter login: ");

        String password;

        if (generator != null) password = passwordReader.readLine("Enter password: ");
        else password = generator.generatePassword();

        var serviceWriter = new FileWriter(serviceFile);
        serviceWriter.write(encryptor.encrypt(login, passphrase) + "\n" + encryptor.encrypt(password, passphrase));

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
}
