package utils;

import encryption.Sha256Encryptor;
import utils.readers.ConsoleReader;
import utils.readers.DefaultConsoleReader;
import utils.readers.PasswordConsoleReader;


import java.io.*;

public class ChecksumHandler {
    public static Boolean checkCurrentPassword(String checksumPath, Boolean isVisible) {
        return checkCurrentPassword(checksumPath, isVisible, 0);
    }
    public static Boolean checkCurrentPassword(String checksumPath, Boolean isVisible, int iteration) {
        if (iteration == 3) return false;
        if (getCurrentPassword(checksumPath, isVisible) != null) return true;
        else return checkCurrentPassword(checksumPath, isVisible, iteration + 1);
    }

    public static String getCurrentPassword(String checksumPath, Boolean isVisible) {
        var checksumFile = new File(checksumPath);
        if (!checksumFile.exists()) {
            System.err.println("Checksum file not found");
            return null;
        }

        ConsoleReader consoleReader;
        if (isVisible) consoleReader = new DefaultConsoleReader();
        else consoleReader = new PasswordConsoleReader();
        var checksumBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(checksumFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                checksumBuilder.append(line);
            }
        } catch (IOException e) {
            System.err.println("Could not read checksum file: " + e.getMessage());
            return null;
        }

        var checksum = checksumBuilder.toString();

        String currentPassword = consoleReader.readLine("Enter your current super password: ");
        String oldPasswordHash = Sha256Encryptor.encrypt(currentPassword);

        if (checksum.equals(oldPasswordHash)) return currentPassword;
        return null;
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
                System.err.println("Could not create folder " + checksumFile.getParentFile().getAbsolutePath());

        try {
            if (!checksumFile.delete()) System.err.println("Unable to update super password: " + checksumFile.getAbsolutePath());
            if (!checksumFile.createNewFile()) System.err.println("Unable to create checksum file");
            var checksumWriter = new FileWriter(checksumPath);
            checksumWriter.write(Sha256Encryptor.encrypt(password) + "\n");
            checksumWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
