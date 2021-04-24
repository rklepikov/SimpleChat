package net.rgk.server;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

public class ClientData extends JFrame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientData());
    }

    private ClientData(){
        setSize(1800, 1200);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JButton button1 = new JButton("Alfa");
        JButton button2 = new JButton("Beta");
        button1.setSize(100, 200);
        button1.setBorder(BorderFactory.createLineBorder(Color.RED, 5));
        button1.addActionListener(n -> button1.setText("Her"));
        button2.addActionListener(n -> button2.setText("Pizda"));

        add(button1);
        add(button2);

        setVisible(true);
    }
}
