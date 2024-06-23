package subcommands;

import com.moandjiezana.toml.Toml;
import picocli.CommandLine.*;
import utils.ChecksumSaver;
import utils.ConfigHandler;
import utils.DirectoryHandler;

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
        String contentFolder;

        final String checksumFilename = ".checksum";

        Toml config = configHandler.readConfig();
        System.out.println(config.getString("contentFolder"));


        if (directory != null) {
            contentFolder = DirectoryHandler.getFullPath(directory);
        } else {
            contentFolder = DirectoryHandler.getFullPath(config.getString("contentFolder"));
        }

        var checksumPath = contentFolder + File.separator + checksumFilename;

        if (new File(checksumPath).exists()) {
            System.out.println("You have already initialized the checksum. \n" +
                    "Want to change you super password? (y/n)");

            return;
        }

        ChecksumSaver.saveChecksum(checksumPath);
        System.out.println("Initializing nice");
    }
}
