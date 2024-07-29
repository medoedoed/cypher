package subcommands;

import com.moandjiezana.toml.Toml;
import handlers.ConfigHandler;

import java.io.IOException;

public abstract class Subcommand {
    protected Toml getConfig(ConfigHandler configHandler) {
        try {
            return configHandler.getConfig();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        return null;
    }
}
