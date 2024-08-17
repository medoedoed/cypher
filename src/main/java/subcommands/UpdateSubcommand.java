package subcommands;

import com.moandjiezana.toml.Toml;
import handlers.ConfigHandler;
import handlers.DirectoryHandler;
import handlers.PassphraseHandler;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import utils.data.Constants;

@Command(name = "update", description = "Change super password")
public class UpdateSubcommand extends Subcommand implements Runnable {
    private final DirectoryHandler directoryHandler = new DirectoryHandler();
    @Option(names = {"-v", "--visible"}, description = "Show password when you enter.", defaultValue = "false")
    private Boolean isVisible;

    private final PassphraseHandler passphraseHandler = new PassphraseHandler();
    private final ConfigHandler configHandler = new ConfigHandler();

    @Override
    public void run() {
        Toml config = getConfig(configHandler);
        String contentFolder = directoryHandler.getFullPath(config.getString(Constants.CONTENT_FOLDER_KEY));
        boolean isComplex = config.getLong(Constants.COMPLEX_PASSPHRASE_KEY) != 0;

        execute(contentFolder, isComplex);
    }

    protected void execute(String contentFolder, boolean isComplex) {
        try {
            passphraseHandler.updatePassphrase(contentFolder, isVisible, isComplex);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
