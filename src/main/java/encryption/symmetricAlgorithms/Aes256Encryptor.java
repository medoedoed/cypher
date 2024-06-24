package encryption.symmetricAlgorithms;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class Aes256Encryptor implements SymmetricAlgorithm {
    public String encrypt(String password, String key) {
        try {
            byte[] keyBytes = key.getBytes("UTF-8");
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            byte[] encryptedBytes = cipher.doFinal(password.getBytes("UTF-8"));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
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
