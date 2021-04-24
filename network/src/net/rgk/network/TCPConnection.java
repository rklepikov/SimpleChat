package net.rgk.network;

import java.io.*;
import java.net.Socket;

public class TCPConnection {
    private final Socket socket;
    private Thread xThread =null;
    private final TCPConnectionListener listener;
    private BufferedReader in = null;
    private BufferedWriter out = null;

    public TCPConnection(TCPConnectionListener listener, String ipAddress, int port)throws IOException{
        this(new Socket(ipAddress, port), listener);
    }

    public TCPConnection(Socket socket, TCPConnectionListener listener) {
        this.listener = listener;
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            xThread = new Thread(() -> {
                try {
                    listener.onConnectionReady(TCPConnection.this);
                    while(!xThread.isInterrupted()) {
                        listener.onRecieveString(TCPConnection.this, in.readLine());
                    }
                } catch (IOException e) {
                    listener.onException(TCPConnection.this, e);
                } finally {
                    listener.inDisconnect(TCPConnection.this);
                }
            });
            xThread.start();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public synchronized void sendMessage(String msg){
        try {
            out.write(msg + "\r\n");
            out.flush();
        } catch (IOException e) {
            listener.onException(TCPConnection.this, e);
            disconnect();
        }
    }

    public synchronized void disconnect() {
        xThread.interrupt();
        try {
            socket.close();
        } catch(IOException ex){
            listener.onException(TCPConnection.this, ex);
        }
    }

    @Override
    public String toString(){
        return "TCPConnection" + socket.getInetAddress() + " : " + socket.getPort();
    }
}
