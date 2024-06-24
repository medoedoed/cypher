package utils.contentCreators;

import encryption.symmetricAlgorithms.Aes256Encryptor;
import encryption.symmetricAlgorithms.SymmetricAlgorithm;

import java.io.File;
import java.io.FileWriter;

public class ServiceSaver {
    public static void saveService(String login, String password, String serviceName, String contentFolder, String superPassword) {
        SymmetricAlgorithm encryptor = new Aes256Encryptor();
        var contentFolderFile = new File(contentFolder);
        if (!contentFolderFile.exists())
            if (!contentFolderFile.mkdirs())
                System.err.println("Could not create folder " + contentFolderFile.getAbsolutePath());

        var serviceFile = new File(contentFolder + File.separator + serviceName);
        if (serviceFile.exists()) {
            System.err.println("Service already exists: " + serviceFile.getAbsolutePath());
            return;
        }

        try {
            if (!serviceFile.createNewFile()) System.err.println("Could not create file " + serviceFile.getAbsolutePath());
            var serviceWriter = new FileWriter(serviceFile);
            serviceWriter.write(encryptor.encrypt(login, superPassword) + System.lineSeparator() + encryptor.encrypt(password, superPassword));
        } catch (Exception e) {
            System.err.println("Could not create file " + serviceFile.getAbsolutePath());
        }
    }
}
