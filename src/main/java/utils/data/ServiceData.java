package utils.data;

public record ServiceData(String serviceName, String login, String password) {
    public String toString() {
        return "[service]:\t" + serviceName + "\nпше [login]:\t" + login + "\n[Password]:\t" + password;
    }
}
