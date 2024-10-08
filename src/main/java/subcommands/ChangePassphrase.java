package subcommands;

import com.moandjiezana.toml.Toml;
import picocli.CommandLine.*;
import utils.data.Constants;
import utils.handlers.PassphraseHandler;
import utils.handlers.ConfigHandler;
import utils.handlers.DirectoryHandler;

import java.io.IOException;

@Command(name = "chp", description = "Change super password")
public class ChangePassphrase implements Runnable {
    private final DirectoryHandler directoryHandler = new DirectoryHandler();
    @Option(names = {"-v", "--visible"}, description = "Show password when you enter.", defaultValue = "false")
    private Boolean isVisible;

    @Override
    public void run() {
        Toml config;

        try {
            config = new ConfigHandler().getConfig();
        } catch (IOException e) {
            throw new RuntimeException("Cannot get config: " + e.getMessage());
        }

        String contentFolder = directoryHandler.getFullPath(config.getString(Constants.CONTENT_FOLDER_KEY));

        try {
            new PassphraseHandler().updatePassphrase(contentFolder, isVisible);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
