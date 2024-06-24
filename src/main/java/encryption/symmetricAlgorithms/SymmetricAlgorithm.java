package encryption.symmetricAlgorithms;

public interface SymmetricAlgorithm {
    String encrypt(String password, String key);

    String decrypt(String hash, String key);
}
