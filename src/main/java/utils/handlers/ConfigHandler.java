package utils.handlers;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;

import java.io.*;
import java.util.Map;


public class ConfigHandler {
    private static final String configDirectory = System.getProperty("user.home") + "/.config/pwm/";
    private static final String configPath = configDirectory + "config.toml";

    private static final String defaultConfigString =
            """
                    contentFolder = "~/.passwords/"
                    copyUtility = "wl-copy"
                    """;
    private static final Toml config = null;

    private static Toml completeConfig(Toml currentConfig) {
        Map<String, Object> currentConfigMap = currentConfig.toMap();

        try (StringReader reader = new StringReader(defaultConfigString)) {
            Toml defaultConfig = new Toml().read(reader);
            defaultConfig.toMap().forEach((key, value) -> {
                if (!currentConfigMap.containsKey(key)) {
                    currentConfigMap.put(key, value);
                }
            });
        }
        return new Toml().read(new TomlWriter().write(currentConfigMap));
    }

    public static Toml getConfig() throws IOException {
        File configFile = new File(configPath);
        if (!configFile.exists()) return createDefaultConfig();

        return completeConfig(new Toml().read(configFile));
    }

    public static Toml createDefaultConfig() throws IOException {
        var directory = new File(configDirectory);
        if (!directory.exists())
            if (!directory.mkdir()) throw new FileNotFoundException("Config directory could not be created");

        File file = new File(configPath);
        if (!file.createNewFile()) throw new FileNotFoundException("Config file could not be created");

        FileWriter writer = new FileWriter(file);
        writer.write(defaultConfigString);
        writer.close();

        return config;
    }
}
