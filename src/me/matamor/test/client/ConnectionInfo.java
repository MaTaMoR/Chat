package me.matamor.test.client;

public class ConnectionInfo {

    private final String username;
    private final String password;

    private final String address;
    private final String port;

    public ConnectionInfo(String username, String password, String address, String port) {
        this.username = username;
        this.password = password;
        this.address = address;
        this.port = port;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getAddress() {
        return this.address;
    }

    public String getPort() {
        return this.port;
    }
}
