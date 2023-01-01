package cat.uvic.teknos.m09.remotecrypto.connections;

import cat.uvic.teknos.m09.polsane.cryptoutils.CryptoUtils;
import cat.uvic.teknos.m09.remotecrypto.exceptions.RemoteCryptoHttpException;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;

import java.io.*;
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
            synchronized (ConnectionHttpServerHttpClient.class.getResourceAsStream("/cryptoutils.properties")) {
                properties.load(ConnectionHttpServerHttpClient.class.getResourceAsStream("/cryptoutils.properties"));
            }
        } catch (IOException e) {
            throw new RemoteCryptoHttpException(e);
        }
    }

    public void processRequestResponse(){
        try {
            RawHttpRequest request = http.parseRequest(client.getInputStream());
            String queryStr= request.getUri().getQuery();
            String path=request.getUri().getPath();
            String[] query = queryStr.split("=");
            String data;
            String body="";
            try {
                if(query.length>1) {
                    data = query[1];
                    var digest= CryptoUtils.hash(data.getBytes());
                    var hashData =digest.getHash();
                    var enc=Base64.getEncoder();

                    body="";
                    String hash = enc.encodeToString(hashData);
                    if(Boolean.parseBoolean(properties.getProperty("hash.salt"))){
                        String salt =enc.encodeToString(digest.getSalt());
                        body="<!DOCTYPE html>\n" +
                                "<html>\n" +
                                "<body>\n" +
                                "\n" +
                                "<h1>Data To Hash</h1>\n" +
                                "<p>"+"Hash Byte Array As A String In Base64: "+hash+"</p>\n" +
                                "<p>"+"Algorithm: "+properties.getProperty("hash.algorithm")+"</p>\n" +
                                "<p>"+"Salt Byte Array As A String In Base64: "+salt+"</p>\n" +
                                "\n" +
                                "</body>\n" +
                                "</html>";
                    }if(!Boolean.parseBoolean(properties.getProperty("hash.salt"))) {
                        body="<!DOCTYPE html>\n" +
                                "<html>\n" +
                                "<body>\n" +
                                "\n" +
                                "<h1>Data To Hash</h1>\n" +
                                "<p>"+"Hash Byte Array As A String In Base64: "+hash+"</p>\n" +
                                "<p>"+"Algorithm: "+properties.getProperty("hash.algorithm")+"</p>\n" +
                                "\n" +
                                "</body>\n" +
                                "</html>";
                    }
                }
                if(request.getMethod().equals("GET")) {
                    var bol=path.equals("/hash");
                    if (query.length==2 && query[0].equals("data") && path.equals("/hash")) {
                        http.parseResponse(
                                "HTTP/1.1 200 OK\r\n" +
                                        "Content-Type: html\r\n" +
                                        "Content-Length: " + (body.length()) + "\r\n" +
                                        "Server: localhost\r\n" +
                                        "\r\n" +
                                        body).writeTo(client.getOutputStream());
                    } if((query.length!=2 && query[0].equals("data") && path.equals("/hash"))||(path.equals("/hash") && !query[0].equals("data"))) {
                        http.parseResponse("HTTP/1.1 400 Bad Request\n" +
                                "Content-Type: text/plain\n" +
                                "Content-Length: 0\n" +
                                "\n").writeTo(client.getOutputStream());
                    }if(query[0].equals("data") && !path.equals("/hash")){
                        http.parseResponse("HTTP/1.1 404 Not Found\n" +
                                "Content-Type: text/plain\n" +
                                "Content-Length: 0\n" +
                                "\n").writeTo(client.getOutputStream());
                    }
                }
            } catch (IOException e) {
                throw new RemoteCryptoHttpException(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                client.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}