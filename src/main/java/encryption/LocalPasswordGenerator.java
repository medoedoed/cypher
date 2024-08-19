package encryption;

import java.security.SecureRandom;

public class LocalPasswordGenerator {
    private final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private final String DIGITS = "0123456789";
    private final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+[]{}|;:,.<>?";

    private final SecureRandom random = new SecureRandom();

    private final boolean special;
    private final int passwordLength;

    public LocalPasswordGenerator(boolean special, int passwordLength) {
        this.special = special;
        this.passwordLength = passwordLength;
    }

    public void checkForComplexity() {
        if (passwordLength < 8) {
            throw new IllegalArgumentException("Password length should be at least 8 characters for better security.");
        }
    }

    public String generatePassword() {
        checkForComplexity();

        StringBuilder password = new StringBuilder(passwordLength);

        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));

        String allChars = UPPERCASE + LOWERCASE + DIGITS;

        if (special) {
            allChars += SPECIAL_CHARACTERS;
            password.append(SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length())));
        }

        for (int i = 4; i < passwordLength; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        return shuffleString(password.toString());
    }

    private String shuffleString(String input) {
        char[] characters = input.toCharArray();

        for (int i = 0; i < characters.length; i++) {
            int randomIndex = random.nextInt(characters.length);
            char temp = characters[i];
            characters[i] = characters[randomIndex];
            characters[randomIndex] = temp;
        }

        return new String(characters);
    }
}