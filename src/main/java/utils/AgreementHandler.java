package utils;

import utils.readers.DefaultConsoleReader;

import java.io.Console;

public class AgreementHandler {
    public static Boolean yesNoQuestion(String question) {
        return yesNoQuestion(question, 0);
    }

    public static Boolean yesNoQuestion(String question, int iteration) {
        if (iteration == 3) {
            return false;
        }

        var reader = new DefaultConsoleReader();
        var answer = reader.readLine(question);

        if (answer.equalsIgnoreCase("y")) {
            return true;
        } else if (answer.equalsIgnoreCase("n")) {
            return false;
        }

        return yesNoQuestion(question, ++iteration);
    }
}
