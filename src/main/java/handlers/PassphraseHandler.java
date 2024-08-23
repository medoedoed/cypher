package handlers;

import encryption.hashCode.Sha256;
import utils.consoleReaders.ConsoleReader;
import utils.consoleReaders.DefaultConsoleReader;
import utils.consoleReaders.PasswordConsoleReader;
import utils.data.Constants;

import java.io.*;
import java.security.NoSuchAlgorithmException;

public class PassphraseHandler {
    private final AgreementHandler agreementHandler = new AgreementHandler();

    public boolean checkCurrentPassphrase(String checksumPath, boolean isVisible) throws IOException, NoSuchAlgorithmException {
        return getCurrentPassphrase(checksumPath, isVisible) != null;
    }

    public String getCurrentPassphrase(String checksumPath, boolean isVisible) throws IOException, NoSuchAlgorithmException {
        checkChecksum(checksumPath);
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
        String currentPassphraseHash = Sha256.encrypt(currentPassphrase);

        var currentChecksum = new BufferedReader(new FileReader(checksumFile)).readLine();
        if (currentChecksum.equals(currentPassphraseHash)) return currentPassphrase;

        if (iteration <= MAX_ITERATIONS - 2) System.out.println("Sorry, try again.");
        return getCurrentPassphrase(checksumPath, isVisible, ++iteration);
    }


    public void updatePassphrase(String contentFolder, boolean isVisible, boolean isComplex) throws IOException, NoSuchAlgorithmException {
        if (!checksumExists(contentFolder)){
            saveChecksum(contentFolder, isVisible, isComplex);
            return;
        }

        if (!checkCurrentPassphrase(contentFolder, isVisible)) {
            throw new RuntimeException("Wrong passphrase!");
        }

        var reader = getReader(isVisible);
        String password = reader.readLine("Enter new passphrase: ");

        if (isComplex && isComplex(password)) throw new RuntimeException("You should to use more complex passphrase");

        String checkingPassword = reader.readLine("Enter new passphrase again: ");

        if (!password.equals(checkingPassword)) {
            throw new RuntimeException("Passphrases don't match.");
        }

        // Maybe I'll add choosing of encrypting algorithm
        saveChecksumFile(contentFolder, Sha256.encrypt(password));
    }

    public void saveChecksum(String contentFolder, boolean isVisible, boolean isComplex) throws IOException, NoSuchAlgorithmException {
        if (checksumExists(contentFolder)) {
            if (agreementHandler.yesNoQuestion("Checksum file already exists. Do you want to overwrite it? (y/n): ")) {
                updatePassphrase(contentFolder, isVisible, isComplex);
                return;
            }

            return;
        }

        var reader = getReader(isVisible);

        String password = reader.readLine("Enter passphrase: ");

        if (isComplex && !isComplex(password)) throw new RuntimeException("You should use more complex passphrase");

        String checkingPassword = reader.readLine("Enter passphrase again: ");

        if (!password.equals(checkingPassword)) {
            throw new RuntimeException("Passphrases don't match.");
        }

        saveChecksumFile(contentFolder, Sha256.encrypt(password));
    }

    private File getChecksumFile(String contentFolder) throws IOException {
        var checksumFile = new File(contentFolder + File.separator + Constants.CHECKSUM_FILE_NAME);

        if (!checksumFile.getParentFile().exists() && !checksumFile.getParentFile().mkdirs())
            throw new RuntimeException("Can't create folder: " + checksumFile.getParentFile().getAbsolutePath());
        if (!checksumFile.exists() && !checksumFile.createNewFile())
            throw new RuntimeException("Can't not create file: " + checksumFile.getAbsolutePath());
        return checksumFile;
    }

    public boolean checksumExists(String contentFolder) throws IOException {
        var checksumFile = getChecksumFile(contentFolder);
        if (!checksumFile.exists()) return false;
        var currentChecksum = new BufferedReader(new FileReader(checksumFile)).readLine();
        if (currentChecksum == null) return false;
        return !currentChecksum.isEmpty();
    }

    public void checkChecksum(String contentFolder) throws IOException {
        if (!checksumExists(contentFolder))
            throw new RuntimeException("Checksum file doesn't exist. Try 'cypher init'.");
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

    private boolean isComplex(String passphrase) {
        return passphrase.length() >= 16;
    }
}
