package handlers;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import utils.data.Constants;

import java.io.*;
import java.nio.file.Files;
import java.util.Map;


public class ConfigHandler {
    private final String defaultConfigString =
            Constants.CONTENT_FOLDER_KEY + " = \"~/.cypher\"" + "\n" +
                    Constants.COPY_UTILITY_KEY + " = \"wl-copy\"" + "\n" +
                    Constants.COMPLEX_PASSPHRASE_KEY + " = 1" + "\n";

    private Toml completeConfig(Toml currentConfig) {
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

    public Toml getConfig() throws IOException {
        var configFile = new File(Constants.CONFIG_FILE_PATH);
        if (!configFile.exists()) return createDefaultConfig();

        StringBuilder tomlBuilder = new StringBuilder();
        Files.readAllLines(configFile.toPath()).forEach(line -> {
            if (line.isEmpty()) return;
            tomlBuilder.append(line).append("\n");
        });
        return completeConfig(new Toml().read(tomlBuilder.toString()));

    }

    public Toml createDefaultConfig() throws IOException {
        var directory = new File(Constants.CONFIG_DIRECTORY_PATH);
        if (!directory.exists())
            if (!directory.mkdir()) throw new FileNotFoundException("Config directory could not be created");

        File configFile = new File(Constants.CONFIG_FILE_PATH);
        if (!configFile.createNewFile()) throw new FileNotFoundException("Config file could not be created");

        FileWriter writer = new FileWriter(configFile);
        writer.write(defaultConfigString);
        writer.close();

        return new Toml().read(defaultConfigString);
    }
}
