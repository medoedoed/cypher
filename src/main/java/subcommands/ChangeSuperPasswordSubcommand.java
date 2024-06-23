package subcommands;

import picocli.CommandLine.*;

import java.io.Console;

@Command(name = "chspw", description = "Change super password")
public class ChangeSuperPasswordSubcommand implements Runnable {

    @Override
    public void run() {
        Console console = System.console();
        if (console == null) {
            System.out.println("No console available");
            return;
        }

        char[] oldPassword = console.readPassword("Enter your old super password: ");
        

        char[] passwordArray = console.readPassword("Enter your super password: ");
        String password = new String(passwordArray);

        char[] checkingPasswordArray = console.readPassword("Enter your super password: ");
        String checkingPassword = new String(checkingPasswordArray);

        if (!password.equals(checkingPassword)) {
            System.err.println("Passwords do not match");
        }

        // TODO: save checksum
    }
}
