package cat.uvic.teknos.m09.remotecrypto.servers;

import cat.uvic.teknos.m09.remotecrypto.servers.threads.ServerHttpThread;
import rawhttp.core.RawHttp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerHttp {
    private static RawHttp http = new RawHttp();
    public static int PORT = 50002;
    private ServerSocket server;
    private Thread serverThread;
    ExecutorService pool = Executors.newFixedThreadPool(10);

    public ServerHttp() throws IOException {
        serverThread = new Thread(()->{
            try{
                server = new ServerSocket(PORT);
                System.out.println("Server Http Start...");
                listener();
            }catch(Exception e){
                e.printStackTrace();
            }
        });

        serverThread.start();
    }

    private void listener() throws IOException {
        pool = Executors.newFixedThreadPool(10);
        while(true){
            System.out.println("waiting for client...");
            Socket client  = server.accept();
            pool.execute(new ServerHttpThread(client));
        }
    }


    public void join(){
        try {
            serverThread.join();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}