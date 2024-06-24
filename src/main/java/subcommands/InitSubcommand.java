package subcommands;

import com.moandjiezana.toml.Toml;
import picocli.CommandLine.*;
import utils.AgreementHandler;
import utils.ChecksumHandler;
import utils.ConfigHandler;
import utils.DirectoryHandler;

import java.io.File;

@Command(name = "init",
        description = "Initialize utility.",
        mixinStandardHelpOptions = true)
public class InitSubcommand implements Runnable {

    @Option(names = {"-d", "--directory"}, description = "Set directory to init utility.")
    private String directory;

    @Option(names = {"-v", "--visible"}, description = "Show password when you enter it.", defaultValue = "false")
    private Boolean isVisible;

    @Override
    public void run() {
        String contentFolder;

        final String checksumFilename = ".checksum";
        Toml config = ConfigHandler.getConfig();

        if (directory != null && !directory.isEmpty()) {
            contentFolder = DirectoryHandler.getFullPath(directory);
        } else {
            contentFolder = DirectoryHandler.getFullPath(config.getString("contentFolder"));
        }

        //TODO change config

        var checksumPath = contentFolder + File.separator + checksumFilename;

        System.out.println(checksumPath);

        if (new File(checksumPath).exists()) {
            System.out.println("You have already initialized the checksum.");

            if (!AgreementHandler.yesNoQuestion("Want to change your super password? (y/n): ")) {
                return;
            }

            if (!ChecksumHandler.checkOldPassword(checksumPath, isVisible)) {
                System.out.println("Your super password is not correct.");
                return;
            }
        }

        ChecksumHandler.saveChecksum(checksumPath, isVisible);
        System.out.println("Initializing nice");
    }
}
