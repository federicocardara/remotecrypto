package cat.uvic.teknos.m09.remotecrypto;

import java.io.*;
import java.net.ServerSocket;
import java.util.Base64;

public class  Server {
    private static final int PORT = 50001;
    private int counterUser = 0;

    public static void main(String[] args) throws IOException {

        var server = new ServerSocket(PORT);
        var client = server.accept();

        var inputStream = new BufferedReader(new InputStreamReader(client.getInputStream()));
        var outputStream = new PrintWriter(client.getOutputStream());

        var data = "";

        var encoder = Base64.getEncoder();

        var exit = true;
        while(exit) { // while for enter text multiple time

            data = inputStream.readLine(); // data from client

            if(data.equals(""))
                exit = !exit;
            else{
                outputStream.println(encoder.encodeToString(data.getBytes()));

                outputStream.flush();
            }
        }
        client.close();


    }
}
