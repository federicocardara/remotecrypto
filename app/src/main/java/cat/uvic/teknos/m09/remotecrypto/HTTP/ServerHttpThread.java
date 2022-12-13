package cat.uvic.teknos.m09.remotecrypto.HTTP;



import cat.uvic.teknos.m09.elbouzzaouiabdelkarim.cryptoutils.CryptoUtils;
import cat.uvic.teknos.m09.elbouzzaouiabdelkarim.cryptoutils.Exceptions.MissingPropertiesException;
import cat.uvic.teknos.m09.elbouzzaouiabdelkarim.cryptoutils.dto.DigestResult;
import cat.uvic.teknos.m09.remotecrypto.HTTP.Exceptions.*;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;

import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

public class ServerHttpThread implements Runnable{
    CryptoUtils cryptoUtils = new CryptoUtils();
    RawHttp http = new RawHttp();
    private Socket client;
    private RawHttpRequest request;

    public ServerHttpThread(Socket client) {
        this.client = client;
    }

    public ServerHttpThread(){
    }

    public void setClient(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        String response = "";
        try {
            initConnection();
            String path = request.getUri().getPath();
            String query = request.getUri().getQuery();
            String res = controller(path,query);
            response = sendSuccessResponse(res);

        }catch (NotFoundException e) {
            response = sendNotFoundResponse();
        } catch (InternalServerErrorException | HttpException | ConnectionException e) {
            response = sendInternalServerErrorResponse();
        } catch (BadRequestException e) {
            response = sendBadRequestResponse();
        }
        sendResponse(response);
    }

    private void sendResponse(String response) {
        try{
            http.parseResponse(response).writeTo(client.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initConnection() throws ConnectionException {
        try {
            request = http.parseRequest(client.getInputStream());
        } catch (Exception e) {
            throw new ConnectionException();
        }
    }

    public String controller(String path, String query) throws NotFoundException, HttpException, InternalServerErrorException, BadRequestException {
        try {
            if (path.contains("/cryptoutils/hash")) {
                if (query.length() == 0) {
                    throw new BadRequestException();
                }
                DigestResult digest = cryptoUtils.hash(query.split("=")[1].getBytes());
                String str = new String(digest.getHash());
                return str;
            } else {
                throw new NotFoundException();
            }
        } catch (NotFoundException e) {
            throw new NotFoundException();
        } catch (NoSuchAlgorithmException | MissingPropertiesException e) {
            throw new InternalServerErrorException();
        } catch (BadRequestException e) {
            throw new BadRequestException();
        }
    }

    private String sendInternalServerErrorResponse() {
        return "HTTP/1.1 500 Internal Server Error\n" +
                "Content-Type: text/plain\n" +
                "Content-Length: 0\n" +
                "\n";
    }

    private String sendNotFoundResponse() {
        return "HTTP/1.1 404 Not Found\n" +
                "Content-Type: text/plain\n" +
                "Content-Length: 0\n" +
                "\n";
    }

    private String sendSuccessResponse(String str) {
        return "HTTP/1.1 200 OK\n" +
                "Content-Type: text/plain\n" +
                "Content-Length: " + str.length() + "\n" +
                "\n" +
                "str";
    }

    private String sendBadRequestResponse(){
        return "HTTP/1.1 400 Bad Request\n" +
                "Content-Type: text/plain\n" +
                "Content-Length: 0\n" +
                "\n";
    }
}
