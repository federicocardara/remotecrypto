package cat.uvic.teknos.m09.remotecrypto.connections;

import cat.uvic.teknos.m09.polsane.cryptoutils.CryptoUtils;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;

import java.io.IOException;
import java.net.Socket;
import java.util.Base64;

public class ConnectionHttpServerHttpClient {
    private final Base64.Encoder encoder;
    private Socket client;
    private RawHttp http;

    public ConnectionHttpServerHttpClient(Socket socket) {
        this.client = socket;
        http = new RawHttp();
        encoder = Base64.getEncoder();
    }

    public void processRequestResponse(){
        try {
            RawHttpRequest request = http.parseRequest(client.getInputStream());
            String queryStr= request.getUri().getQuery();
            String[] query = queryStr.split("=");
            String data;

            try {
                data=query[1];
                System.out.println("si");


                var hashData = CryptoUtils.hash(data.getBytes()).getHash();

                String hash = new String(hashData);

                //System.out.println(takeHashUri(request));



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
            } catch (Exception e) {
                String body="<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<body>\n" +
                        "\n" +
                        "<h1>Text to Base64</h1>\n" +
                        "<p>"+"HELLOW WORLD"+"</p>\n" +
                        "\n" +
                        "</body>\n" +
                        "</html>";

                http.parseResponse(
                        "HTTP/1.1 200 OK\r\n" +
                                "Content-Type: html\r\n" +
                                "Content-Length: " + body.length() + "\r\n" +
                                "Server: localhost\r\n" +
                                "\r\n" +
                                body).writeTo(client.getOutputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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