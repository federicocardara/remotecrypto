package cat.uvic.teknos.m09.remotecrypto;

import cat.uvic.teknos.m09.remotecrypto.servers.ServerHttp;
import cat.uvic.teknos.m09.remotecrypto.servers.ServerTelnet;

public class ServerInit {
    private static ServerHttp http;
    private static ServerTelnet telnet;

    public static void main(String ...args){
        try{
            http = new ServerHttp();
            telnet = new ServerTelnet();
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
