package subcommands;

import com.moandjiezana.toml.Toml;
import handlers.ConfigHandler;
import handlers.DirectoryHandler;
import handlers.ServiceHandler;
import picocli.CommandLine;
import utils.data.Constants;

@CommandLine.Command(name = "remove",
        description = "Remove service.",
        mixinStandardHelpOptions = true)
public class RemoveSubcommand extends Subcommand implements Runnable {
    @CommandLine.Parameters(index = "0", description = "Service name.")
    private String serviceName;

    private final ConfigHandler configHandler = new ConfigHandler();
    private final ServiceHandler serviceHandler = new ServiceHandler();
    private final DirectoryHandler directoryHandler = new DirectoryHandler();

    @Override
    public void run() {
        Toml config = getConfig(configHandler);
        var contentFolder = directoryHandler.getFullPath(config.getString(Constants.CONTENT_FOLDER_KEY));
        execute(serviceName, contentFolder);
        printOutput();
    }

    private void execute(String serviceName, String contentFolder) {
        try {
//            if (!passphraseHandler.checksumExists(contentFolder, isVisible)) return;
            serviceHandler.removeService(serviceName, contentFolder);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void printOutput() {
        System.out.println("Service " + serviceName + " removed");
    }
}
