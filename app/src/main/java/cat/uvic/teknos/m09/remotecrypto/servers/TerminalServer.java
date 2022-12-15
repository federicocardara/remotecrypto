package cat.uvic.teknos.m09.remotecrypto.servers;

import cat.uvic.teknos.m09.remotecrypto.clients.TerminalClient;
import cat.uvic.teknos.m09.remotecrypto.exceptions.InputOutputStreamException;
import cat.uvic.teknos.m09.remotecrypto.exceptions.ServerSocketException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TerminalServer {
    private static final int PORT = 50001;
    private static ServerSocket server;
    private static BufferedReader inputStream;
    private static PrintWriter outputStream;
    private static Socket client;

    public static void main(String[] args){

        try {
            server = new ServerSocket(PORT);
        } catch (IOException e) {
            throw new ServerSocketException();
        }

        while (true){
            try {
                client = server.accept();

                inputStream = new BufferedReader(new InputStreamReader(client.getInputStream()));
                outputStream = new PrintWriter(client.getOutputStream());
            } catch (IOException e){
                throw new InputOutputStreamException();
            }
            Thread thread = new Thread(new TerminalClient(client, inputStream, outputStream)::clientConecction);

            thread.start();
        }
    }

}