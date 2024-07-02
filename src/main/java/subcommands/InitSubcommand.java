package subcommands;

import com.moandjiezana.toml.Toml;
import picocli.CommandLine.*;
import utils.data.Constants;
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
            config = new ConfigHandler().getConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
//            System.err.println(e.getMessage());
//            System.exit(1);
        }

        if (directory != null && !directory.isEmpty()) {
            contentFolder = DirectoryHandler.getFullPath(directory);
        } else {
            contentFolder = DirectoryHandler.getFullPath(config.getString(Constants.CONTENT_FOLDER_KEY));
        }

        try {
            new PassphraseHandler().saveChecksum(contentFolder, isVisible);
        } catch (Exception e) {
            throw new RuntimeException(e);
//            System.err.println(e.getMessage());
//            System.exit(1);
        }
    }
}
