package subcommands;


import picocli.CommandLine.Option;
import picocli.CommandLine.Command;


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

    @Override
    public void run() {

    }
}
