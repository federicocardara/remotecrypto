package cat.uvic.teknos.m09.remotecrypto;

import java.io.*;
import java.net.Socket;

public class Client {
    public static final int SERVER_PORT = 50001;

    public static void main(String[] args) throws IOException {
        Socket client =new Socket("localhost", SERVER_PORT);

        var inputStream = new BufferedReader(new InputStreamReader(client.getInputStream()));
        var outputStream = new PrintWriter(client.getOutputStream());

        outputStream.println("test");
        outputStream.println("test");

        outputStream.flush();

        var hash = inputStream.readLine();

        System.out.println("Hash: " + hash);

    }
}
