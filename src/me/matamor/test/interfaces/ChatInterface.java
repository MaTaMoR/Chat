package me.matamor.test.interfaces;

import me.matamor.test.client.Client;
import me.matamor.test.packets.defaults.MessagePacket;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ChatInterface {

    private final Client client;

    private final JFrame frame;

    private final JTextArea chat;
    private final JTextField input;
    private final JButton button;

    public ChatInterface(Client client) {
        this.client = client;

        this.frame = new JFrame("Chat");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setResizable(false);

        //This is the panel where everything goes
        JPanel globalPanel = new JPanel(new BorderLayout());

        //This panel is for the chat window
        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        //Create the text area and the scroll, finally add everything to the chat panel
        this.chat = new JTextArea(10, 20);
        this.chat.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(this.chat, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        chatPanel.add(scrollPane, BorderLayout.CENTER);

        //Buttons and input panel
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        //Create the field that will input the messages
        this.input = new JTextField(15);
        this.input.addActionListener(actionEvent -> sendMessage());

        this.button = new JButton("Enviar");
        this.button.addActionListener(actionEvent -> sendMessage());

        inputPanel.add(this.input, BorderLayout.WEST);
        inputPanel.add(this.button, BorderLayout.EAST);

        //Add the chat panel to the global panel
        globalPanel.add(chatPanel, BorderLayout.NORTH);
        globalPanel.add(inputPanel, BorderLayout.SOUTH);

        this.frame.setContentPane(globalPanel);
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
    }

    private void sendMessage() {
        String message = this.input.getText();
        if (message == null || message.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No puedes enviar un mensaje vacio!");
        } else {
            //Reset the message from the chat window and disable both the input and button
            this.input.setText("");
            this.input.setEditable(false);
            this.button.setEnabled(false);

            //Send the message to the server
            try {
                this.client.sendPacket(new MessagePacket(message));

                //Enable again the input and button
                this.input.setEditable(true);
                this.button.setEnabled(true);
            } catch (IOException e) {
                e.printStackTrace();

                JOptionPane.showMessageDialog(null, "No se ha podido enviar este mensaje!");
            }
        }
    }

    public void addMessage(String message) {
        //Add the message to the chat
        this.chat.append(message + "\n");
    }

    public void setVisible(boolean visible) {
        this.frame.setVisible(visible);
    }

    public static void main(String[] args) {
        ChatInterface chatInterface = new ChatInterface(null);
        chatInterface.setVisible(true);
    }
}
