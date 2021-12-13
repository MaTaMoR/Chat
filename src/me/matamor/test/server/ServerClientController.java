package me.matamor.test.server;

import me.matamor.test.packets.Packet;
import me.matamor.test.packets.defaults.DisconnectPacket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerClientController extends Thread {

    private final Map<Integer, ServerClient> clients = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(3);
    private final AtomicBoolean enabled = new AtomicBoolean(true);

    private final RunnableQueue runnableQueue;

    public ServerClientController(RunnableQueue runnableQueue) {
        this.runnableQueue = runnableQueue;
    }

    public void register(ServerClient client) {
        this.clients.put(client.getClientId(), client);
    }

    public void disconnect(int clientId) {
        disconnect(clientId, "Disconnected");
    }

    public void disconnect(int clientId, String message) {
        ServerClient client = this.clients.remove(clientId);

        if (client != null) {
            //If there connection is already closed there isn't much else left to do
            if (!client.isClosed()) {
                try {
                    //If the client is logged send him a disconnect message, if not logged, well fuck him
                    if (client.isLogged()) {
                        client.sendPacket(new DisconnectPacket(message));
                    }

                    //Close the connection
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void broadcast(Packet packet) {
        for (Map.Entry<Integer, ServerClient> entry : this.clients.entrySet()) {
            ServerClient serverClient = entry.getValue();

            //If the packet fails to be sent, disconnect the client
            try {
                serverClient.sendPacket(packet);
            } catch (IOException e) {
                e.printStackTrace();

                disconnect(serverClient.getClientId(), "Failed to send packet!");
            }
        }
    }

    public void stopTicking() {
        this.enabled.set(false);
    }

    @Override
    public void run() {
        while (this.enabled.get()) {
            for (ServerClient client : this.clients.values()) {
                this.runnableQueue.registerQueue(client);
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
