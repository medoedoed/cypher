package subcommands;

import com.moandjiezana.toml.Toml;
import encryption.symmetricAlgorithms.Aes256Encryptor;
import encryption.symmetricAlgorithms.SymmetricAlgorithm;
import handlers.ConfigHandler;
import handlers.DirectoryHandler;
import handlers.ServiceHandler;
import picocli.CommandLine;
import utils.data.Constants;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

@CommandLine.Command(name = "remove",
        description = "Remove service.",
        mixinStandardHelpOptions = true)
public class RemoveSubcommand extends Subcommand implements Runnable {
    @CommandLine.Parameters(index = "0", description = "Service name.")
    private String serviceName;

    private final ConfigHandler configHandler = new ConfigHandler();
    private final ServiceHandler serviceHandler = new ServiceHandler();
    private final DirectoryHandler directoryHandler = new DirectoryHandler();

    @Override
    public void run() {
        Toml config = getConfig(configHandler);
        var contentFolder = directoryHandler.getFullPath(config.getString(Constants.CONTENT_FOLDER_KEY));
        SymmetricAlgorithm algorithm = new Aes256Encryptor();
        execute(serviceName, contentFolder);
        printOutput();
    }

    private void execute(String serviceName, String contentFolder) {
        try {
//            if (!passphraseHandler.checksumExists(contentFolder, isVisible)) return;
            serviceHandler.removeService(serviceName, contentFolder);
        } catch (IOException | NoSuchAlgorithmException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Can't read service: " + e.getMessage());
        }
    }

    private void printOutput() {
        System.out.println("Service " + serviceName + " removed");
    }
}
