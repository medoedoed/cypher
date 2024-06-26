import picocli.CommandLine;
import subcommands.ChangeSuperPasswordSubcommand;
import subcommands.InitSubcommand;
import subcommands.SaveSubcommand;

@CommandLine.Command(name = "PasswordManager",
        version = "Password manager 'pwm' 0.1",
        subcommands = {
                InitSubcommand.class,
                ChangeSuperPasswordSubcommand.class,
                SaveSubcommand.class
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
