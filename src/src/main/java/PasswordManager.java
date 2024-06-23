import encryption.Sha256Encryptor;
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
        String argsTest = "init";
        System.out.println("______________________________________");
        int exitCode = new CommandLine(new PasswordManager()).execute(args);
        System.out.println("______________________________________");
        System.exit(exitCode);
    }

    @Override
    public void run() {
    }
}
