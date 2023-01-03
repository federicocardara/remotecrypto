package cat.uvic.teknos.m09.remotecrypto.exceptions;

public class RemoteCryptoHttpException extends RuntimeException{
    public RemoteCryptoHttpException() {
        super();
    }

    public RemoteCryptoHttpException(String message) {
        super(message);
    }

    public RemoteCryptoHttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteCryptoHttpException(Throwable cause) {
        super(cause);
    }

    protected RemoteCryptoHttpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
