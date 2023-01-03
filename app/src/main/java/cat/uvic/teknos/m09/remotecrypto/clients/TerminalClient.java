package cat.uvic.teknos.m09.remotecrypto.clients;

import java.io.*;
import java.net.Socket;

public class TerminalClient {
    public static final int SERVER_PORT= 50001;

    public static void main(String[] args) throws IOException {

            var client=new Socket("localhost",SERVER_PORT);
            var inputStream=new BufferedReader((new InputStreamReader(client.getInputStream())));
            var outputStream=new PrintWriter(client.getOutputStream());

            outputStream.println("aaaa");
            outputStream.flush();
            inputStream.readLine();
            var hash=inputStream.readLine();
            System.out.println(hash);

            outputStream.println("bbbb");
            outputStream.flush();
            inputStream.readLine();
            hash=inputStream.readLine();
            System.out.println(hash);

            outputStream.println("");
            outputStream.flush();
            inputStream.readLine();
            hash=inputStream.readLine();
            System.out.println(hash);
            client.close();
    }
}