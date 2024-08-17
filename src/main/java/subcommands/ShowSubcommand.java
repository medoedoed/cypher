package subcommands;


import com.moandjiezana.toml.Toml;
import encryption.symmetricAlgorithms.Aes256Encryptor;
import encryption.symmetricAlgorithms.SymmetricAlgorithm;
import handlers.ConfigHandler;
import handlers.CopyHandler;
import handlers.DirectoryHandler;
import handlers.ServiceHandler;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import utils.data.Constants;
import utils.data.ServiceData;


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

    @Parameters(index = "0", description = "Service name or index in list.")
    private String serviceName;

    private final ConfigHandler configHandler = new ConfigHandler();
    private final ServiceHandler serviceHandler = new ServiceHandler();
    private final DirectoryHandler directoryHandler = new DirectoryHandler();
    private final CopyHandler copyHandler = new CopyHandler();

    private static String contentFolder;

    @Override
    public void run() {
        Toml config = getConfig(configHandler);
        var contentFolder = directoryHandler.getFullPath(config.getString(Constants.CONTENT_FOLDER_KEY));
        var copyUtility = config.getString(Constants.COPY_UTILITY_KEY);
        SymmetricAlgorithm algorithm = new Aes256Encryptor();

        ServiceData serviceData = execute(serviceName, contentFolder, copyUtility, isVisible, copyToClipboard, algorithm);
        if (serviceData == null) return;
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

        return serviceData;
    }

    private void printOutput(ServiceData serviceData) {
        System.out.println("[login]:\t" + serviceData.login());
        System.out.println("[password]:\t" + serviceData.password());
    }
}
