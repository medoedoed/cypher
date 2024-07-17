package utils.serviceUtils;

import encryption.symmetricAlgorithms.SymmetricAlgorithm;
import utils.data.ServiceData;
import utils.handlers.PassphraseHandler;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class ServiceReader {
    private final ServiceDecoder serviceDecoder = new ServiceDecoder();
    private final PassphraseHandler passphraseHandler = new PassphraseHandler();

    public ServiceData readService(String serviceName, String contentFolder, boolean isVisible, SymmetricAlgorithm algorithm) throws IOException, NoSuchAlgorithmException {
        var serviceFile = getServiceFile(serviceName, contentFolder);
        if (serviceFile == null) throw new RuntimeException("Cannot find service: " + serviceName);
        var passphrase = passphraseHandler.getCurrentPassphrase(contentFolder, isVisible);
        return serviceDecoder.getServiceData(serviceFile, algorithm, passphrase);
    }

    private File getServiceFile(String serviceName, String contentFolder) {
        for (File file : Objects.requireNonNull(new File(contentFolder).listFiles())) {
            if (file.getName().equals(serviceName)) {
                return file;
            }
        }

        return null;
    }
}
