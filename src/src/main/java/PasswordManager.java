import picocli.CommandLine;
import picocli.CommandLine.*;
import subcommands.ChangeSuperPasswordSubcommand;
import subcommands.InitSubcommand;


@Command(name = "PasswordManager",
        version = "pwm 0.1",
        subcommands = {
                InitSubcommand.class,
                ChangeSuperPasswordSubcommand.class
        },
        mixinStandardHelpOptions = true)
public class PasswordManager implements Runnable {


    public static void main(String[] args) {
        int exitCode = new CommandLine(new PasswordManager()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
    }
}
