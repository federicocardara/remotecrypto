package cat.uvic.teknos.m09.remotecrypto.servers;

import cat.uvic.teknos.m09.remotecrypto.servers.threads.ServerTelnetThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerTelnet  {

    public static final int PORT = 50001;
    private ServerSocket server;
    public Thread threadServer;
    private ExecutorService threadExecutor;
    public ServerTelnet() throws IOException {
        threadServer =  new Thread(()->{
            try{
                server = new ServerSocket(PORT);
                System.out.println("Server telnet Start...");
                listener();
            }catch(Exception e){
                e.printStackTrace();
            }
        });

        threadServer.start();
    }
    private void listener() throws IOException {
        threadExecutor  = Executors.newFixedThreadPool(3); // Up to 3 clients at the same time
        while(true){
            System.out.println("waiting for client...");
            Socket client  = server.accept();
            threadExecutor.execute(new ServerTelnetThread(client)); // Runs run method from the clientThread
        }
    }

    public void join() throws InterruptedException {
        threadServer.join();
    }

    public void stop(){
        threadExecutor.shutdown();
    }



}
