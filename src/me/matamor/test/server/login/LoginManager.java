package me.matamor.test.server.login;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class LoginManager {

    private final Map<String, String> users = new ConcurrentHashMap<>();

    public LoginManager() {
        register("santi", "1234");
        register("jordi", "1234");
        register("dani", "1234");
    }

    public void register(String username, String password) {
        this.users.put(username, password);
    }

    public boolean isRegistered(String username) {
        return this.users.containsKey(username);
    }

    public boolean checkLogin(String username, String targetPassword) {
        String password = this.users.get(username);
        return Objects.equals(password, targetPassword);
    }
}
