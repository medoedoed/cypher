package subcommands;

import com.moandjiezana.toml.Toml;
import handlers.ConfigHandler;
import handlers.DirectoryHandler;
import handlers.ServiceHandler;
import picocli.CommandLine.Command;
import utils.data.Constants;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

@Command(name = "list",
        description = "Print list of all services",
        mixinStandardHelpOptions = true)
public class ListSubcommand extends Subcommand {
    private final ConfigHandler configHandler = new ConfigHandler();
    private final ServiceHandler serviceHandler = new ServiceHandler();
    private final DirectoryHandler directoryHandler = new DirectoryHandler();

    private String contentFolder;
    private ArrayList<String> serviceNames;

    @Override
    void getDataFromConfig() {
        Toml config = getConfig(configHandler);
        contentFolder = directoryHandler.getFullPath(config.getString(Constants.CONTENT_FOLDER_KEY));
    }

    @Override
    void execute() throws IOException, NoSuchAlgorithmException, ClassNotFoundException, SQLException {
        serviceNames = serviceHandler.getAllServices(contentFolder);
    }

    @Override
    void printOutput() {
        if (serviceNames == null) System.out.println("[ERROR]: No services found");
        for (int i = 0; i < serviceNames.size(); i++) {
            System.out.println((i + 1) + ": " + serviceNames.get(i));
        }
    }
}