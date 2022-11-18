package cat.uvic.teknos.m09.remotecrypto;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;

public class Server {
    private static final int PORT = 50001;


    public static void main(String[] args) throws IOException {

        var server = new ServerSocket(PORT);

        while (true){
            var client = server.accept();

            try
            {

                var inputStream = new BufferedReader(new InputStreamReader(client.getInputStream()));
                var outputStream = new PrintWriter(client.getOutputStream());

                Thread thread = new Client(client, inputStream, outputStream);

                thread.start();

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

    }

}