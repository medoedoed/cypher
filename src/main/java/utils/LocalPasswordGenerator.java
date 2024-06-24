package utils;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

public class LocalPasswordGenerator {
    public static String generatePassword(int length) {
        CharacterRule uppercaseRule = new CharacterRule(EnglishCharacterData.UpperCase, 2);
        CharacterRule lowercaseRule = new CharacterRule(EnglishCharacterData.LowerCase, 2);
        CharacterRule digitRule = new CharacterRule(EnglishCharacterData.Digit, 2);
        CharacterRule specialRule = new CharacterRule(EnglishCharacterData.Special, 2);
        PasswordGenerator generator = new PasswordGenerator();
        return generator.generatePassword(12, uppercaseRule, lowercaseRule, digitRule, specialRule);
    }
}