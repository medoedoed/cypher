import picocli.CommandLine;
import picocli.CommandLine.*;

import java.io.File;

public class PasswordManager {
    @Option(names = {"-v", "--verbose"}, description = "Verbose mode. Helpful for troubleshooting. " + "Multiple -v options increase the verbosity.")
    private boolean[] verbose = new boolean[0];

    @CommandLine.Option(names = {"-h", "--help"}, usageHelp = true, description = "Displays this help message and quits.")
    private boolean helpRequested = false;

    @CommandLine.Parameters(arity = "1..*", paramLabel = "FILE", description = "File(s) to process.")
    private File[] inputFiles;

    public void run() {
        if (verbose.length > 0) {
            System.out.println(inputFiles.length + " files to process...");
        }
        if (verbose.length > 1) {
            for (File f : inputFiles) {
                System.out.println(f.getAbsolutePath());
            }
        }
    }

    public static void main(String[] args) {
        new CommandLine(new PasswordManager()).execute(args);
    }
}
