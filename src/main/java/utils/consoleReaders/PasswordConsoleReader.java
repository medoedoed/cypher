package utils.consoleReaders;

import java.io.Console;

public class PasswordConsoleReader implements ConsoleReader {
    @Override
    public String readLine(String prompt) {
        Console console = System.console();
        if (console == null) {
            throw new RuntimeException("No console available");
        }

        char[] passwordArray = console.readPassword(prompt);
        return new String(passwordArray);
    }
}
