package me.matamor.test.server;

import me.matamor.test.packets.Packet;
import me.matamor.test.packets.PacketManager;
import me.matamor.test.packets.SocketHandler;
import me.matamor.test.packets.defaults.LoginPacket;
import me.matamor.test.packets.defaults.LoginResultPacket;
import me.matamor.test.packets.defaults.MessagePacket;
import me.matamor.test.server.login.LoginManager;
import me.matamor.test.utils.Callback;
import me.matamor.test.utils.Pair;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ServerController extends Thread {

    private final ServerSocket serverSocket;
    private final ServerClientController serverClientController;
    private final RunnableQueue runnableQueue;
    private final ScheduledThreadPoolExecutor loginExecutor;

    private final LoginManager loginManager;
    private final PacketManager packetManager;

    private int clientsId = 0;

    public ServerController(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.runnableQueue = new RunnableQueue();
        this.serverClientController = new ServerClientController(this.runnableQueue);
        this.loginExecutor = new ScheduledThreadPoolExecutor(3);

        //Create the login manager to manage the accounts
        this.loginManager = new LoginManager();

        //PacketManager will only handle packets from logged clients
        this.packetManager = new PacketManager();

        //Message packet handler, when we get a message from a client, redirect it to everyone!
        this.packetManager.registerHandler(MessagePacket.class, (socketHandler, packet) -> {
            MessagePacket messagePacket = (MessagePacket) packet;
            ServerClient serverClient = (ServerClient) socketHandler;

            String formattedMessage = serverClient.getLoginData().getUsername() + ": " + messagePacket.getMessage();
            System.out.println(formattedMessage); //Console message

            this.serverClientController.broadcast(new MessagePacket(formattedMessage));
        });

        this.serverClientController.start();
        this.runnableQueue.start();
    }

    public ServerSocket getServerSocket() {
        return this.serverSocket;
    }

    public ServerClientController getClientController() {
        return this.serverClientController;
    }

    private void handleLogin(ServerClient serverClient, LoginPacket loginPacket) {
        boolean success = this.loginManager.checkLogin(loginPacket.getUsername(), loginPacket.getPassword());
        String message = (success ? "Correcto" : "Incorrecto");

        System.out.println("Login request from: " + loginPacket.getUsername() + "/" + loginPacket.getPassword());

        try {
            //Create the result packet and send it to the client
            LoginResultPacket resultPacket = new LoginResultPacket(success, message);
            serverClient.sendPacket(resultPacket);

            if (success) {
                //If the packet is successfully sent, redirect the new upcoming messages to the packet manager
                Callback<Pair<SocketHandler, Packet>> callback = (value, e) ->
                        this.packetManager.executeHandlers(value.getLeft(), value.getRight());

                serverClient.setCallback(callback);

                //Register the ServerClient in the ServerClientController so it can keep receiving new packets
                this.serverClientController.register(serverClient);

                //Attach the login data to the client
                serverClient.setLoginData(new LoginData(loginPacket.getUsername(), loginPacket.getPassword()));

                this.serverClientController.broadcast(new MessagePacket(loginPacket.getUsername() + " se ha conectado!"));
            } else {
                //Login not successful, fuck the client lol

                try {
                    serverClient.close();
                } catch (IOException e) {
                    System.out.println("Couldn't close connection after unsuccessful login!");

                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Couldn't send LoginResultPacket to client '" + serverClient.getClientId() + "'");

            e.printStackTrace();

            try {
                serverClient.close();
            } catch (IOException ex) {
                System.out.println("Couldn't close connection!");

                ex.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        while (!this.serverSocket.isClosed()) {
            try {
                Socket socket = this.serverSocket.accept();
                int clientId = this.clientsId++;

                Callback<Pair<SocketHandler, Packet>> callback = (value, exception) -> {
                    Packet packet = value.getRight();

                    if (packet instanceof LoginPacket) {
                        LoginPacket loginPacket = (LoginPacket) packet;

                        handleLogin((ServerClient) value.getLeft(), loginPacket);
                    } else {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };

                ServerClient serverClient = new ServerClient(clientId, socket, callback);
                serverClient.setCallback(callback);

                this.loginExecutor.schedule(() ->
                        this.runnableQueue.registerQueue(serverClient), 100, TimeUnit.MILLISECONDS);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
