package cat.uvic.teknos.m09.remotecrypto.exceptions;

public class RemoteCryptoException extends RuntimeException{
    public RemoteCryptoException() {
    }

    public RemoteCryptoException(String message) {
        super(message);
    }

    public RemoteCryptoException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteCryptoException(Throwable cause) {
        super(cause);
    }

    public RemoteCryptoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
