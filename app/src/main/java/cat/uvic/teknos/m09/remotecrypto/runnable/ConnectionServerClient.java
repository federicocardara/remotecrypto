package cat.uvic.teknos.m09.remotecrypto.runnable;

import cat.uvic.teknos.m09.remotecrypto.exceptions.RemoteCryptoException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Base64;

public class ConnectionServerClient implements Runnable {
    private Socket clientSocket;
    private  boolean dataIsEmpty =false;
    
    public ConnectionServerClient(Socket clientSocket) {
        this.clientSocket =clientSocket;
    }

    @Override
    public void run() {
        try {
            var input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            var output = new PrintWriter(clientSocket.getOutputStream());

            output.println("This socket returns any given string in base64. To terminate the connection, enter an empty string.");
            output.flush();

            output.print("Enter the string that you wish to get the Base64 from: ");
            output.flush();
            String data;
            data = input.readLine();
            while (!dataIsEmpty) {
                if(!data.equals("")) {
                    var encoder = Base64.getEncoder();
                    output.println("Base64: "+encoder.encodeToString(data.getBytes()));
                    output.flush();
                    output.print("Enter the string that you wish to get the Base64 from: ");
                    output.flush();
                    data = input.readLine();
                }else {
                    dataIsEmpty = !dataIsEmpty;
                }
            }
            output.println("You have terminated the connection.");
            output.flush();
            Thread.sleep(10); //DO NOT DELETE
            clientSocket.close();
        } catch (IOException e) {
            throw new RemoteCryptoException("An exception occurred while closing the socket: "+clientSocket.toString(),e);
        } catch (InterruptedException e) {
            throw new RemoteCryptoException("An exception occurred while trying to make thread "+Thread.currentThread().getName()+" sleep for 10 milliseconds",e);
        }
    }
}

