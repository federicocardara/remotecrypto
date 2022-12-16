package cat.uvic.teknos.m09.remotecrypto.ConnectionsTelnet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Base64;

public class Client implements Runnable{

    private final Socket client;

    public Client(Socket client){
        this.client=client;
    }
    @Override
    public void run() {

        PrintWriter outputStream;
        BufferedReader inputStream;

        try {
            outputStream=new PrintWriter(client.getOutputStream());
            inputStream = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        outputStream.println("Escribe un texto para encriptarlo i pulsa enter:");
        outputStream.flush();

        String data;
        try {
            data=inputStream.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var encoder = Base64.getEncoder();

        while(!data.equals("")){

            outputStream.println(encoder.encodeToString(data.getBytes()));
            outputStream.flush();
            outputStream.println("Escribe un texto para encriptarlo i pulsa enter:");
            outputStream.flush();
            try {
                data=inputStream.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            client.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
