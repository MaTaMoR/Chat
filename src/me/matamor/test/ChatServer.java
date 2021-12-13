package me.matamor.test;

import me.matamor.test.packets.defaults.MessagePacket;
import me.matamor.test.server.ServerController;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Scanner;

public class ChatServer {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(503, 50, InetAddress.getByName("192.168.20.93"));
            System.out.printf("Server waiting for clients on '%s:%d'\n",
                    serverSocket.getInetAddress().getHostAddress(), serverSocket.getLocalPort());

            ServerController serverController = new ServerController(serverSocket);
            serverController.start();

            Scanner scanner = new Scanner(System.in);

            while (scanner.hasNextLine()) {
                String message = scanner.nextLine();
                if (message != null && !message.trim().isEmpty()) {
                    serverController.getClientController().broadcast(new MessagePacket("Console: " + message));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
