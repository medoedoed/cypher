package subcommands;


import com.moandjiezana.toml.Toml;
import encryption.LocalPasswordGenerator;
import encryption.symmetricAlgorithms.Aes256Encryptor;
import encryption.symmetricAlgorithms.SymmetricAlgorithm;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;
import utils.contentCreators.ServiceSaver;
import utils.data.Constants;
import utils.data.ServiceData;
import utils.handlers.ConfigHandler;
import utils.handlers.CopyHandler;
import utils.handlers.DirectoryHandler;
import utils.handlers.PassphraseHandler;
import utils.readers.ConsoleReader;
import utils.readers.DefaultConsoleReader;
import utils.readers.PasswordConsoleReader;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;


@Command(name = "save",
        description = "Save service.",
        mixinStandardHelpOptions = true)
public class SaveSubcommand implements Runnable {
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
            names = {"-c", "--clipboard"},
            description = "Save password to clipboard.")
    private boolean copyToClipboard;

    @Option(names = {"-v", "--visible"}, description = "Show password when you enter it.", defaultValue = "false")
    private boolean isVisible;

    @Option(names = {"-h", "--hide"}, description = "Hide password after savin service", defaultValue = "false")
    private boolean hidePassword;

    @Option(names = {"-s", "--special"}, description = "Use special characters", defaultValue = "false")
    private boolean useSpecialCharacters;

    @Parameters(index = "0", description = "Service name")
    private String serviceName;

    private final ConfigHandler configHandler = new ConfigHandler();
    private final ServiceSaver serviceSaver = new ServiceSaver();

    @Override
    public void run() {
        Toml config;
        SymmetricAlgorithm algorithm = new Aes256Encryptor();
        // TODO: add choosing of algorithm (in config)

        try {
            config = configHandler.getConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var contentFolder = config.getString(Constants.CONTENT_FOLDER_KEY);

        LocalPasswordGenerator passwordGenerator = null;
        if (generatePassword) passwordGenerator = new LocalPasswordGenerator(useSpecialCharacters, passwordLength);

        try {
            serviceSaver.saveService(serviceName, contentFolder, isVisible, passwordGenerator, algorithm);
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
