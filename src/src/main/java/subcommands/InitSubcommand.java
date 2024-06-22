package subcommands;

import picocli.CommandLine.*;
import utils.ConfigHandler;

@Command(name = "init",
        description = "Initialize utility.")
public class InitSubcommand implements Runnable {

    @Option(names = {"-d", "--directory"}, description = "Set directory to init utility.")
    private String directory;

    private final ConfigHandler configHandler;

    public InitSubcommand(ConfigHandler configHandler) {
        this.configHandler = configHandler;
    }

    @Override
    public void run() {
        if (configHandler.readConfig() == null)
            configHandler.createStandardConfig();
        final String contentFolder = configHandler.readConfig().getString("contentFolder");

        System.out.println("Initializing nice");
    }
}
