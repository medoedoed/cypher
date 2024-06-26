package encryption.asymmetricAlgorithms;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Sha256Encryptor {
    public static String encrypt(String input) {
        byte[] encodedhash = new byte[0];

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            encodedhash = digest.digest(
                    input.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return bytesToHex(encodedhash);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
