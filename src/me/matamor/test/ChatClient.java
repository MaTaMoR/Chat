package me.matamor.test;

import me.matamor.test.client.Client;
import me.matamor.test.client.ClientController;
import me.matamor.test.interfaces.LoginInterface;
import me.matamor.test.packets.Packet;
import me.matamor.test.packets.SocketHandler;
import me.matamor.test.packets.defaults.LoginPacket;
import me.matamor.test.packets.defaults.LoginResultPacket;
import me.matamor.test.server.RunnableQueue;
import me.matamor.test.utils.Callback;
import me.matamor.test.utils.Pair;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

public class ChatClient {

    public static void main(String[] args) {
        initializeLogin();
    }

    private static void initializeLogin() {
        LoginInterface loginInterface = new LoginInterface((loginInfo, exception) -> {
            try {
                String username = loginInfo.getUsername();
                String password = loginInfo.getPassword();
                String address = loginInfo.getAddress();
                String port = loginInfo.getPort();
                int realPort;

                try {
                    realPort = Integer.parseInt(port);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Puerto invalido! Debe ser un numero.");

                    initializeLogin();
                    return;
                }

                System.out.println("Connecting to: " + address + ":" + realPort);

                RunnableQueue runnableQueue = new RunnableQueue();
                Socket socket = new Socket(address, realPort);

                Callback<Pair<SocketHandler, Packet>> callback = (pair, socketException) -> {
                    Packet packet = pair.getRight();

                    if (packet instanceof LoginResultPacket) {
                        LoginResultPacket resultPacket = (LoginResultPacket) packet;

                        //Successful login!
                        if (resultPacket.getResult()) {
                            JOptionPane.showMessageDialog(null, "Logeado correctamente!");

                            //Create the client
                            Client client = (Client) pair.getLeft();
                            ClientController clientController = new ClientController(client, runnableQueue);

                            //Redirect the new packets to the PacketHandler
                            client.setCallback((value, e) ->
                                    clientController.getPacketManager().executeHandlers(value.getLeft(), value.getRight()));

                            //Register the packet handlers
                            clientController.registerHandlers();

                            //Register the chat
                            clientController.enableChat();

                            //Start listening to new packets!
                            clientController.start();
                        } else {
                            JOptionPane.showMessageDialog(null, "Login incorrecto!");

                            initializeLogin();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Se ha recibido un paquete incorrecto!");

                        System.out.println("Got packet: " + pair.getClass() + " instead of LoginResponse");

                        initializeLogin();
                    }
                };

                //Create the client so we start communicating with it
                Client socketHandler = new Client(socket, callback, username);

                try {
                    socketHandler.sendPacket(new LoginPacket(username, password));

                    try {
                        //Wait a bit for the response
                        Thread.sleep(1000);

                        //Execute the socket handler so we can read the incoming LoginResultPacket
                        runnableQueue.registerQueue(socketHandler);
                        runnableQueue.start();
                    } catch (InterruptedException e) {
                        JOptionPane.showMessageDialog(null, "No se ha podido parar el proceso!");

                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "No se ha podido enviar la información de login!");

                    e.printStackTrace();

                    initializeLogin();
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "No se ha podido establecer la conexion con el servidor!");

                ex.printStackTrace();

                initializeLogin();
            }
        });

        loginInterface.setVisible(true);
    }
}
