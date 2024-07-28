package subcommands;

import com.moandjiezana.toml.Toml;
import picocli.CommandLine.*;
import utils.data.Constants;
import handlers.PassphraseHandler;
import handlers.ConfigHandler;
import handlers.DirectoryHandler;

import java.io.IOException;

@Command(name = "init",
        description = "Initialize utility.",
        mixinStandardHelpOptions = true)
public class InitSubcommand implements Runnable {
    private final DirectoryHandler directoryHandler = new DirectoryHandler();
    @Option(names = {"-d", "--directory"}, description = "Set directory to init utility.")
    private String directory;

    @Option(names = {"-v", "--visible"}, description = "Show password when you enter it.", defaultValue = "false")
    private Boolean isVisible;

    private final PassphraseHandler passphraseHandler = new PassphraseHandler();
    private final ConfigHandler configHandler = new ConfigHandler();

    @Override
    public void run() {
        String contentFolder;
        Toml config = null;

        try {
            config = configHandler.getConfig();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        if (directory != null && !directory.isEmpty()) {
            contentFolder = directoryHandler.getFullPath(directory);
        } else {
            contentFolder = directoryHandler.getFullPath(config.getString(Constants.CONTENT_FOLDER_KEY));
        }

        try {
            passphraseHandler.saveChecksum(contentFolder, isVisible);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
