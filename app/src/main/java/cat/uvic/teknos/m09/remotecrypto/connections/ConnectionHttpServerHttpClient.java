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
            String body="";
            try {
                if(query.length>1) {
                    data = query[1];
                    var digest= CryptoUtils.hash(data.getBytes());
                    var hashData =digest.getHash();
                    body="";
                    String hash = new String(hashData);
                    if(Boolean.parseBoolean(properties.getProperty("hash.salt"))){
                        String salt =new String(digest.getSalt());
                        body="<!DOCTYPE html>\n" +
                                "<html>\n" +
                                "<body>\n" +
                                "\n" +
                                "<h1>Data To Hash</h1>\n" +
                                "<p>"+"Hash Byte Array Inside A String: "+hash+"</p>\n" +
                                "<p>"+"Algorithm: "+properties.getProperty("hash.algorithm")+"</p>\n" +
                                "<p>"+"Salt Byte Array Inside A String: "+salt+"</p>\n" +
                                "\n" +
                                "</body>\n" +
                                "</html>";
                    }if(!Boolean.parseBoolean(properties.getProperty("hash.salt"))) {
                        body="<!DOCTYPE html>\n" +
                                "<html>\n" +
                                "<body>\n" +
                                "\n" +
                                "<h1>Data To Hash</h1>\n" +
                                "<p>"+"Hash Byte Array Inside A String: "+hash+"</p>\n" +
                                "<p>"+"Algorithm: "+properties.getProperty("hash.algorithm")+"</p>\n" +
                                "\n" +
                                "</body>\n" +
                                "</html>";
                    }
                }



                //System.out.println(takeHashUri(request));





                if(request.getMethod().equals("GET")) {
                    System.out.println(body);
                    if (query.length==2 && query[0].equals("data")) {
                        http.parseResponse(
                                "HTTP/1.1 200 OK\r\n" +
                                        "Content-Type: html\r\n" +
                                        "Content-Length: " + (body.length()) + "\r\n" +
                                        "Server: localhost\r\n" +
                                        "\r\n" +
                                        body).writeTo(client.getOutputStream());
                    } if(query.length!=2 && query[0].equals("data")) {
                        http.parseResponse("HTTP/1.1 400 Bad Request\n" +
                                "Content-Type: text/plain\n" +
                                "Content-Length: 0\n" +
                                "\n").writeTo(client.getOutputStream());
                    }if(!query[0].equals("data")){
                        http.parseResponse("HTTP/1.1 404 Not Found\n" +
                                "Content-Type: text/plain\n" +
                                "Content-Length: 0\n" +
                                "\n").writeTo(client.getOutputStream());
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}