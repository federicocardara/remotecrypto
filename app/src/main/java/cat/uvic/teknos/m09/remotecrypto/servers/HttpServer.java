package cat.uvic.teknos.m09.remotecrypto.servers;

import cat.uvic.teknos.m09.remotecrypto.connections.ConnectionHttpServerHttpClient;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    private  final int PORT;
    private  ServerSocket serverSoket;
    private  Socket client;

    public HttpServer(int PORT) {
        this.PORT=PORT;
    }

    public void turnOnServer() {

        try {
            serverSoket = new ServerSocket(PORT);
            while (true){

                client = serverSoket.accept();

                Thread thread = new Thread(new ConnectionHttpServerHttpClient(client)::processRequestResponse);

                thread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}

