package me.matamor.test.interfaces;

import me.matamor.test.client.ConnectionInfo;
import me.matamor.test.utils.Callback;
import me.matamor.test.utils.Pair;

import javax.swing.*;
import java.awt.*;

import java.lang.reflect.InvocationTargetException;

public class LoginInterface {

    private final JFrame frame;

    private final JTextField username;
    private final JPasswordField password;

    private final JTextField address;
    private final JTextField port;

    private JButton button;

    private final Callback<ConnectionInfo> callback;

    public LoginInterface(Callback<ConnectionInfo> callback) {
        this.callback = callback;

        this.frame = new JFrame("Login");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setResizable(false);

        //This is the panel where everything goes
        JPanel globalPanel = new JPanel(new BorderLayout());

        //This panel is for the chat window
        JPanel loginPanel = new JPanel(new BorderLayout());
        loginPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel loginPanelTop = new JPanel(new BorderLayout());
        JPanel loginPanelBottom = new JPanel(new BorderLayout());

        JLabel usernameText = new JLabel("Username");
        JLabel passwordText = new JLabel("Password");

        loginPanelTop.add(usernameText, BorderLayout.WEST);
        loginPanelTop.add(passwordText, BorderLayout.EAST);

        //Create the text area and the scroll, finally add everything to the chat panel
        this.username = new JTextField(10);
        this.password = new JPasswordField(10);

        loginPanelBottom.add(this.username, BorderLayout.WEST);
        loginPanelBottom.add(this.password, BorderLayout.EAST);

        loginPanel.add(loginPanelTop, BorderLayout.NORTH);
        loginPanel.add(loginPanelBottom, BorderLayout.SOUTH);

        //Create the connection panel

        JPanel connectionPanel = new JPanel(new BorderLayout());
        connectionPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel connectionTopPanel = new JPanel(new BorderLayout());
        JPanel connectionBottomPanel = new JPanel(new BorderLayout());

        JLabel addressText = new JLabel("Address", JLabel.CENTER);
        JLabel portText = new JLabel("Port", JLabel.CENTER);

        connectionTopPanel.add(addressText, BorderLayout.WEST);
        connectionTopPanel.add(portText, BorderLayout.EAST);

        this.address = new JTextField(10);
        this.port = new JTextField(10);

        connectionBottomPanel.add(this.address, BorderLayout.WEST);
        connectionBottomPanel.add(this.port, BorderLayout.EAST);

        connectionPanel.add(connectionTopPanel, BorderLayout.NORTH);
        connectionPanel.add(connectionBottomPanel, BorderLayout.SOUTH);

        //Create the button

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        this.button = new JButton("Login");

        this.button.addActionListener(actionEvent -> {
            this.username.setEditable(false);
            this.password.setEditable(false);

            String username = this.username.getText();
            String password = new String(this.password.getPassword());

            if ((username == null || username.isEmpty()) || (password.isEmpty())) {
                JOptionPane.showMessageDialog(null, "Escribe un nombre de usuario y contraseña!");

                this.address.setEditable(true);
                this.port.setEditable(true);
            } else {
                this.address.setEditable(false);
                this.port.setEditable(false);

                String address = this.address.getText();
                String port = this.port.getText();

                if ((address == null || address.isEmpty()) || (port == null || port.isEmpty())) {
                    JOptionPane.showMessageDialog(null, "Escribe una direccion y puerto!");

                    this.address.setEditable(true);
                    this.port.setEditable(true);
                } else {
                    this.frame.setVisible(false);
                    this.frame.dispose();

                    this.callback.callback(new ConnectionInfo(username, password, address, port), null);
                }
            }
        });

        buttonPanel.add(this.button, BorderLayout.CENTER);

        //Add the chat panel to the global panel
        globalPanel.add(loginPanel, BorderLayout.NORTH);
        globalPanel.add(connectionPanel, BorderLayout.CENTER);
        globalPanel.add(buttonPanel, BorderLayout.SOUTH);
        //globalPanel.add(buttonPanel, BorderLayout.SOUTH);

        this.frame.setContentPane(globalPanel);
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
    }

    public void setVisible(boolean visible) {
        this.frame.setVisible(visible);
    }

    public static void main(String[] args) {
        try {
            SwingUtilities.invokeAndWait(() -> {
                LoginInterface loginInterface = new LoginInterface(null);
                loginInterface.setVisible(true);
            });
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
