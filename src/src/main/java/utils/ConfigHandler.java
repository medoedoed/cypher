package utils;

import com.moandjiezana.toml.Toml;

import java.io.*;


public class ConfigHandler {

    private static final String configDirectory = System.getProperty("user.home") + "/.config/pwm/";
    private static final String configPath = configDirectory + "config.toml";

    private String standartConfigString = "contentFolder = \"~/.passwords/\"" +
            "";
    private final Toml standardConfig = new Toml().read(standartConfigString);


    public static Toml getConfig() {
        File configFile = new File(configPath);
        if (!configFile.exists()) return null;
        return new Toml().read(configFile);

        // TODO handle params what are empty in config
    }

    public void createStandardConfig() {
        try {
            standartConfigString = getStandardConfig();
            System.out.println(standartConfigString);
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
