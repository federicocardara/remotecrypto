package cat.uvic.teknos.m09.remotecrypto;

import cat.uvic.teknos.m09.remotecrypto.servers.HttpServer;
import cat.uvic.teknos.m09.remotecrypto.servers.TerminalServer;

public class TurnOnServers {
    public static void main(String[] args) {
//        TerminalServer terminalServer = new TerminalServer();
        HttpServer httpServer = new HttpServer();
    }
}
