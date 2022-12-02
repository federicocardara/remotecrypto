package cat.uvic.teknos.m09.remotecrypto.servers.thread;

import cat.uvic.teknos.m09.elbouzzaouiabdelkarim.cryptoutils.CryptoUtils;
import cat.uvic.teknos.m09.elbouzzaouiabdelkarim.cryptoutils.dto.DigestResult;
import cat.uvic.teknos.m09.remotecrypto.exceptions.ConnectionException;
import cat.uvic.teknos.m09.remotecrypto.exceptions.HttpException;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.body.HttpMessageBody;

import java.io.IOException;
import java.net.Socket;

public class ServerHttpThread implements Runnable {
    CryptoUtils cryptoUtils = new CryptoUtils();
    RawHttp http = new RawHttp();
    private Socket client;
    private RawHttpRequest request;

    public ServerHttpThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            initConnection();
            controller();

        } catch (HttpException e) {
            sendInternalServerErrorResponse();
        } catch (ConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendBadRequestResponse() throws HttpException {
        try{
            http.parseResponse("HTTP/1.1 400 Bad Request\n" +
                    "Content-Type: text/plain\n" +
                    "Content-Length: 0\n" +
                    "\n"
            ).writeTo(client.getOutputStream());
        }catch(IOException e){
            throw new HttpException();
        }
    }

    private void initConnection() throws ConnectionException {
        try{
            request = http.parseRequest(client.getInputStream());

        }catch(IOException e){
            throw new ConnectionException();
        }
    }

    private void controller() throws HttpException {
        try{

        if (request.
                getUri().getPath().contains("/cryptoutils/hash")) {
            String query = request.getUri().getQuery();
            if(query.length()==0){
                sendBadRequestResponse();
            }
            DigestResult digest = cryptoUtils.hash(query.split("=")[1].getBytes());
            String str = new String(digest.getHash());
            http.parseResponse("HTTP/1.1 200 OK\n" +
                            "Content-Type: text/plain\n" +
                        "Content-Length: 9\n" +
                            "\n" +
                            str
            ).writeTo(client.getOutputStream());
        }else{
            sendNotFoundResponse();
        }
        }catch (Exception e) {
            throw new HttpException();
        }
    }

    private void sendInternalServerErrorResponse(){
        try{
            http.parseResponse("HTTP/1.1 500 Internal Server Error\n" +
                    "Content-Type: text/plain\n" +
                    "Content-Length: 0\n" +
                    "\n"
            ).writeTo(client.getOutputStream());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void sendNotFoundResponse(){
        try{
            http.parseResponse("HTTP/1.1 404 Not Found\n" +
                    "Content-Type: text/plain\n" +
                    "Content-Length: 0\n" +
                    "\n"
            ).writeTo(client.getOutputStream());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
