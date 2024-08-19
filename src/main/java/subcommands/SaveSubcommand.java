package subcommands;


import com.moandjiezana.toml.Toml;
import encryption.LocalPasswordGenerator;
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


@Command(name = "save",
        description = "Save service.",
        mixinStandardHelpOptions = true)
public class SaveSubcommand extends Subcommand {
    @Option(
            names = {"-g", "--generate"},
            description = "Generate password.")
    private boolean generatePassword;

    @Option(
            names = {"-l", "--length"},
            description = "Specify the length of the password (default length is 20).",
            defaultValue = "20")
    private int passwordLength;

    @Option(
            names = {"-c", "--copy"},
            description = "Save password to clipboard.")
    private boolean copyToClipboard;

    @Option(names = {"-v", "--visible"}, description = "Show password when you enter it.", defaultValue = "false")
    private boolean isVisible;

    @Option(names = {"-h", "--hide"}, description = "Hide password after saving service.", defaultValue = "false")
    private boolean hidePassword;

    @Option(names = {"-s", "--special"}, description = "Use special characters.", defaultValue = "false")
    private boolean useSpecialCharacters;

    @Parameters(index = "0", description = "Service name.")
    private String serviceName;

    private final ConfigHandler configHandler = new ConfigHandler();
    private final ServiceHandler serviceHandler = new ServiceHandler();
    private final DirectoryHandler directoryHandler = new DirectoryHandler();
    private final CopyHandler copyHandler = new CopyHandler();

    private String contentFolder;
    private String copyUtility;
    private LocalPasswordGenerator passwordGenerator = null;
    private SymmetricAlgorithm algorithm = new Aes256Encryptor();
    private ServiceData serviceData = null;

    @Override
    void getDataFromConfig() {
        Toml config = getConfig(configHandler);
        contentFolder = directoryHandler.getFullPath(config.getString(Constants.CONTENT_FOLDER_KEY));
        copyUtility = config.getString(Constants.COPY_UTILITY_KEY);
    }

    @Override
    void execute() throws Exception {
        if (generatePassword) {
            passwordGenerator = new LocalPasswordGenerator(useSpecialCharacters, passwordLength);
            passwordGenerator.checkForComplexity();
        }
        serviceData = serviceHandler.saveService(serviceName, contentFolder, isVisible, passwordGenerator, algorithm);
        if (serviceData == null) return;
        if (copyToClipboard)
            copyHandler.copyToClipboard(serviceData.password(), copyUtility);
    }

    @Override
    void printOutput() {
        if (hidePassword) serviceData = new ServiceData(serviceData.serviceName(), serviceData.login(), "*****");
        System.out.println(serviceData);
    }
}

