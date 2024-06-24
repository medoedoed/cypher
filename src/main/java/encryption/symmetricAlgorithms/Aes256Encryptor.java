package encryption.symmetricAlgorithms;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;

public class Aes256Encryptor implements SymmetricAlgorithm {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final int KEY_SIZE = 128; // можно использовать 192 или 256 бит
    private static final int ITERATIONS = 65536; // рекомендуется минимум 1000, но большее число итераций делает ключ сильнее
    private static final byte[] SALT = "12345678".getBytes();

    public String encrypt(String password, String passphrase) {
        try {
            SecretKeySpec secretKey = generateSecretKey(passphrase);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(password.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    private SecretKeySpec generateSecretKey(String passphrase) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), SALT, ITERATIONS, KEY_SIZE);
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public String decrypt(String hash, String key) {
        try {
            byte[] keyBytes = key.getBytes("UTF-8");
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(hash));
            return new String(decryptedBytes, "UTF-8");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
