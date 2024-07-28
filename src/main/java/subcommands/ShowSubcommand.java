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
public class ShowSubcommand implements Runnable {
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
        Toml config = null;
        SymmetricAlgorithm algorithm = new Aes256Encryptor();

        try {
            config = configHandler.getConfig();
        } catch (IOException e) {
            System.err.println("Cannot get config: " + e.getMessage());
            System.exit(1);
        }



        ServiceData serviceData = null;
        var contentFolder = directoryHandler.getFullPath(config.getString(Constants.CONTENT_FOLDER_KEY));
        var copyUtility = config.getString(Constants.COPY_UTILITY_KEY);

        try {
//            if (!passphraseHandler.checksumExists(contentFolder, isVisible)) return;
            serviceData = serviceHandler.getService(serviceName, contentFolder, isVisible, algorithm);
        } catch (IOException | NoSuchAlgorithmException | SQLException | ClassNotFoundException e) {
            System.out.println("Can't read service: " + e.getMessage());
            System.exit(1);
        }

        if (copyToClipboard) {
            try {
                copyHandler.copyToClipboard(serviceData.password(), copyUtility);
            } catch (Exception e) {
                System.out.println("Can't copy to clipboard: " + e.getMessage());
                System.exit(1);
            }
        }

        System.out.println(serviceData);
    }
}
