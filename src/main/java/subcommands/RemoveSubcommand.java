package subcommands;

import com.moandjiezana.toml.Toml;
import handlers.ConfigHandler;
import handlers.DirectoryHandler;
import handlers.ServiceHandler;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;
import utils.data.Constants;

@Command(name = "remove",
        description = "Remove service.",
        mixinStandardHelpOptions = true)
public class RemoveSubcommand extends Subcommand {
    @Parameters(index = "0", description = "Service name.")
    private String serviceName;

    private final ConfigHandler configHandler = new ConfigHandler();
    private final ServiceHandler serviceHandler = new ServiceHandler();
    private final DirectoryHandler directoryHandler = new DirectoryHandler();

    private String contentFolder;

    @Override
    void getDataFromConfig() {
        Toml config = getConfig(configHandler);
        contentFolder = directoryHandler.getFullPath(config.getString(Constants.CONTENT_FOLDER_KEY));
    }

    @Override
    void execute() throws Exception {
        serviceHandler.removeService(serviceName, contentFolder);

    }

    @Override
    void printOutput() {
        System.out.println("Service " + serviceName + " removed");
    }
}
