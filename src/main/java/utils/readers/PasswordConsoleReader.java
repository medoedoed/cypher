package utils.readers;

import java.io.Console;

public class PasswordConsoleReader implements ConsoleReader {
    @Override
    public String readLine(String prompt) {
        Console console = System.console();
        if (console == null) {
            System.out.println("No console available");
            return null;
        }

        char[] passwordArray = console.readPassword(prompt);
        return new String(passwordArray);
    }
}
