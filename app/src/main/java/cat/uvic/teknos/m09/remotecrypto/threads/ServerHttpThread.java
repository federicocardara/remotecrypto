package cat.uvic.teknos.m09.remotecrypto.threads;

import cat.uvic.teknos.m09.elbouzzaouiabdelkarim.cryptoutils.CryptoUtils;
import cat.uvic.teknos.m09.elbouzzaouiabdelkarim.cryptoutils.Exceptions.MissingPropertiesException;
import cat.uvic.teknos.m09.elbouzzaouiabdelkarim.cryptoutils.dto.DigestResult;
import cat.uvic.teknos.m09.remotecrypto.exceptions.*;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;

import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

public class ServerHttpThread implements Runnable {
    CryptoUtils cryptoUtils = new CryptoUtils();
    RawHttp http = new RawHttp();
    private Socket client;
    private RawHttpRequest request;

    public ServerHttpThread(Socket client) {
        this.client = client;
    }

    public ServerHttpThread(){
    }

    
    /** 
     * @param client
     */
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
        } catch (InternalServerErrorException| HttpException | ConnectionException e) {
           response = sendInternalServerErrorResponse();
        } catch (BadRequestException e) {
           response = sendBadRequestResponse();
        }
        sendResponse(response);

    }

    
    /** 
     * @param response
     */
    private void sendResponse(String response) {
        try{
            http.parseResponse(response).writeTo(client.getOutputStream());
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    /** 
     * get http request of client
     * @throws ConnectionException
     */
    public void initConnection() throws ConnectionException {
        try {
            request = http.parseRequest(client.getInputStream());
        } catch (Exception e) {
            throw new ConnectionException();
        }
    }

    
    /** 
     * method to control path of request and returns response http as string
     * @param path request url
     * @param query parameters in url
     * @return String
     * @throws NotFoundException
     * @throws HttpException
     * @throws InternalServerErrorException
     * @throws BadRequestException
     */
    public String controller(String path, String query) throws NotFoundException, HttpException, InternalServerErrorException, BadRequestException {
        try {
            if (path.contains("/cryptoutils/hash")) {
                if (query.length() == 0) {
                    throw new BadRequestException();
                }
                DigestResult digest = cryptoUtils.hash(query.split("=")[1].getBytes());
                String str = "{\"hash\":\""+new String(digest.getHash())+"\",\n";
                str+="\"algorithm\":\""+digest.getAlgorithm()+"\",\n";
                str+="\"salt\":\""+digest.getSalt()+"\",\n}";
                return str;
            } else {
                throw new NotFoundException();
            }
        } catch (NotFoundException e) {
            throw new NotFoundException();
        } catch (NoSuchAlgorithmException| MissingPropertiesException e) {
            throw new InternalServerErrorException();
        } catch (BadRequestException e) {
            throw new BadRequestException();
        }
    }

    
    /**
     * @return String
     */
    private String sendInternalServerErrorResponse() {
        return "HTTP/1.1 500 Internal Server Error\n" +
                "Content-Type: text/plain\n" +
                "Content-Length: 0\n" +
                "\n";
    }

    
    /** 
     * @return String
     */
    private String sendNotFoundResponse() {
        return "HTTP/1.1 404 Not Found\n" +
                "Content-Type: text/plain\n" +
                "Content-Length: 0\n" +
                "\n";
    }

    
    /** 
     * @param str
     * @return String
     */
    private String sendSuccessResponse(String str) {
        return "HTTP/1.1 200 OK\n" +
                "Content-Type: application/json\n" +
                "Content-Length: " + str.length() + "\n" +
                "\n" +
                str;
    }

    
    /** 
     * @return String
     */
    private String sendBadRequestResponse(){
        return "HTTP/1.1 400 Bad Request\n" +
                    "Content-Type: text/plain\n" +
                    "Content-Length: 0\n" +
                    "\n";
    }
}
