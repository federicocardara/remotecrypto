package cat.uvic.teknos.m09.remotecrypto;

import cat.uvic.teknos.m09.remotecrypto.Client.ClientFTP;
import cat.uvic.teknos.m09.remotecrypto.servers.ServerHttp;
import cat.uvic.teknos.m09.remotecrypto.servers.ServerTelnet;

public class MainActivity {
    private static ServerHttp http;
    private static ServerTelnet telnet;
    private static ClientFTP ftpClient;

    public static void main(String ...args){
        try{
            http = new ServerHttp();
            telnet = new ServerTelnet();
            ftpClient=new ClientFTP();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void join() throws InterruptedException {
        http.join();
        telnet.join();
        ftpClient.join();
    }
}
