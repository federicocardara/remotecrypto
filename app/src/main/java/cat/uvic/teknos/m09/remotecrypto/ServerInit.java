package cat.uvic.teknos.m09.remotecrypto;

import cat.uvic.teknos.m09.remotecrypto.servers.ServerHttp;
import cat.uvic.teknos.m09.remotecrypto.servers.ServerTelnet;

import java.io.IOException;

public class ServerInit {
    private static ServerHttp http;
    private static ServerTelnet telnet;

    public static void main(String[] args) throws IOException, InterruptedException {
        http = new ServerHttp();
        telnet = new ServerTelnet();

        http.join();
        telnet.join();

    }
}
