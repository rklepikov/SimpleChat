package net.rgk.server;

import net.rgk.network.TCPConnection;
import net.rgk.network.TCPConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * Класс Server реализует серверную часть сетевого чата.
 * Класс реализует интерфейс TCPConnectionListener
 * @see TCPConnectionListener
 *
 * </body>
 */
public class Server implements TCPConnectionListener {
    public static void main(String[] args) {
        new Server();
    }

    private final ArrayList<TCPConnection> listConnection = new ArrayList<>();

    /**
     * В конструкторе создается объект ServerSocket и ожидает подключения нового клиента.
     * При подключении клиента к сокету создается новое соединение TCPConnection, которому в параметрах передается объект Socket
     * и сам объект класса Server
     * @throws
     */
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

    /**
     * <body>
     *     Метод получает установленное соединение TCPConnection и добавляет его в список.
     *     Далее отправляется сообщение всем пользователям, что подключен новый клиент
     *
     * @param connection содержит новое установленное подключение
     * </body>
     */
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
