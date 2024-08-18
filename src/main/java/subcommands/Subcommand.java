package subcommands;

import com.moandjiezana.toml.Toml;
import handlers.ConfigHandler;

import java.io.IOException;

public abstract class Subcommand implements Runnable {
    protected Toml getConfig(ConfigHandler configHandler) {
        try {
            return configHandler.getConfig();
        } catch (IOException e) {
            System.err.println("[ERROR]: Wrong configuration file");
            System.exit(1);
        }

        return null;
    }

    abstract void getDataFromConfig();
    abstract void execute() throws Exception;
    abstract void printOutput();

    @Override
    public void run() {
        try {
            getDataFromConfig();
        } catch (Exception e) {
            System.err.println("[ERROR]: Wrong configuration file");
            return;
        }

        try {
            execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        printOutput();
    }
}
