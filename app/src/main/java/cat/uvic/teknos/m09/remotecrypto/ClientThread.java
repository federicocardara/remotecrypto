package cat.uvic.teknos.m09.remotecrypto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Base64;

public class ClientThread implements Runnable{

    private Socket client;

    public ClientThread(Socket client){
        this.client = client;

    }

    @Override
    public void run() {
        System.out.println("Thread: " + Thread.currentThread().getName());

        PrintWriter outputStream = null;
        try {
            outputStream = new PrintWriter(client.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        outputStream.println("Type text to encode in base64 otherwise press enter");
        outputStream.flush();

        BufferedReader inputStream = null;
        try {
            inputStream = new BufferedReader(new InputStreamReader( client.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String data = null;
        try {
            data = inputStream.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var encoder = Base64.getEncoder();

        while (!data.equals("")){
            outputStream.println(encoder.encodeToString(data.getBytes()));
            outputStream.flush();
            outputStream.println("Type text to encode in base64 otherwise press enter");
            outputStream.flush();
            try {
                data = inputStream.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        try {
            client.close();
            Thread.currentThread().stop();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
