package cat.uvic.teknos.m09.remotecrypto;

import cat.uvic.teknos.m09.remotecrypto.servers.ServerHttp;
import cat.uvic.teknos.m09.remotecrypto.servers.ServerTelnet;
import cat.uvic.teknos.m09.remotecrypto.servers.thread.FTPConnection;

public class ServerInit {
    private static ServerHttp http;
    private static ServerTelnet telnet;
    private static FTPConnection ftpConnection;

    
    /** 
     * @param ...args
     */
    public static void main(String ...args){
        try{
            http = new ServerHttp();
            telnet = new ServerTelnet();
            ftpConnection = new FTPConnection();
            ftpConnection.init();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void join(){
        http.join();
        telnet.join();
    }
}
