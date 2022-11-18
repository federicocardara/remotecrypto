package cat.uvic.teknos.m09.remotecrypto;

import java.io.*;
import java.net.Socket;
import java.util.Base64;

public class Client extends Thread {

    private Socket socket;
    private BufferedReader inputStream;
    private PrintWriter outputStream;
    private static final Base64.Encoder encoder = Base64.getEncoder();

    public Client(Socket socket, BufferedReader inputStream, PrintWriter outputStream) {
        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    @Override
    public void run(){
        var exit = true;

        while(exit) { // while for enter text multiples times

            String data;
            try {
                data = inputStream.readLine(); // get data from client

                System.out.println(data);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if(data.equals("")) {
                exit = !exit;
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else{
                outputStream.println(encoder.encodeToString(data.getBytes()));

                outputStream.flush();
            }
        }
    }
}
