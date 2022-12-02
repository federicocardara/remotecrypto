package cat.uvic.teknos.m09.remotecrypto.connections;

import cat.uvic.teknos.m09.polsane.cryptoutils.CryptoUtils;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;

import java.io.*;
import java.net.Socket;
import java.util.Base64;
import java.util.Properties;

public class ConnectionHttpServerHttpClient {
    private final Base64.Encoder encoder;
    private Socket client;
    private RawHttp http;

    public ConnectionHttpServerHttpClient(Socket socket) {
        this.client = socket;
        http=new RawHttp();
        encoder = Base64.getEncoder();
    }

    public void processRequestResponse(){
        try {
            RawHttpRequest request = http.parseRequest(client.getInputStream());
            String queryStr= request.getUri().getQuery();
            String[] query = queryStr.split("=");
            String data=query[1];

            var hashData = CryptoUtils.hash(data.getBytes()).getHash();

            String hash = new String(hashData);

            String body="<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<body>\n" +
                    "\n" +
                    "<h1>Text to Base64</h1>\n" +
                    "<p>"+hash+"</p>\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>";

            if(request.getMethod().equals("GET")) {
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
