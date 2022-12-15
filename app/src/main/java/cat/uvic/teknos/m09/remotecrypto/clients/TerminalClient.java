package cat.uvic.teknos.m09.remotecrypto.clients;

import cat.uvic.teknos.m09.remotecrypto.exceptions.IncorrectDataException;
import java.io.*;
import java.net.Socket;
import java.util.Base64;

public class TerminalClient {

    private Socket socket;
    private BufferedReader inputStream;
    private PrintWriter outputStream;
    private final Base64.Encoder encoder = Base64.getEncoder();

    public TerminalClient(Socket socket, BufferedReader inputStream, PrintWriter outputStream) {
        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }
    public void clientConecction(){
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
