package subcommands;

import com.moandjiezana.toml.Toml;
import encryption.symmetricAlgorithms.Aes256Encryptor;
import encryption.symmetricAlgorithms.SymmetricAlgorithm;
import picocli.CommandLine;
import utils.data.Constants;

import java.util.ArrayList;


@CommandLine.Command(name = "list",
        description = "Print list of all services",
        mixinStandardHelpOptions = true)
public class ListSubcommand extends Subcommand implements Runnable {
    private final ConfigHandler configHandler = new ConfigHandler();
    private final ServiceHandler serviceHandler = new ServiceHandler();
    private final DirectoryHandler directoryHandler = new DirectoryHandler();

    @Override
    public void run() {
        Toml config = getConfig(configHandler);
        var contentFolder = directoryHandler.getFullPath(config.getString(Constants.CONTENT_FOLDER_KEY));
        SymmetricAlgorithm algorithm = new Aes256Encryptor();
        ArrayList<String> allServices = execute(contentFolder, algorithm);
        printOutput(allServices);
    }

    private ArrayList<String> execute(String contentFolder, SymmetricAlgorithm algorithm) {
        try {
//            if (!passphraseHandler.checksumExists(contentFolder, isVisible)) return;
        }
    }

    private void printOutput(ArrayList<String> output) {
        for (int i = 0; i < output.size(); i++) {
            System.out.println((i + 1) + ": " + output.get(i));
        }
    }

}
