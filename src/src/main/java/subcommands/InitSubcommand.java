package subcommands;

import picocli.CommandLine.*;
import utils.ChecksumSaver;
import utils.ConfigHandler;

import java.io.File;

@Command(name = "init",
        description = "Initialize utility.")
public class InitSubcommand implements Runnable {

    @Option(names = {"-d", "--directory"}, description = "Set directory to init utility.")
    private String directory;

    private final ConfigHandler configHandler = new ConfigHandler();

    @Override
    public void run() {
        if (configHandler.readConfig() == null)
            configHandler.createStandardConfig();
        final String contentFolder = configHandler.readConfig().getString("contentFolder");
        var checksumPath = contentFolder + File.separator + ".checksum";

        if (new File(checksumPath).exists()) {
            System.out.println("You have already initialized the checksum");
            return;
        }

        ChecksumSaver.saveChecksum(checksumPath);
        System.out.println("Initializing nice");
    }
}
