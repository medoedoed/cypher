package subcommands;


import com.moandjiezana.toml.Toml;
import encryption.symmetricAlgorithms.Aes256Encryptor;
import encryption.symmetricAlgorithms.SymmetricAlgorithm;
import handlers.*;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import utils.data.Constants;
import utils.data.ServiceData;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;


@Command(name = "show",
        description = "Show service.",
        mixinStandardHelpOptions = true)
public class ShowSubcommand extends Subcommand implements Runnable {
    @Option(names = {"-c", "--copy"},
            description = "Copy password to clipboard).")
    private boolean copyToClipboard;

    @Option(names = {"-v", "--visible"},
            description = "Show password when you enter it.",
            defaultValue = "false")
    private boolean isVisible;

    @Parameters(index = "0", description = "Service name.")
    private String serviceName;

    private final ConfigHandler configHandler = new ConfigHandler();
    private final ServiceHandler serviceHandler = new ServiceHandler();
    private final PassphraseHandler passphraseHandler = new PassphraseHandler();
    private final DirectoryHandler directoryHandler = new DirectoryHandler();
    private final CopyHandler copyHandler = new CopyHandler();

    @Override
    public void run() {
        Toml config = getConfig(configHandler);
        var contentFolder = directoryHandler.getFullPath(config.getString(Constants.CONTENT_FOLDER_KEY));
        var copyUtility = config.getString(Constants.COPY_UTILITY_KEY);
        SymmetricAlgorithm algorithm = new Aes256Encryptor();

        ServiceData serviceData = execute(serviceName, contentFolder, copyUtility, isVisible, copyToClipboard, algorithm);
        printOutput(serviceData);
    }

    private ServiceData execute(
            String serviceName,
            String contentFolder,
            String copyUtility,
            boolean isVisible,
            boolean copyToClipboard,
            SymmetricAlgorithm algorithm
    ) {
        ServiceData serviceData;
        try {
//            if (!passphraseHandler.checksumExists(contentFolder, isVisible)) return;
            serviceData = serviceHandler.getService(serviceName, contentFolder, isVisible, algorithm);
            if (copyToClipboard)
                copyHandler.copyToClipboard(serviceData.password(), copyUtility);
        } catch (IOException | NoSuchAlgorithmException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Can't read service: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException("Can't copy to clipboard: " + e.getMessage());
        }

        return serviceData;
    }

    private void printOutput(ServiceData serviceData) {
        System.out.println(serviceData);
    }
}
