package cat.uvic.teknos.m09.remotecrypto;

import rawhttp.core.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpTest {

    private static final int PORT = 8083;
    private static RawHttp http = new RawHttp();
    private static BufferedReader inputStream;
    private static PrintWriter outputStream;
    private static ServerSocket serverSoket;
    private static Socket client;

    public static void main(String[] args) {

        try {
            serverSoket = new ServerSocket(PORT);
        } catch (IOException e) {
            throw new RuntimeException();
        }

        while (true){

            try {
                client = serverSoket.accept();

                inputStream = new BufferedReader(new InputStreamReader(client.getInputStream()));
                outputStream = new PrintWriter(client.getOutputStream());
            } catch (IOException e){
                throw new RuntimeException();
            }

            Thread thread = new Thread(new ConnectionTest(client, inputStream, outputStream)::connection);

            thread.start();
        }
    }
}

