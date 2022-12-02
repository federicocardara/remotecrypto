package cat.uvic.teknos.m09.remotecrypto.connections;

import cat.uvic.teknos.m09.remotecrypto.exceptions.RemoteCryptoHttpException;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;

import java.io.*;
import java.net.Socket;
import java.util.Base64;

public class ConnectionHttpServerHttpClient {
    private BufferedReader inputStream;
    private PrintWriter outputStream;
    private final Base64.Encoder encoder;
    private Socket client;
    private RawHttp http;

    public ConnectionHttpServerHttpClient(Socket socket, BufferedReader inputStream, PrintWriter outputStream) {
        this.client = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        http=new RawHttp();
        encoder = Base64.getEncoder();
    }

    public void connection(){
        try {
            RawHttpRequest request = http.parseRequest(client.getInputStream());
            String queryStr= request.getUri().getQuery();
            String[] query=queryStr.split("=");
            String data=query[1];
            var encoder=Base64.getEncoder();
            String encodedData=new String(encoder.encode(data.getBytes()));
            System.out.println(query[1]);

            String body="<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<body>\n" +
                    "\n" +
                    "<h1>Text to Base64</h1>\n" +
                    "<p>"+encodedData+"</p>\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>";
            System.out.println(request.getUri());
            if(request.getMethod().equals("GET")) {
                //TODO BAD REQUEST if data is null or "" 400
                if (!data.equals("")) {
                    http.parseResponse(
                            "HTTP/1.1 200 OK\r\n" +
                                    "Content-Type: html\r\n" +
                                    "Content-Length: " + body.length() + "\r\n" +
                                    "Server: localhost\r\n" +
                                    "\r\n" +
                                    body).writeTo(client.getOutputStream());
                } else {
                    http.parseResponse("HTTP/1.1 404 Not Found\n" +
                            "Content-Type: text/plain\n" +
                            "Content-Length: 0\n" +
                            "\n").writeTo(client.getOutputStream());
                }
            }else {
                //idk
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
