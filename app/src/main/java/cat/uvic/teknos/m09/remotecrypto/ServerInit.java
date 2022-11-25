package cat.uvic.teknos.m09.remotecrypto;

import cat.uvic.teknos.m09.remotecrypto.servers.ServerHttp;
import cat.uvic.teknos.m09.remotecrypto.servers.ServerTelnet;

import java.io.IOException;

public class ServerInit {

    public static void main(String[] args) throws IOException {
        ServerHttp server = new ServerHttp();
        ServerTelnet telnet = new ServerTelnet();

    }
}
