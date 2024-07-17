package utils.handlers;

import utils.consoleReaders.DefaultConsoleReader;

public class AgreementHandler {
    public boolean yesNoQuestion(String question) {
        return yesNoQuestion(question, 0);
    }

    private boolean yesNoQuestion(String question, int iteration) {
        if (iteration >= 3) {
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
