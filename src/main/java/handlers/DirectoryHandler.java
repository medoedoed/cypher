package handlers;

import java.io.File;
import java.io.IOException;

public class DirectoryHandler {
    public  String getFullPath(String path) {
        if (path == null ) return null;

        if (path.isEmpty()) return System.getProperty("user.dir");

        path = path.replaceFirst("^~", System.getProperty("user.home"));
        File file = new File(path);
        try {
            return file.getCanonicalPath().replace("/./", "/");
        } catch (IOException e) {
            return null;
        }
    }
}
