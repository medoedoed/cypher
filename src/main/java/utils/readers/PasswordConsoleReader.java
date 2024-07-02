package utils.readers;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;

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
