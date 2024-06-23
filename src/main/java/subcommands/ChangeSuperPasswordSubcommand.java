package subcommands;

import encryption.Sha256Encryptor;
import picocli.CommandLine.*;
import utils.ConfigHandler;
import utils.DirectoryHandler;

import java.io.Console;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@Command(name = "chspw", description = "Change super password")
public class ChangeSuperPasswordSubcommand implements Runnable {

    @Override
    public void run() {
        Console console = System.console();
        if (console == null) {
            System.out.println("No console available");
            return;
        }

        String contentFolder = ConfigHandler.getConfig().getString("contentFolder");
        var checksumFile = new File(DirectoryHandler.getFullPath(contentFolder + ".checksum"));
        if (!checksumFile.exists()) System.err.println("Checksum file not found");
        String checksum;
        try {
            checksum = String.valueOf(new FileReader(checksumFile).read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        char[] oldPassword = console.readPassword("Enter your old super password: ");

        String checkingHash = new String(oldPassword);
        if (!Objects.equals(checksum, checkingHash)) {
            System.err.println("Super password does not match.");
            return;
        }

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
