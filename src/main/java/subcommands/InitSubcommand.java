package subcommands;

import com.moandjiezana.toml.Toml;
import picocli.CommandLine.*;
import utils.handlers.PassphraseHandler;
import utils.handlers.ConfigHandler;
import utils.handlers.DirectoryHandler;

import java.io.IOException;

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
        Toml config = null;

        try {
            config = ConfigHandler.getConfig();
        } catch (IOException e) {
            throw new RuntimeException("Cannot get config: " + e.getMessage());
        }

        if (directory != null && !directory.isEmpty()) {
            contentFolder = DirectoryHandler.getFullPath(directory);
        } else {
            contentFolder = DirectoryHandler.getFullPath(config.getString("contentFolder"));
        }

//        if (new File(contentFolder).exists()) {
//            System.out.println("You have already initialized the checksum.");
//
//            if (!AgreementHandler.yesNoQuestion("Want to change your super password? (y/n): ")) {
//                return;
//            }
//
//            if (!PassphraseHandler.checkCurrentPassword(checksumPath, isVisible)) {
//                System.out.println("Your super password is not correct.");
//                return;
//            }
//        }

        PassphraseHandler.saveChecksum(contentFolder, isVisible);
    }
}
