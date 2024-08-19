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

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

@Command(name = "init",
        description = "Initialize utility.",
        mixinStandardHelpOptions = true)
public class InitSubcommand extends Subcommand {
    @Option(names = {"-d", "--directory"}, description = "Set directory to init utility.")
    private String directory;

    @Option(names = {"-v", "--visible"}, description = "Show password when you enter it.", defaultValue = "false")
    private Boolean isVisible;

    private final PassphraseHandler passphraseHandler = new PassphraseHandler();
    private final ConfigHandler configHandler = new ConfigHandler();
    private final ConnectionProvider connectionProvider = new ConnectionProvider();
    private final PasswordRepository passwordRepository = new PasswordRepository();
    private final DirectoryHandler directoryHandler = new DirectoryHandler();

    private String contentFolder;
    private boolean isComplex;

    @Override
    void getDataFromConfig() {
        Toml config = getConfig(configHandler);
        contentFolder = directoryHandler.getFullPath(config.getString(Constants.CONTENT_FOLDER_KEY));
        isComplex = config.getLong(Constants.COMPLEX_PASSPHRASE_KEY) != 0;
    }

    @Override
    void execute() throws SQLException, IOException, NoSuchAlgorithmException, ClassNotFoundException {
        passwordRepository.connect(connectionProvider.connect(contentFolder));
        passwordRepository.createPasswordTable();
        passphraseHandler.saveChecksum(contentFolder, isVisible, isComplex);
    }


    @Override
    void printOutput() {
        System.out.println("Passphrase saved successfully.");
    }
}