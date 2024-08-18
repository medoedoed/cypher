import picocli.CommandLine;
import subcommands.*;

@CommandLine.Command(name = "cypher",
        version = "0.4.0",
        subcommands = {
                InitSubcommand.class,
                UpdateSubcommand.class,
                SaveSubcommand.class,
                ShowSubcommand.class,
                ListSubcommand.class,
                RemoveSubcommand.class,
        },
        mixinStandardHelpOptions = true)
public class PasswordManager implements Runnable {

    public static void main(String[] args) {
        new CommandLine(new PasswordManager()).execute(args);
    }

    @Override
    public void run() {

    }
}
