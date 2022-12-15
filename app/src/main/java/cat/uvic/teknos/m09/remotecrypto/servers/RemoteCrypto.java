package cat.uvic.teknos.m09.remotecrypto.servers;

public class RemoteCrypto {
    private  static final int HTTP_SERVER_PORT=6008;
    private static final int TERMINAL_SERVER_PORT=50002;


    public static void main(String[] args) {
        var httpServerThread=new Thread(new HttpServer(HTTP_SERVER_PORT)::turnOnServer);
        var terminalServerThread=new Thread(new TerminalServer(TERMINAL_SERVER_PORT)::turnOnServer);
        httpServerThread.start();
        terminalServerThread.start();
    }
}
