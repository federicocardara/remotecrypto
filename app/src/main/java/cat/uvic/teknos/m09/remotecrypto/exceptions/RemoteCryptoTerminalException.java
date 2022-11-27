package cat.uvic.teknos.m09.remotecrypto.exceptions;

public class RemoteCryptoTerminalException extends RuntimeException{
    public RemoteCryptoTerminalException() {
    }

    public RemoteCryptoTerminalException(String message) {
        super(message);
    }

    public RemoteCryptoTerminalException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteCryptoTerminalException(Throwable cause) {
        super(cause);
    }

    public RemoteCryptoTerminalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
