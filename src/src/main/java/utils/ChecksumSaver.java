package utils;

import encryption.Sha256Encryptor;

import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ChecksumSaver {
    public static void saveChecksum(String checksumPath) {
        Console console = System.console();
        if (console == null) {
            System.out.println("No console available");
            return;
        }

        char[] passwordArray = console.readPassword("Enter your super password: ");
        String password = new String(passwordArray);

        // TODO: add password handler (check for complexity)

        char[] checkingPasswordArray = console.readPassword("Enter your super password: ");
        String checkingPassword = new String(checkingPasswordArray);

        if (!password.equals(checkingPassword)) {
            System.err.println("Passwords do not match");
            System.exit(1);
        }

        var checksumFile = new File(checksumPath);
        if (!checksumFile.getParentFile().exists())
            if (checksumFile.getParentFile().mkdirs())
                System.err.println("Unable to create content folder");


        try {
            var checksumWriter = new FileWriter(checksumPath);
            checksumWriter.write(Sha256Encryptor.encrypt(password));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
