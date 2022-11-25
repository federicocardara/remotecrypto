package cat.uvic.teknos.m09.remotecrypto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Base64;

public class ServerTelnetThread implements Runnable{

    private Socket client;

    public ServerTelnetThread(Socket client){ //this Thread receives the client Socket
        this.client = client;

    }

    @Override
    public void run() {
        PrintWriter outputStream = null;
        try {
            outputStream = new PrintWriter(client.getOutputStream()); // points to the client output
        } catch (IOException e) {
            throw new RuntimeException("Output stream null",e);
        }

        outputStream.println("Type text to encode in base64 otherwise press enter");
        outputStream.flush();

        BufferedReader inputStream = null;
        try {
            inputStream = new BufferedReader(new InputStreamReader( client.getInputStream())); // points to the client input
        } catch (IOException e) {
            throw new RuntimeException("Null input",e);
        }

        String data = null;
        try {
            data = inputStream.readLine(); // input reading
        } catch (IOException e) {
            throw new RuntimeException("Nothing to read",e);
        }

        var encoder = Base64.getEncoder();

        while (!data.equals("")){ //loop till client press enter key it repeats the process of reading and showing the text in base 64

            outputStream.println(encoder.encodeToString(data.getBytes()));
            outputStream.flush();
            outputStream.println("Type text to encode in base64 otherwise press enter");
            outputStream.flush();
            try {
                data = inputStream.readLine();
            } catch (IOException e) {
                throw new RuntimeException("Nothing to read",e);
            }
        }
        try {
            client.close(); //Client session ends
            Thread.currentThread().stop(); //Thread dies aswell
        } catch (IOException e) {
            throw new RuntimeException("Null client", e);
        }
    }


}
