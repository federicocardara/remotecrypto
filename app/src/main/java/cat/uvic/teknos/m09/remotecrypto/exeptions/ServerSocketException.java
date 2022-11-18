package cat.uvic.teknos.m09.remotecrypto.exeptions;

public class ServerSocketException extends RuntimeException{

    public String ServerSocketException() {
        return "ERROR: CAN'T CREATE SERVER SOCKET";
    }
}
