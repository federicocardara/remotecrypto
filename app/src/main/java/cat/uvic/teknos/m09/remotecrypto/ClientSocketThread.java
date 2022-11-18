package cat.uvic.teknos.m09.remotecrypto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.Base64;

public class ClientSocketThread implements Runnable {
    private Socket newServerSocketFromClientSocketConnectionToServerSocket;
    private  boolean dataIsEmpty =false;
    public ClientSocketThread(Socket client) {
        this.newServerSocketFromClientSocketConnectionToServerSocket=client;
    }

    @Override
    public void run() {
        try {
            var input = new BufferedReader(new InputStreamReader(newServerSocketFromClientSocketConnectionToServerSocket.getInputStream()));
            var output = new PrintWriter(newServerSocketFromClientSocketConnectionToServerSocket.getOutputStream());

            output.println("This socket returns any given string in base64. To terminate the connection, enter a null string.");
            output.flush();

            output.print("Enter the string that you wish to get the Base64 from:");
            output.flush();
            String data;
            data = input.readLine();
            while (!dataIsEmpty) {
                if(!data.equals("")) {
                    var encoder = Base64.getEncoder();
                    output.println("Base64: "+encoder.encodeToString(data.getBytes()));
                    output.flush();
                    output.print("Enter the string that you wish to get the Base64 from:");
                    output.flush();
                    data = input.readLine();
                }else {
                    dataIsEmpty = !dataIsEmpty;
                }
            }
            output.println("You have terminated the connection.");
            output.flush();
            System.out.println();
            Thread.sleep(10);
            newServerSocketFromClientSocketConnectionToServerSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

