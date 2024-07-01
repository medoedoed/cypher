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
    private boolean generate;

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
    private boolean special;

    @Parameters(index = "0", description = "Service name")
    private String service;

    @Override
    public void run() {
        Toml config;
        String superPassword;
        SymmetricAlgorithm algorithm = new Aes256Encryptor();

        try {
            config = new ConfigHandler().getConfig();
        } catch (IOException e) {
            throw new RuntimeException("Cannot get config: " + e.getMessage());
        }

        String contentFolder = DirectoryHandler.getFullPath(config.getString(Constants.CONTENT_FOLDER_KEY));

        try {
            superPassword = new PassphraseHandler().getCurrentPassphrase(contentFolder, isVisible);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (superPassword == null) {
            throw new RuntimeException("Password incorrect.");
        }

        LocalPasswordGenerator generator = null;

        if (generate) {
            generator = new LocalPasswordGenerator(special, passwordLength);
        }

        ServiceData serviceData;

        try {
            serviceData = new ServiceSaver().saveService(service, contentFolder, isVisible, generator, algorithm);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //TODO Handle results

        if (copyToClipboard) {
            try {
                new CopyHandler().copyToClipboard(serviceData.password(), config.getString(Constants.COPY_UTILITY_KEY));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


        System.out.println("Saved service successfully:");
        System.out.println("Service name:\t" + service);
        System.out.println("Login:\t\t" + serviceData.login());
        if (hidePassword)
            System.out.println("Password:\t*****");
        else
            System.out.println("Password:\t" + serviceData.password());
    }
}
