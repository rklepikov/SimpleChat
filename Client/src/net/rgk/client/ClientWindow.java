package net.rgk.client;

import net.rgk.network.TCPConnection;
import net.rgk.network.TCPConnectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientWindow extends JFrame implements ActionListener, TCPConnectionListener {
    private static final String IP_ADDRESS = "localhost";
    private static final int PORT = 8189;
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 1000;
    private TCPConnection connection;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientWindow());
    }

    private final JTextArea log = new JTextArea();
    private final JTextField fieldNick = new JTextField("Roman");
    private final JTextField fieldInput = new JTextField();

    private ClientWindow(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);

        log.setEditable(false);
        log.setLineWrap(true);
        add(log, BorderLayout.CENTER);
        fieldInput.setSize(100, 200);

        fieldInput.addActionListener(this);
        add(fieldInput, BorderLayout.SOUTH);
        add(fieldNick, BorderLayout.WEST);

        setVisible(true);
        try {
            connection = new TCPConnection(this, IP_ADDRESS, PORT);
        } catch (IOException e) {
            printMessage("Connection exception " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = fieldInput.getText();
        if(msg.equals("")) return;
        fieldInput.setText(null);
        connection.sendMessage(fieldNick.getText() + " : " + msg);
    }

    @Override
    public void onConnectionReady(TCPConnection connection) {
        printMessage("Connection ready...");
    }

    @Override
    public void onRecieveString(TCPConnection connection, String string) {
        printMessage(string);
    }

    @Override
    public void inDisconnect(TCPConnection connection) {
        printMessage("Connection close...");
    }

    @Override
    public void onException(TCPConnection connection, Exception e) {
        printMessage("Connection exception " + e.getMessage());
    }

    private synchronized void printMessage(String msg){
        SwingUtilities.invokeLater(() -> {
            log.append(msg + "\n");
            log.setCaretPosition(log.getDocument().getLength());
        });
    }
}
