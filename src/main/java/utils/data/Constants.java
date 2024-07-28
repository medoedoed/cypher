package utils.data;

public final class Constants {
    private Constants() {}

    public static final String CHECKSUM_FILE_NAME = "checksum";
    public static final String CONTENT_FOLDER_KEY = "contentFolder";
    public static final String COPY_UTILITY_KEY = "copyUtility";
    public static final String CONFIG_DIRECTORY_PATH = System.getProperty("user.home") + "/.config/cypher";
    public static final String CONFIG_FILE_PATH = CONFIG_DIRECTORY_PATH + "/config.toml";
}
