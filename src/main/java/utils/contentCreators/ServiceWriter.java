package utils.contentCreators;

import org.passay.PasswordData;
import utils.data.ServiceData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class ServiceWriter {
    public void writeService(String body, String serviceName, String contentFolder) throws IOException {
        var serviceWriter = new FileWriter(getServiceFile(serviceName, contentFolder));

        serviceWriter.write(body);

    }

     private File getServiceFile(String serviceName, String contentFolder) throws IOException {
        var serviceFile = new File(contentFolder + File.separator + serviceName);

        if (!serviceFile.getParentFile().exists()) if (!serviceFile.getParentFile().mkdirs())
            throw new RuntimeException("Could not create folder " + serviceFile.getParentFile().getAbsolutePath());

        if (!serviceFile.createNewFile())
            throw new RuntimeException("Could not create file " + serviceFile.getAbsolutePath());
        return serviceFile;
    }
}
