package net.rgk.network;

public interface TCPConnectionListener {
    void onConnectionReady(TCPConnection connection);
    void onRecieveString(TCPConnection connection, String string);
    void inDisconnect(TCPConnection connection);
    void onException(TCPConnection connection, Exception e);
}
