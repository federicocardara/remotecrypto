package cat.uvic.teknos.m09.remotecrypto.servers;

import cat.uvic.teknos.m09.remotecrypto.connections.ConnectionTerminalServerTerminalClient;
import cat.uvic.teknos.m09.remotecrypto.exceptions.RemoteCryptoTerminalException;

import java.io.*;
import java.net.ServerSocket;

public class TerminalServer {
    private  final int SERVER_SOURCE_PORT;
    public TerminalServer(int SERVER_SOURCE_PORT) {
        this.SERVER_SOURCE_PORT=SERVER_SOURCE_PORT;
    }

    public  void turnOnServer()   {
        try {
            ServerSocket serverSocket= null;
            serverSocket = new ServerSocket(SERVER_SOURCE_PORT);
            while (true) {
                var clientSocket=serverSocket.accept();
                Thread thread=new Thread(new ConnectionTerminalServerTerminalClient(clientSocket)::clientConnection);
                thread.start();
            }
        } catch (IOException e) {
            throw new RemoteCryptoTerminalException("An error occurred while opening the socket or while waiting for a connection from a client",e);
        }
    }
}