package cat.uvic.teknos.m09.remotecrypto.clients;

import cat.uvic.teknos.m09.remotecrypto.servers.ClusterServer;

public class ClusterClient {
    public static void main(String[] args) {
        ClusterServer server=new ClusterServer(50001,50002);
        server.turnOnServers();
    }
}
