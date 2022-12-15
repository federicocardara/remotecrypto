package cat.uvic.teknos.m09.remotecrypto;

import cat.uvic.teknos.m09.remotecrypto.exceptions.incorrectDataException;
import java.io.*;
import java.net.Socket;
import java.util.Base64;

public class Client {

    private Socket socket;
    private BufferedReader inputStream;
    private PrintWriter outputStream;
    private final Base64.Encoder encoder = Base64.getEncoder();

    public Client(Socket socket, BufferedReader inputStream, PrintWriter outputStream) {
        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }
    public void clientConecction(){
        var exit = true;

        while(exit) { // while per enter per multiples textos

            String data;
            try {
                data = inputStream.readLine(); // conseguint datos del client

                if(data.equals("")) {
                    exit = !exit;
                    socket.close();
                } else{
                    outputStream.println(encoder.encodeToString(data.getBytes()));

                    outputStream.flush();
                }

            } catch (IOException e) {
                throw new incorrectDataException();
            }
        }

    }
}