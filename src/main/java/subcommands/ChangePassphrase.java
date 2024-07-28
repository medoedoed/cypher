package subcommands;

import com.moandjiezana.toml.Toml;
import picocli.CommandLine.*;
import utils.data.Constants;
import handlers.PassphraseHandler;
import handlers.ConfigHandler;
import handlers.DirectoryHandler;

import java.io.IOException;

@Command(name = "chp", description = "Change super password")
public class ChangePassphrase implements Runnable {
    private final DirectoryHandler directoryHandler = new DirectoryHandler();
    @Option(names = {"-v", "--visible"}, description = "Show password when you enter.", defaultValue = "false")
    private Boolean isVisible;

    private final PassphraseHandler passphraseHandler = new PassphraseHandler();
    @Override
    public void run() {
        Toml config = null;

        try {
            config = new ConfigHandler().getConfig();
        } catch (IOException e) {
            System.err.println("Cannot get config: " + e.getMessage());
            System.exit(1);
        }

        String contentFolder = directoryHandler.getFullPath(config.getString(Constants.CONTENT_FOLDER_KEY));

        try {
            passphraseHandler.updatePassphrase(contentFolder, isVisible);
        } catch (Exception e) {
            System.err.println("Cannot update password: " + e.getMessage());
            System.exit(1);
        }
    }
}
