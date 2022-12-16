package cat.uvic.teknos.m09.remotecrypto;

import cat.uvic.teknos.m09.remotecrypto.servers.ServerHttp;
import cat.uvic.teknos.m09.remotecrypto.servers.ServerTelnet;
import cat.uvic.teknos.m09.remotecrypto.servers.ServerUDP;
import cat.uvic.teknos.m09.remotecrypto.threads.FTPConnection;

public class ServerInit {
    private static ServerHttp http;
    private static ServerTelnet telnet;
    private static FTPConnection ftpConnection;
    private static ServerUDP udp;

    
    /** 
     * @param ...args
     */
    public static void main(String ...args){
        try{
            http = new ServerHttp();
            telnet = new ServerTelnet();
            udp = new ServerUDP();
            ftpConnection = new FTPConnection();
            ftpConnection.init();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void join(){
        http.join();
        telnet.join();
        udp.join();
    }
}
