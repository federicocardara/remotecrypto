package cat.uvic.teknos.m09.remotecrypto;

import cat.uvic.teknos.m09.remotecrypto.ConnectionsViaTelnet.ServerTelnet;
import cat.uvic.teknos.m09.remotecrypto.FTP.FTPConnection;
import cat.uvic.teknos.m09.remotecrypto.HTTP.ServerHttp;

import java.io.IOException;

public class RemoteCrypto {
    private static ServerHttp serverHttp;
    private static ServerTelnet serverTelnet;

    private static FTPConnection ftpConnection;

    public static void main(String ...args) throws IOException {
        serverHttp = new ServerHttp();
        serverTelnet = new ServerTelnet();
        ftpConnection = new FTPConnection();
    }
    public static void joinServers() throws InterruptedException {
        serverTelnet.joinThread();
        serverHttp.joinThread();
        ftpConnection.join();
    }
}
