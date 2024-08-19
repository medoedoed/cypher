package utils.serviceUtils;

import encryption.symmetricAlgorithms.SymmetricAlgorithm;
import utils.data.ServiceData;

import java.io.*;

public class ServiceDecoder {
    private ServiceData getDataFromFile(File serviceFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(serviceFile));
        var encodedLogin = reader.readLine();
        var encodedPassword = reader.readLine();
        return new ServiceData(encodedLogin, encodedPassword);
    }

    public ServiceData getServiceData(File serviceFile, SymmetricAlgorithm algorithm, String key) throws IOException {
        var encodedData = getDataFromFile(serviceFile);
        var decodedLogin = algorithm.decrypt(encodedData.login(), key);
        var decodedPassword = algorithm.decrypt(encodedData.password(), key);

        return new ServiceData(decodedLogin, decodedPassword);
    }
}
