package cat.uvic.teknos.m09.remotecrypto.servers;

public class ClusterServer {
    private final int HTTP_SERVER_PORT;
    private final int TERMINAL_SERVER_PORT;

    public ClusterServer(int http_server_port, int terminal_server_port) {
        HTTP_SERVER_PORT = http_server_port;
        TERMINAL_SERVER_PORT = terminal_server_port;
    }

    public  void turnOnServers() {
        var httpServerThread=new Thread(new HttpServer(HTTP_SERVER_PORT)::turnOnServer);
        var terminalServerThread=new Thread(new TerminalServer(TERMINAL_SERVER_PORT)::turnOnServer);
        httpServerThread.start();
        terminalServerThread.start();
        while (terminalServerThread.isAlive() && httpServerThread.isAlive()){

        }
    }
}
