package subcommands;


import encryption.LocalPasswordGenerator;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;
import utils.contentCreators.ServiceSaver;
import utils.handlers.ConfigHandler;
import utils.handlers.CopyHandler;
import utils.handlers.DirectoryHandler;
import utils.handlers.PassphraseHandler;
import utils.readers.ConsoleReader;
import utils.readers.DefaultConsoleReader;
import utils.readers.PasswordConsoleReader;

import java.io.File;


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
        ConsoleReader reader;
        if (isVisible) reader = new DefaultConsoleReader();
        else reader = new PasswordConsoleReader();
        var contentPath = DirectoryHandler.getFullPath(ConfigHandler.getConfig().getString("contentFolder"));

        String superPassword = PassphraseHandler.getCurrentPassphrase(contentPath + File.separator + ".checksum", isVisible);
        if (superPassword == null) {
            System.out.println("Password incorrect.");
            return;
        }

        String login = new DefaultConsoleReader().readLine("Login: ");

        String password;
        if (!generate) password = reader.readLine("Password: ");
        else password = LocalPasswordGenerator.generatePassword(passwordLength);


        ServiceSaver.saveService(login, password, service, contentPath, superPassword);
        //TODO Handle results

        if (copyToClipboard) {
            CopyHandler.copyToClipboard(password, ConfigHandler.getConfig().getString("copyUtility"));
        }

        if (hidePassword) password = "*****";

        System.out.println("Saved service successfully:");
        System.out.println("Service name:\t" + service);
        System.out.println("Login:\t\t" + login);
        System.out.println("Password:\t" + password);
    }


}
