package cat.uvic.teknos.m09.remotecrypto.servers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cat.uvic.teknos.m09.remotecrypto.threads.ServerUDPThread;

public class ServerUDP {
    private static int PORT = 50003;
    private DatagramSocket server;
    private boolean running;
    private byte[] buf = new byte[256];
    private Thread serverThread;
    ExecutorService pool = Executors.newFixedThreadPool(10);

    public ServerUDP(){
        serverThread = new Thread(()->{
            try{
            server = new DatagramSocket(PORT);
            System.out.println("Server UDP Start...");
            run();
            }catch(Exception e){
                e.printStackTrace();
            }
        });

        serverThread.start();
    }

    private void run() throws IOException{
        pool = Executors.newFixedThreadPool(10);
        while(true){
            DatagramPacket packet 
              = new DatagramPacket(buf, buf.length);
            System.out.println("waiting for client...");
            server.receive(packet);
            pool.execute(new ServerUDPThread(server,packet));
        }
    }

    public void join(){
        try {
            pool.shutdown();
            serverThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
