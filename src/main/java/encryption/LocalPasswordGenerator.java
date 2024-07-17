package encryption;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

public class LocalPasswordGenerator {
    private final boolean special;
    private final int passwordLength;

    public LocalPasswordGenerator(boolean special, int passwordLength) {
        this.special = special;
        this.passwordLength = passwordLength;
    }

    public String generatePassword() {
        CharacterRule uppercaseRule = new CharacterRule(EnglishCharacterData.UpperCase, 2);
        CharacterRule lowercaseRule = new CharacterRule(EnglishCharacterData.LowerCase, 2);
        CharacterRule digitRule = new CharacterRule(EnglishCharacterData.Digit, 3);
        CharacterRule specialRule = new CharacterRule(EnglishCharacterData.Special, 4);
        PasswordGenerator generator = new PasswordGenerator();

        if (this.special) {
            return generator.generatePassword(this.passwordLength, uppercaseRule, lowercaseRule, digitRule, specialRule);
        }

        return generator.generatePassword(this.passwordLength, uppercaseRule, lowercaseRule, digitRule);
    }
}