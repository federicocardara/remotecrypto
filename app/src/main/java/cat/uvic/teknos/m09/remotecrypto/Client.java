package cat.uvic.teknos.m09.remotecrypto;

import cat.uvic.teknos.m09.remotecrypto.exeptions.IncorrectDataException;

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

                if(data.equals("")) {
                    exit = !exit;
                    socket.close();
                } else{
                    outputStream.println(encoder.encodeToString(data.getBytes()));

                    outputStream.flush();
                }

            } catch (IOException e) {
                throw new IncorrectDataException();
            }
        }

    }
}
