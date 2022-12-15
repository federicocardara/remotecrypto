package cat.uvic.teknos.m09.remotecrypto.connections;

import cat.uvic.teknos.m09.polsane.cryptoutils.CryptoUtils;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;

import java.io.IOException;
import java.net.Socket;
import java.util.Base64;
import java.util.Properties;

public class ConnectionHttpServerHttpClient {
    private Properties properties;
    private Socket client;
    private RawHttp http;

    public ConnectionHttpServerHttpClient(Socket socket) {
        this.client = socket;
        http = new RawHttp();
        properties=new Properties();
        try {
            properties.load(ConnectionHttpServerHttpClient.class.getResourceAsStream("/cryptoutils.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void processRequestResponse(){
        try {
            RawHttpRequest request = http.parseRequest(client.getInputStream());
            String queryStr= request.getUri().getQuery();
            String[] query = queryStr.split("=");
            String data;
            String dataType;

            try {
                dataType=query[0];
                data=query[1];


                var digest= CryptoUtils.hash(data.getBytes());
                var hashData =digest.getHash();

                String hash = new String(hashData);
                String salt =new String(digest.getSalt());
                //System.out.println(takeHashUri(request));



                String body="<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<body>\n" +
                        "\n" +
                        "<h1>Data To Hash</h1>\n" +
                        "<p>"+"Hash Byte Array In String: "+hash+"</p>\n" +
                        "<p>"+"Algorithm: "+properties.getProperty("hash.algorithm")+"</p>\n" +
                        "<p>"+"Salt Byte Array In String: "+salt+"</p>\n" +
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


}