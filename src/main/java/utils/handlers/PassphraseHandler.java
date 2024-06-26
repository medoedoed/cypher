package utils.handlers;

import encryption.asymmetricAlgorithms.Sha256Encryptor;
import utils.data.Constants;
import utils.readers.ConsoleReader;
import utils.readers.DefaultConsoleReader;
import utils.readers.PasswordConsoleReader;


import java.io.*;

public class PassphraseHandler {


    public static boolean checkCurrentPassword(String checksumPath, boolean isVisible) throws IOException {
        return getCurrentPassphrase(checksumPath, isVisible) != null;
    }

    public static String getCurrentPassphrase(String checksumPath, boolean isVisible) throws IOException {
        return getCurrentPassphrase(checksumPath, isVisible, 0);
    }

    public static String getCurrentPassphrase(String checksumPath, boolean isVisible, int iteration) throws IOException {
        if (iteration >= 3) {
            return null;
        }

        var checksumFile = getChecksumFile(checksumPath);
        var reader = getReader(isVisible);

        String currentPassphrase = reader.readLine("Enter your current super password: ");
        String currentPassphraseHash = Sha256Encryptor.encrypt(currentPassphrase);

        var currentChecksum = new BufferedReader(new FileReader(checksumFile)).readLine();
        if (currentChecksum.equals(currentPassphraseHash)) return currentPassphrase;

        return getCurrentPassphrase(checksumPath, isVisible, ++iteration);
    }

    public static void updatePassphrase(String contentFolder, boolean isVisible) throws IOException {
        var checksumFile = getChecksumFile(contentFolder);
        var reader = getReader(isVisible);

        var currentPassphrase = getCurrentPassphrase(contentFolder, isVisible);
    }

    public static void saveChecksum(String contentFolder, boolean isVisible) {
        var checksumPath = contentFolder + File.separator + Constants.CHECKSUM_FILE_NAME;

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

        var checksumFile = new File(contentFolder);
        if (!checksumFile.getParentFile().exists())
            if (!checksumFile.getParentFile().mkdirs())
                System.err.println("Could not create folder " + checksumFile.getParentFile().getAbsolutePath());

        try {
            if (!checksumFile.delete())
                System.err.println("Unable to update super password: " + checksumFile.getAbsolutePath());
            if (!checksumFile.createNewFile()) System.err.println("Unable to create checksum file");
            var checksumWriter = new FileWriter(contentFolder);
            checksumWriter.write(Sha256Encryptor.encrypt(password) + "\n");
            checksumWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static File getChecksumFile(String contentFolder) {
        var checksumPath = contentFolder + File.separator + Constants.CHECKSUM_FILE_NAME;
        return new File(checksumPath);
    }

    private static ConsoleReader getReader(boolean isVisible) {
        if (!isVisible) return new PasswordConsoleReader();
        return new DefaultConsoleReader();
    }
}
