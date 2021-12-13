package me.matamor.test.server;

import me.matamor.test.packets.defaults.MessagePacket;

import java.util.Scanner;

public class ConsoleController extends Thread {

    private final ServerClientController serverClientController;

    public ConsoleController(ServerClientController serverClientController) {
        this.serverClientController = serverClientController;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String message = scanner.nextLine();
            if (message != null && !message.trim().isEmpty()) {
                this.serverClientController.broadcast(new MessagePacket("Servidor: " + message));
            }
        }
    }
}
