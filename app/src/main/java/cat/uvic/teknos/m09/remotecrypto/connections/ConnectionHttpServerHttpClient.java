package cat.uvic.teknos.m09.remotecrypto.connections;

import cat.uvic.teknos.m09.polsane.cryptoutils.CryptoUtils;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import java.io.*;
import java.net.Socket;
import java.util.Base64;

public class ConnectionHttpServerHttpClient {
    private final Base64.Encoder encoder;
    private Socket client;
    private RawHttp http;
    private String body = "";

    private final String EXEMPLE_URI = "http://localhost:50002/hash?data=MESSAGE";

    public ConnectionHttpServerHttpClient(Socket socket) {
        this.client = socket;
        http = new RawHttp();
        encoder = Base64.getEncoder();
    }

    public void processRequestResponse(){
        try {
            var request = http.parseRequest(client.getInputStream());
            var queryStr= request.getUri().getQuery();
            String[] query = queryStr.split("=");
            var data = "";

            try {
                data=query[1];

                var hashData = CryptoUtils.hash(data.getBytes()).getHash();

                var hash = new String(hashData);

                if(takeHashUri(request) && !data.equals(null) && query[0].equals("data"))
                    body = getHTML(String.valueOf(hash.getBytes()), true);
                else
                    body = getHTML("ERROR 404, EXEMPLE URI: Exemple: " + EXEMPLE_URI, false);

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
                }
            } catch (Exception e) {
                var body = getHTML("ERROR 400, EXEMPLE URI: Exemple: " + EXEMPLE_URI, false);

                http.parseResponse(
                        "HTTP/1.1 200 OK\r\n" +
                                "Content-Type: html\r\n" +
                                "Content-Length: " + body.length() + "\r\n" +
                                "Server: localhost\r\n" +
                                "\r\n" +
                                body).writeTo(client.getOutputStream());
            }
        } catch (IOException e) {
            body = getHTML("INCORRECT URI TO GET HASH MESSAGE, Exemple: " + EXEMPLE_URI, false);
            try {
                http.parseResponse(
                        "HTTP/1.1 200 OK\r\n" +
                                "Content-Type: html\r\n" +
                                "Content-Length: " + body.length() + "\r\n" +
                                "Server: localhost\r\n" +
                                "\r\n" +
                                body).writeTo(client.getOutputStream());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private String getHTML(String message, boolean b){
        if(b)
            message = encoder.encodeToString(message.getBytes());

        String body="<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" + "<h1>Encrypted Message</h1>" +
                "<p>"+message+"</p>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
        return body;
    }

    private boolean takeHashUri(RawHttpRequest request) {
        String s = request.getUri().toString();
        var sa = s.split("/");

        var hs = sa[3];

        var h = hs.split("data");

        if(h[0].equals("hash?"))
            return true;
        else
            return false;
    }
}