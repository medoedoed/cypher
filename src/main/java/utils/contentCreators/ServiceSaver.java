package utils.contentCreators;

import encryption.symmetricAlgorithms.SymmetricAlgorithm;
import utils.handlers.AgreementHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ServiceSaver {
    public static void saveService(String login, String password, String serviceName, String contentFolder, String superPassword, SymmetricAlgorithm encryptor) throws IOException {
        var contentFolderFile = new File(contentFolder);

        if (!contentFolderFile.exists())
            if (!contentFolderFile.mkdirs()) {
                throw new RuntimeException("Could not create folder " + contentFolderFile.getAbsolutePath());
            }

        var serviceFile = new File(contentFolder + File.separator + serviceName);

        if (serviceFile.exists()) {
            if (AgreementHandler.yesNoQuestion("Service already exists: " + serviceFile.getName() + "Want to rewrite password? (y/n)")) {
                System.out.println("TODO");
                // TODO
            }

            return;
        }

        if (!serviceFile.createNewFile())
            throw new RuntimeException("Could not create file " + serviceFile.getAbsolutePath());

        var serviceWriter = new FileWriter(serviceFile);
        serviceWriter.write(encryptor.encrypt(login, superPassword) + System.lineSeparator() + encryptor.encrypt(password, superPassword));
    }
}
