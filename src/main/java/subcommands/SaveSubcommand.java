package subcommands;


import com.moandjiezana.toml.Toml;
import encryption.LocalPasswordGenerator;
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


@Command(name = "save",
        description = "Save service.",
        mixinStandardHelpOptions = true)
public class SaveSubcommand extends Subcommand implements Runnable {
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
    private final PassphraseHandler passphraseHandler = new PassphraseHandler();
    private final DirectoryHandler directoryHandler = new DirectoryHandler();
    private final CopyHandler copyHandler = new CopyHandler();

    @Override
    public void run() {
        Toml config = getConfig(configHandler);
        var contentFolder = directoryHandler.getFullPath(config.getString(Constants.CONTENT_FOLDER_KEY));
        var copyUtility = config.getString(Constants.COPY_UTILITY_KEY);
        SymmetricAlgorithm algorithm = new Aes256Encryptor();
        // TODO: add choosing of algorithm (in config)

        LocalPasswordGenerator passwordGenerator = null;
        if (generatePassword) passwordGenerator = new LocalPasswordGenerator(useSpecialCharacters, passwordLength);

        ServiceData serviceData = execute(serviceName, contentFolder, copyUtility, isVisible, copyToClipboard, passwordGenerator, algorithm);
        if (serviceData == null) return;

        printOutoput(serviceData, hidePassword);
    }

    private ServiceData execute(
            String serviceName,
            String contentFolder,
            String copyUtility,
            boolean isVisible,
            boolean copyToClipboard,
            LocalPasswordGenerator passwordGenerator,
            SymmetricAlgorithm algorithm
    ) {
        ServiceData serviceData;
        try {
//            if (!passphraseHandler.checksumExists(contentFolder, isVisible)) return;

            serviceData = serviceHandler.saveService(serviceName, contentFolder, isVisible, passwordGenerator, algorithm);
            if (serviceData == null) return null;
            if (copyToClipboard)
                copyHandler.copyToClipboard(serviceData.password(), copyUtility);
        } catch (IOException | NoSuchAlgorithmException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Can't save service: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException("Can't copy to clipboard: " + e.getMessage());
        }

        return serviceData;
    }

    private void printOutoput(ServiceData serviceData, boolean hidePassword) {
        if (hidePassword) serviceData = new ServiceData(serviceData.login(), "*****");
        System.out.println(serviceData);
    }
}

