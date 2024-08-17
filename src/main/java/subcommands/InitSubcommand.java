package subcommands;

import com.moandjiezana.toml.Toml;
import dataAccess.ConnectionProvider;
import dataAccess.PasswordRepository;
import handlers.ConfigHandler;
import handlers.DirectoryHandler;
import handlers.PassphraseHandler;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import utils.data.Constants;

@Command(name = "init",
        description = "Initialize utility.",
        mixinStandardHelpOptions = true)
public class InitSubcommand extends Subcommand implements Runnable {
    private final DirectoryHandler directoryHandler = new DirectoryHandler();
    @Option(names = {"-d", "--directory"}, description = "Set directory to init utility.")
    private String directory;

    @Option(names = {"-v", "--visible"}, description = "Show password when you enter it.", defaultValue = "false")
    private Boolean isVisible;

    private final PassphraseHandler passphraseHandler = new PassphraseHandler();
    private final ConfigHandler configHandler = new ConfigHandler();
    private final ConnectionProvider connectionProvider = new ConnectionProvider();
    private final PasswordRepository passwordRepository = new PasswordRepository();

    @Override
    public void run() {
        Toml config = getConfig(configHandler);
        var contentFolder = directoryHandler.getFullPath(config.getString(Constants.CONTENT_FOLDER_KEY));
        boolean isComplex = config.getLong(Constants.COMPLEX_PASSPHRASE_KEY) != 0;
        execute(contentFolder, isComplex);
    }

    private void execute(String contentFolder, boolean isComplex) {
        try {
            passwordRepository.connect(connectionProvider.connect(contentFolder));
            passwordRepository.createPasswordTable();

            passphraseHandler.saveChecksum(contentFolder, isVisible, isComplex);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}