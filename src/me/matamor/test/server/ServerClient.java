package me.matamor.test.server;

import me.matamor.test.packets.Packet;
import me.matamor.test.packets.SocketHandler;
import me.matamor.test.utils.Callback;
import me.matamor.test.utils.Pair;

import java.io.IOException;
import java.net.Socket;

public class ServerClient extends SocketHandler {

    private final int clientId;

    private LoginData loginData;

    public ServerClient(int clientId, Socket socket, Callback<Pair<SocketHandler, Packet>> callback) throws IOException {
        super(socket, callback);

        this.clientId = clientId;
    }

    public int getClientId() {
        return this.clientId;
    }

    public void setLoginData(LoginData loginData) {
        this.loginData = loginData;
    }

    public boolean isLogged() {
        return this.loginData != null;
    }

    public LoginData getLoginData() {
        return loginData;
    }
}
