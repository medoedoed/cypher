package utils.readers;

import java.io.Console;

public class DefaultConsoleReader implements ConsoleReader {
    @Override
    public String readLine(String prompt) {
        Console console = System.console();
        if (console == null) {
            System.out.println("No console available");
            return null;
        }

        return console.readLine("Enter your new super password: ");
    }
}
