import picocli.CommandLine;
import subcommands.ChangePassphrase;
import subcommands.InitSubcommand;
import subcommands.SaveSubcommand;
import subcommands.ShowSubcommand;

@CommandLine.Command(name = "PasswordManager",
        version = "Password manager 'pwm' 0.1",
        subcommands = {
                InitSubcommand.class,
                ChangePassphrase.class,
                SaveSubcommand.class,
                ShowSubcommand.class,
        },
        mixinStandardHelpOptions = true)
public class PasswordManager implements Runnable {
    public static void main(String[] args) {
        System.out.println("__________________________________________");
        int exitCode = new CommandLine(new PasswordManager()).execute(args);
        System.out.println("__________________________________________");
        System.exit(exitCode);
    }

    @Override
    public void run() {
    }
}
