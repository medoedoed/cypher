package subcommands;


import picocli.CommandLine.Parameters;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;
import utils.ChecksumHandler;
import utils.ConfigHandler;
import utils.DirectoryHandler;
import utils.LocalPasswordGenerator;
import utils.contentCreators.ServiceSaver;
import utils.readers.ConsoleReader;
import utils.readers.DefaultConsoleReader;
import utils.readers.PasswordConsoleReader;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;

import static java.awt.SystemColor.text;


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

    @Parameters(index = "0", description = "Service name")
    private String service;

    @Override
    public void run() {
        ConsoleReader reader;
        if (isVisible) reader = new DefaultConsoleReader();
        else reader = new PasswordConsoleReader();
        var contentPath = DirectoryHandler.getFullPath(ConfigHandler.getConfig().getString("contentFolder"));
        String superPassword = ChecksumHandler.getCurrentPassword(contentPath + File.separator + ".checksum", isVisible);
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
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection selection = new StringSelection(password);
            clipboard.setContents(selection, null);
        }

        if (hidePassword) password = "*****";


        System.out.println("Saved service successfully:");
        System.out.println("Service name:\t" + service);
        System.out.println("Login:\t\t" + login);
        System.out.println("Password:\t" + password);


    }
}
