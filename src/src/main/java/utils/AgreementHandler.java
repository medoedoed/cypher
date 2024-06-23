package utils;

import java.io.Console;

public class AgreementHandler {
    public Boolean yesNoQuestion(String question) {
        return yesNoQuestion(question, 0);
    }

    public Boolean yesNoQuestion(String question, int iteration) {
        if (iteration == 3) {
            return false;
        }
        var console = System.console();
        if (console == null) {
            System.out.println("No console available");
            return false;
        }
        System.out.println(question);
        String answer = console.readLine();
        if (answer.equalsIgnoreCase("y")) {
            return true;
        } else if (answer.equalsIgnoreCase("n")) {
            return false;
        }

        return yesNoQuestion(question, ++iteration);
    }
}
