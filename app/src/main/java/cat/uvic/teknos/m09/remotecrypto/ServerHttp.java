package cat.uvic.teknos.m09.remotecrypto;

import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ThreadPoolExecutor;

public class ServerHttp {
    private static RawHttp http = new RawHttp();
    public static int PORT = 50002;

    public ServerHttp() {
        ThreadPoolExecutor pool = new ThreadPoolExecutor();


        try{
            run();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void run() throws IOException {
        ServerSocket server = new ServerSocket(PORT);

        Socket client  = server.accept();
        RawHttpRequest request = http.parseRequest(client.getInputStream());




    }
}
