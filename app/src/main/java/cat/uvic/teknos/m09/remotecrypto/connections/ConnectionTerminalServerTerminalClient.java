package cat.uvic.teknos.m09.remotecrypto.connections;

import cat.uvic.teknos.m09.polsane.cryptoutils.CryptoUtils;
import cat.uvic.teknos.m09.remotecrypto.exceptions.RemoteCryptoTerminalException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Base64;
import java.util.Properties;

public class ConnectionTerminalServerTerminalClient {
    private Socket clientSocket;
    private  boolean dataIsEmpty =false;
    private Properties properties;

    public ConnectionTerminalServerTerminalClient(Socket clientSocket) {
        this.clientSocket =clientSocket;
        properties=new Properties();
        try {
            properties.load(ConnectionTerminalServerTerminalClient.class.getResourceAsStream("/cryptoutils.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void clientConnection() {
        try {
            var input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            var output = new PrintWriter(clientSocket.getOutputStream());
            output.println("Enter the string that you wish to get the hash from: ");
            output.flush();
            String data;
            Thread.sleep(50);
            data = input.readLine();
            while (!dataIsEmpty) {
                if (!data.equals("")) {
                   var digest= CryptoUtils.hash(data.getBytes());
                    output.println("Hash Byte Array Inside A String: " + new String(digest.getHash()));
                    output.flush();
                    output.println("Algorithm : " + properties.getProperty("hash.algorithm"));
                    output.flush();
                    if(Boolean.parseBoolean(properties.getProperty("hash.salt"))){
                        output.println("Salt Byte Array Inside A String: " + new String(digest.getSalt()));
                        output.flush();
                    }
                    
                    output.println("Enter the string that you wish to get the hash from: ");
                    output.flush();
                    data = input.readLine();
                } else {
                    dataIsEmpty = !dataIsEmpty;
                }
            }
            output.println("You have terminated the connection.");
            output.flush();
            Thread.sleep(10); //DO NOT DELETE
            clientSocket.close();
        } catch (IOException e) {
            throw new RemoteCryptoTerminalException("An exception occurred while closing the socket: "+clientSocket.toString(),e);
        } catch (InterruptedException e) {
            throw new RemoteCryptoTerminalException("An exception occurred while trying to make thread "+Thread.currentThread().getName()+" sleep for 10 milliseconds",e);
        }
    }
}