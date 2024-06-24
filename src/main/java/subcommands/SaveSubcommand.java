package subcommands;


import picocli.CommandLine.Parameters;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;
import utils.ChecksumHandler;
import utils.ConfigHandler;
import utils.DirectoryHandler;
import utils.readers.ConsoleReader;
import utils.readers.DefaultConsoleReader;
import utils.readers.PasswordConsoleReader;


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
            names = {"-s", "--special"},
            description = "Include special characters.")
    private boolean includeSpecialCharacters;

    @Option(
            names = {"-c", "--clipboard"},
            description = "Save password to clipboard.")
    private boolean copyToClipboard;

    @Option(names = {"-v", "--visible"}, description = "Show password when you enter it.", defaultValue = "false")
    private Boolean isVisible;

    @Parameters(index = "0", description = "Service name")
    private String service;

    @Override
    public void run() {
        ConsoleReader reader;
        if (isVisible) reader = new DefaultConsoleReader();
        else reader = new PasswordConsoleReader();
        var contentPath = DirectoryHandler.getFullPath(ConfigHandler.getConfig().getString("contentFolder"));

        if (ChecksumHandler.getCurrentPassword(contentPath, isVisible) == null)
    }
}
