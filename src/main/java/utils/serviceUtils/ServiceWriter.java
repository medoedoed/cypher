package utils.serviceUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ServiceWriter {
    public void writeService(String body, String serviceName, String contentFolder) throws IOException {
        var serviceWriter = new FileWriter(getServiceFile(serviceName, contentFolder));
        serviceWriter.write(body);
        serviceWriter.close();
    }

    private File getServiceFile(String serviceName, String contentFolder) throws IOException {
        var serviceFile = new File(contentFolder + File.separator + serviceName);

        if (!serviceFile.getParentFile().exists() && !serviceFile.getParentFile().mkdirs())
            throw new RuntimeException("Could not create folder " + serviceFile.getParentFile().getAbsolutePath());
        if (!serviceFile.exists() && !serviceFile.createNewFile())
            throw new RuntimeException("Could not create file " + serviceFile.getAbsolutePath());
        return serviceFile;
    }
}
