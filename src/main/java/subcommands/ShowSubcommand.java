package subcommands;


import com.moandjiezana.toml.Toml;
import encryption.symmetricAlgorithms.Aes256Encryptor;
import encryption.symmetricAlgorithms.SymmetricAlgorithm;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import utils.data.Constants;
import utils.data.ServiceData;
import utils.handlers.ConfigHandler;
import utils.handlers.CopyHandler;
import utils.handlers.DirectoryHandler;
import utils.handlers.PassphraseHandler;
import utils.serviceUtils.ServiceReader;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;


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
    private final ServiceReader serviceReader = new ServiceReader();
    private final PassphraseHandler passphraseHandler = new PassphraseHandler();
    private final DirectoryHandler directoryHandler = new DirectoryHandler();
    private final CopyHandler copyHandler = new CopyHandler();

    @Override
    public void run() {
        Toml config;
        SymmetricAlgorithm algorithm = new Aes256Encryptor();

        try {
            config = configHandler.getConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ServiceData serviceData;
        var contentFolder = directoryHandler.getFullPath(config.getString(Constants.CONTENT_FOLDER_KEY));
        var copyUtility = config.getString(Constants.COPY_UTILITY_KEY);

        try {
            if (!passphraseHandler.checksumExists(contentFolder, isVisible)) return;
            serviceData = serviceReader.readService(serviceName, contentFolder, isVisible, algorithm);
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        if (copyToClipboard) {
            try {
                copyHandler.copyToClipboard(serviceData.password(), copyUtility);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println(serviceData);
    }
}
