package me.matamor.test.client;

import me.matamor.test.packets.*;
import me.matamor.test.utils.Callback;
import me.matamor.test.utils.Pair;

import java.io.IOException;
import java.net.Socket;

public class Client extends SocketHandler {

    private final String username;

    private ClientChat clientChat;

    public Client(Socket socket, Callback<Pair<SocketHandler, Packet>> callback, String username) throws IOException {
        super(socket, callback);

        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void setClientChat(ClientChat clientChat) {
        this.clientChat = clientChat;
    }

    public ClientChat getClientChat() {
        return this.clientChat;
    }

    public boolean hasClientChat() {
        return this.clientChat != null;
    }
}
