package cat.uvic.teknos.m09.remotecrypto;

import cat.uvic.teknos.m09.remotecrypto.exeptions.InputOutputStreamException;
import cat.uvic.teknos.m09.remotecrypto.exeptions.ServerSocketException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
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
            Thread thread = new Client(client, inputStream, outputStream);

            thread.start();
        }
    }

}