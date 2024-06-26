package utils.handlers;

import encryption.asymmetricAlgorithms.Sha256Encryptor;
import utils.data.Constants;
import utils.readers.ConsoleReader;
import utils.readers.DefaultConsoleReader;
import utils.readers.PasswordConsoleReader;


import java.io.*;

public class PassphraseHandler {
    public boolean checkCurrentPassphrase(String checksumPath, boolean isVisible) throws IOException {
        return getCurrentPassphrase(checksumPath, isVisible) != null;
    }

    public String getCurrentPassphrase(String checksumPath, boolean isVisible) throws IOException {
        return getCurrentPassphrase(checksumPath, isVisible, 0);
    }

    private String getCurrentPassphrase(String checksumPath, boolean isVisible, int iteration) throws IOException {
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

    public void updatePassphrase(String contentFolder, boolean isVisible) throws IOException {
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

        updateChecksumFile(contentFolder, Sha256Encryptor.encrypt(password));
    }

    public void saveChecksum(String contentFolder, boolean isVisible) throws IOException {
        if (getChecksumFile(contentFolder).exists()) {
            if (AgreementHandler.yesNoQuestion("Checksum file already exists. Do you want to overwrite it? (y/n)")) {
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
    }

    private File getChecksumFile(String contentFolder) {
        var checksumPath = contentFolder + File.separator + Constants.CHECKSUM_FILE_NAME;
        return new File(checksumPath);
    }

    private ConsoleReader getReader(boolean isVisible) {
        if (!isVisible) return new PasswordConsoleReader();
        return new DefaultConsoleReader();
    }

    private void saveChecksumFile(String contentFolder, String content) throws IOException {
        var checksumFile = getChecksumFile(contentFolder);

        if (!checksumFile.getParentFile().exists()) if (!checksumFile.getParentFile().mkdirs())
            throw new RuntimeException("Could not create folder " + checksumFile.getParentFile().getAbsolutePath());

        if (!checksumFile.createNewFile())
            throw new RuntimeException("Unable to create checksum file: " + checksumFile.getAbsolutePath());

        var checksumWriter = new FileWriter(contentFolder);
        checksumWriter.write(content);
        checksumWriter.close();
    }

    private void updateChecksumFile(String contentFolder, String content) throws IOException {
        var checksumFile = getChecksumFile(contentFolder);

        var checksumWriter = new FileWriter(checksumFile, false);
        checksumWriter.write(content);
        checksumWriter.close();
    }
}
