package cat.uvic.teknos.m09.remotecrypto;

import cat.uvic.teknos.m09.remotecrypto.servers.ServerHttp;
import cat.uvic.teknos.m09.remotecrypto.servers.ServerTelnet;
import cat.uvic.teknos.m09.remotecrypto.servers.thread.FTPThread;

public class ServerInit {
    private static ServerHttp http;
    private static ServerTelnet telnet;

    private static FTPThread ftpConnection;


    public static void main(String ...args){
        try{
            http = new ServerHttp();
            telnet = new ServerTelnet();
            ftpConnection = new FTPThread();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void join() throws InterruptedException {
        http.join();
        telnet.join();
        ftpConnection.join();
    }
}
