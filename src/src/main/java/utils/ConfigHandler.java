package utils;

import com.moandjiezana.toml.Toml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigHandler {

    private final String configDirectory = System.getProperty("user.home") + "/.config/pwm/";
    private final String configPath = configDirectory + "config.toml";


    private final String standartConfigString =
            "contentFolder = \"~/.passwords/\"\n";
    private final Toml standardConfig = new Toml().read(standartConfigString);

    public Toml readConfig() {
        File configFile = new File(configPath);
        if (!configFile.exists()) return null;
        return new Toml().read(configFile);

        // TODO handle params what are empty in config
    }

    public void createStandardConfig() {
        try {
            var directory = new File(configDirectory);
            if (!directory.exists())
                if (!directory.mkdir())
                    throw new FileNotFoundException("Config directory could not be created");

            File file = new File(configPath);
            if (!file.createNewFile()) return;

            FileWriter writer = new FileWriter(file);
            writer.write(standartConfigString);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
