package subcommands;

import com.moandjiezana.toml.Toml;
import handlers.ConfigHandler;
import handlers.DirectoryHandler;
import handlers.PassphraseHandler;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import utils.data.Constants;

@Command(name = "update", description = "Change super password")
public class UpdateSubcommand extends Subcommand {
    private final DirectoryHandler directoryHandler = new DirectoryHandler();
    @Option(names = {"-v", "--visible"}, description = "Show password when you enter.", defaultValue = "false")
    private Boolean isVisible;

    private final PassphraseHandler passphraseHandler = new PassphraseHandler();
    private final ConfigHandler configHandler = new ConfigHandler();

    private String contentFolder;
    private boolean isComplex;

    @Override
    void getDataFromConfig() {
        Toml config = getConfig(configHandler);
        contentFolder = directoryHandler.getFullPath(config.getString(Constants.CONTENT_FOLDER_KEY));
        isComplex = config.getLong(Constants.COMPLEX_PASSPHRASE_KEY) != 0;
    }

    @Override
    void execute() throws Exception {
        passphraseHandler.updatePassphrase(contentFolder, isVisible, isComplex);
    }

    @Override
    void printOutput() {
        System.out.println("Passphrase updated successfully.");
    }
}
