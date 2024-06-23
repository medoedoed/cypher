import picocli.CommandLine;
import picocli.CommandLine.*;
import subcommands.ChangeSuperPasswordSubcommand;
import subcommands.InitSubcommand;
import utils.ConfigHandler;

import java.io.*;
import java.util.Objects;


@Command(name = "PasswordManager",
        version = "pwm 0.1",
        subcommands = {
                InitSubcommand.class,
                ChangeSuperPasswordSubcommand.class
        },
        mixinStandardHelpOptions = true)
public class PasswordManager implements Runnable {

    public static void main(String[] args) throws IOException {
//        String argsTest = "init";
//        System.out.println("______________________________________");
//        int exitCode = new CommandLine(new PasswordManager()).execute(args);
//        System.out.println("______________________________________");
//        System.exit(exitCode);
        System.out.println("Hello world!");
    }

    @Override
    public void run() {
    }
}
