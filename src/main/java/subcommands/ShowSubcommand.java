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
public class ShowSubcommand extends Subcommand {
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

    private String contentFolder;
    String copyUtility;
    SymmetricAlgorithm algorithm = new Aes256Encryptor();
    private ServiceData serviceData = null;

    @Override
    void getDataFromConfig() {
        Toml config = getConfig(configHandler);

        contentFolder = directoryHandler.getFullPath(config.getString(Constants.CONTENT_FOLDER_KEY));
        copyUtility = config.getString(Constants.COPY_UTILITY_KEY);
    }

    @Override
    void execute() throws Exception {
        serviceData = serviceHandler.getService(serviceName, contentFolder, isVisible, algorithm);
        if (copyToClipboard)
            copyHandler.copyToClipboard(serviceData.password(), copyUtility);
    }

    @Override
    void printOutput() {
        if (serviceData == null) System.out.println("[ERROR]: Can't get service data");
        System.out.println("[service]:\t" + serviceName);
        System.out.println(serviceData);
    }
}
