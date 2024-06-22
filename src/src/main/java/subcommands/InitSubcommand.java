package subcommands;

import picocli.CommandLine.*;

@Command(name = "init",
        description = "Initialize utility.")
public class InitSubcommand implements Runnable {

    @Option(names = {"-d", "--directory"}, description = "Set directory to init utility.")
    String directory;


    @Override
    public void run() {
        System.out.println("Initializing..." + directory);

    }
}
