package handlers;

import java.io.IOException;
import java.io.OutputStream;

public class CopyHandler {
    public void copyToClipboard(String text, String copyUtility) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(copyUtility);
        Process process = processBuilder.start();
        try (OutputStream os = process.getOutputStream()) {
            os.write(text.getBytes());
            os.flush();
        }
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            System.err.println(copyUtility + "exited with code " + exitCode);
        }
    }
}