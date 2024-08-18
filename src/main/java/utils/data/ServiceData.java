package utils.data;

public record ServiceData(String login, String password) {
    public String toString() {
        return "[login]:\t" + login + "\n[Password]:\t" + password;
    }
}
