package me.matamor.test.client;

import me.matamor.test.interfaces.ChatInterface;
import me.matamor.test.packets.PacketManager;
import me.matamor.test.packets.defaults.MessagePacket;
import me.matamor.test.server.RunnableQueue;

public class ClientController extends Thread {

    private final Client client;
    private final RunnableQueue runnableQueue;
    private final PacketManager packetManager;

    private ChatInterface chatInterface;

    public ClientController(Client client, RunnableQueue runnableQueue)  {
        this.client = client;
        this.runnableQueue = runnableQueue;
        this.packetManager = new PacketManager();
    }

    public PacketManager getPacketManager() {
        return this.packetManager;
    }

    public Client getClient() {
        return this.client;
    }

    public RunnableQueue getRunnableQueue() {
        return runnableQueue;
    }

    public ChatInterface getChatInterface() {
        return this.chatInterface;
    }

    public void enableChat() {
        this.chatInterface = new ChatInterface(this.client);
        this.client.setClientChat(this.chatInterface::addMessage);
        this.chatInterface.setVisible(true);
    }

    public void registerHandlers() {
        //Register a packet handler for the Messages
        this.packetManager.registerHandler(MessagePacket.class, (socketHandler, packet) -> {
            MessagePacket messagePacket = (MessagePacket) packet;

            if (this.client.hasClientChat()) {
                this.client.getClientChat().addMessage(messagePacket.getMessage());
            } else {
                System.out.println(messagePacket.getMessage());
            }
        });
    }

    @Override
    public void run() {
        while (!this.client.isClosed()) {
            this.client.run();

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
