package utils.readers;

import java.io.Console;

public class DefaultConsoleReader implements ConsoleReader {
    @Override
    public String readLine(String prompt) {
        Console console = System.console();
        if (console == null) {
            throw new RuntimeException("No console available");
        }

        return console.readLine(prompt);
    }
}
