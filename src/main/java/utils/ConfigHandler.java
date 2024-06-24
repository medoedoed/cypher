package utils;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;

import java.io.*;
import java.util.Map;


public class ConfigHandler {

    private static final String configDirectory = System.getProperty("user.home") + "/.config/pwm/";
    private static final String configPath = configDirectory + "config.toml";

    private static final String defaultConfigString = """
            contentFolder = "~/.passwords/"
            """;
    private static final Toml config = new Toml().read(defaultConfigString);

    public static Toml getConfig() {
        File configFile = new File(configPath);
        Toml writtenConfig;
        if (!configFile.exists()) {
            return createDefaultConfig();
        } else {
            writtenConfig = new Toml().read(configFile);
        }

        Map<String, Object> writtenMap = writtenConfig.toMap();

        try (StringReader reader = new StringReader(defaultConfigString)) {
            Toml defaultConfig = new Toml().read(reader);
            defaultConfig.toMap().forEach((key, value) -> {
                if (!writtenMap.containsKey(key)) {
                    writtenMap.put(key, value);
                }
            });
        }
        return new Toml().read(new TomlWriter().write(writtenMap));
    }

    public static Toml createDefaultConfig() {
        try {
            var directory = new File(configDirectory);
            if (!directory.exists())
                if (!directory.mkdir()) throw new FileNotFoundException("Config directory could not be created");

            File file = new File(configPath);
            if (!file.createNewFile()) throw new FileNotFoundException("Config file could not be created");

            FileWriter writer = new FileWriter(file);
            writer.write(defaultConfigString);
            writer.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return config;
    }
}
