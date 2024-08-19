package utils.handlers;

import encryption.asymmetricAlgorithms.Sha256Encryptor;
import utils.data.Constants;
import utils.consoleReaders.ConsoleReader;
import utils.consoleReaders.DefaultConsoleReader;
import utils.consoleReaders.PasswordConsoleReader;


import java.io.*;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;

public class PassphraseHandler {

    public boolean checkCurrentPassphrase(String checksumPath, boolean isVisible) throws IOException, NoSuchAlgorithmException {
        return getCurrentPassphrase(checksumPath, isVisible) != null;
    }

    public String getCurrentPassphrase(String checksumPath, boolean isVisible) throws IOException, NoSuchAlgorithmException {
        return getCurrentPassphrase(checksumPath, isVisible, 0);
    }

    private String getCurrentPassphrase(String checksumPath, boolean isVisible, int iteration) throws IOException, NoSuchAlgorithmException {
        int MAX_ITERATIONS = 3;

        if (iteration >= MAX_ITERATIONS) {
            return null;
        }

        var checksumFile = getChecksumFile(checksumPath);
        var reader = getReader(isVisible);

        String currentPassphrase = reader.readLine("Passphrase: ");
        String currentPassphraseHash = Sha256Encryptor.encrypt(currentPassphrase);

        var currentChecksum = new BufferedReader(new FileReader(checksumFile)).readLine();
        if (currentChecksum.equals(currentPassphraseHash)) return currentPassphrase;

        if (iteration <= MAX_ITERATIONS - 2) System.out.println("Sorry, try again.");
        return getCurrentPassphrase(checksumPath, isVisible, ++iteration);
    }

    public void updatePassphrase(String contentFolder, boolean isVisible) throws IOException, NoSuchAlgorithmException {
        if (!checksumExists(contentFolder, isVisible)) return;

        if (!checkCurrentPassphrase(contentFolder, isVisible)) {
            throw new RuntimeException("Passphrase update failed.");
        }

        var reader = getReader(isVisible);
        String password = reader.readLine("Enter new passphrase: ");
        // TODO: add password handler (check for complexity)
        String checkingPassword = reader.readLine("Enter new passphrase again: ");

        if (!password.equals(checkingPassword)) {
            throw new RuntimeException("Passphrases don't match.");
        }

        // Maybe I'll add choosing of encrypting algorithm
        saveChecksumFile(contentFolder, Sha256Encryptor.encrypt(password));
        System.out.println("Passphrase updated successfully.");
    }

    public void saveChecksum(String contentFolder, boolean isVisible) throws IOException, NoSuchAlgorithmException {
        if (checksumExists(contentFolder, isVisible)) {
            if (new AgreementHandler().yesNoQuestion("Checksum file already exists. Do you want to overwrite it? (y/n): ")) {
                updatePassphrase(contentFolder, isVisible);
                return;
            }

            return;
        }

        var reader = getReader(isVisible);

        String password = reader.readLine("Enter passphrase: ");
        // TODO: add password handler (check for complexity)
        String checkingPassword = reader.readLine("Enter passphrase again: ");

        if (!password.equals(checkingPassword)) {
            throw new RuntimeException("Passphrases don't match.");
        }

        saveChecksumFile(contentFolder, Sha256Encryptor.encrypt(password));
        System.out.println("Passphrase saved successfully.");
    }

    private File getChecksumFile(String contentFolder) throws IOException {
        var checksumFile = new File(contentFolder + File.separator + Constants.CHECKSUM_FILE_NAME);

        if (!checksumFile.getParentFile().exists() && !checksumFile.getParentFile().mkdirs())
            throw new RuntimeException("Could not create folder " + checksumFile.getParentFile().getAbsolutePath());
        if (!checksumFile.exists() && !checksumFile.createNewFile())
            throw new RuntimeException("Could not create file " + checksumFile.getAbsolutePath());
        return checksumFile;
    }

    public boolean checksumExists(String contentFolder, boolean isVisible) throws IOException, NoSuchAlgorithmException {
        var checksumFile = getChecksumFile(contentFolder);
        if (!checksumFile.exists()) return false;
        if (!Files.readAllLines(getChecksumFile(contentFolder).toPath()).isEmpty()) return true;

        if (new AgreementHandler().yesNoQuestion("Checksum file doesn't exists. Do you want to create it? (y/n): ")) {
            saveChecksum(contentFolder, isVisible);
            return false;
        }

        return true;
    }

    private ConsoleReader getReader(boolean isVisible) {
        if (!isVisible) return new PasswordConsoleReader();
        return new DefaultConsoleReader();
    }

    private void saveChecksumFile(String contentFolder, String content) throws IOException {
        var checksumWriter = new FileWriter(getChecksumFile(contentFolder));
        checksumWriter.write(content + "\n");
        checksumWriter.close();
    }
}
