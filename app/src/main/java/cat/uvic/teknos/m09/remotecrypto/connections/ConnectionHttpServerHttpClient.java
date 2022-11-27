package cat.uvic.teknos.m09.remotecrypto.connections;

import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;

import java.io.*;
import java.net.Socket;
import java.util.Base64;

public class ConnectionHttpServerHttpClient {

    private BufferedReader inputStream;
    private PrintWriter outputStream;
    private final Base64.Encoder encoder = Base64.getEncoder();
    private Socket client;
    private RawHttp http = new RawHttp();

    public ConnectionHttpServerHttpClient(Socket socket, BufferedReader inputStream, PrintWriter outputStream) {
        this.client = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public void connection(){
        try {
            RawHttpRequest request = http.parseRequest(client.getInputStream());
            System.out.println(request.getUri());

            if (request.getUri().getPath().equals("/saysomething")) {
                http.parseResponse("HTTP/1.1 200 OK\n" +
                        "Content-Type: text/plain\n" +
                        "Content-Length: 25\n" +
                        "\n" +
                        "en pol es molt impacient\n").writeTo(client.getOutputStream());
            } else {
                http.parseResponse("HTTP/1.1 404 Not Found\n" +
                        "Content-Type: text/plain\n" +
                        "Content-Length: 0\n" +
                        "\n").writeTo(client.getOutputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
