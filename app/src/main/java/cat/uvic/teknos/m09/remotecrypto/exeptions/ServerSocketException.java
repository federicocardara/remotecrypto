package cat.uvic.teknos.m09.remotecrypto.exeptions;

public class ServerSocketException extends RuntimeException{

    public void ServerSocketException() {
        System.out.println("ERROR: CAN'T CREATE SERVER SOCKET");
    }
}
