package cat.uvic.teknos.m09.remotecrypto;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;

public class Server {
    public static  final int PORT = 50001;
    private static ServerSocket server;
    private static BufferedReader inputStream;
    private static PrintWriter outputStream;
    private static Socket client;
    public static void main(String[] args) throws IOException {
        var server = new ServerSocket(PORT);
        System.out.println("Server listening on port " + server.getLocalPort());
        var client = server.accept();
        var end = true;
        while(end) {
            var inputStream = new BufferedReader(new InputStreamReader(client.getInputStream()));
            var outputStream = new PrintWriter(client.getOutputStream());
            var data = inputStream.readLine();
            if (data.equals(""))
                end = false;
            else {
                var encoder = Base64.getEncoder();
                outputStream.println(encoder.encodeToString(data.getBytes()));
                outputStream.flush();
            }
        }
        client.close();
    }
}