package utils;

import encryption.Sha256Encryptor;
import utils.readers.ConsoleReader;
import utils.readers.DefaultConsoleReader;
import utils.readers.PasswordConsoleReader;


import java.io.*;

public class ChecksumHandler {

    public static Boolean checkOldPassword(String checksumPath, Boolean isVisible) {
        ConsoleReader consoleReader;
        if (isVisible) consoleReader = new DefaultConsoleReader();
        else consoleReader = new PasswordConsoleReader();

        var checksumFile = new File(checksumPath);
        if (!checksumFile.exists()) System.err.println("Checksum file not found");
        var checksumBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(checksumFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                checksumBuilder.append(line);
            }
        } catch (IOException e) {
            System.err.println("Could not read checksum file: " + e.getMessage());
            return false;
        }

        var checksum = new String(checksumBuilder);

        String oldPassword = consoleReader.readLine("Enter your old super password: ");
        String oldPasswordHash = Sha256Encryptor.encrypt(oldPassword);

        return checksum.equals(oldPasswordHash);
    }

    public static void saveChecksum(String checksumPath, Boolean isVisible) {
        ConsoleReader consoleReader;
        if (isVisible) consoleReader = new DefaultConsoleReader();
        else consoleReader = new PasswordConsoleReader();

        String password = consoleReader.readLine("Enter super password: ");

        // TODO: add password handler (check for complexity)

        String checkingPassword = consoleReader.readLine("Enter super password again: ");

        if (!password.equals(checkingPassword)) {
            System.err.println("Passwords do not match");
            return;
        }

        var checksumFile = new File(checksumPath);
        if (!checksumFile.getParentFile().exists())
            if (!checksumFile.getParentFile().mkdirs())
                System.err.println("Unable to create content folder");

        try {
            if (!checksumFile.createNewFile()) System.err.println("Unable to create checksum file");
            var checksumWriter = new FileWriter(checksumPath);
            checksumWriter.write(Sha256Encryptor.encrypt(password) + "\n");
            checksumWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
