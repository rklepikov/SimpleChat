package net.rgk.server;

import net.rgk.network.TCPConnection;
import net.rgk.network.TCPConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server implements TCPConnectionListener {
    public static void main(String[] args) {
        new Server();
    }

    private final ArrayList<TCPConnection> listConnection = new ArrayList<>();

    private Server() {
        System.out.println("Server is runing...");
        try (ServerSocket serverSocket = new ServerSocket(8189)){
            while(true){
                try{
                    new TCPConnection(serverSocket.accept(), this);
                } catch (IOException e){
                    System.out.println("TCPConnection exception...");
                }
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection connection) {
        listConnection.add(connection);
        sendAll("Client connect: " + connection.toString());
    }

    @Override
    public synchronized void onRecieveString(TCPConnection connection, String string) {
        sendAll(string);
    }

    @Override
    public synchronized void inDisconnect(TCPConnection connection) {
        listConnection.remove(connection);
        sendAll("Client disconnect " + connection.toString() + " отключился");
    }

    @Override
    public synchronized void onException(TCPConnection connection, Exception e) {

    }

    private void sendAll(String string){
        System.out.println(string);
        listConnection.forEach(n->n.sendMessage(string));
    }
}
