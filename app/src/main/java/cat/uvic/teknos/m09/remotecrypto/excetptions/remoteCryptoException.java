package cat.uvic.teknos.m09.remotecrypto.excetptions;

public class remoteCryptoException extends RuntimeException {

    public remoteCryptoException() {
    }

    public remoteCryptoException(String message) {
        super(message);
    }

    public remoteCryptoException(String message, Throwable cause) {
        super(message, cause);
    }

    public remoteCryptoException(Throwable cause) {
        super(cause);
    }

    public remoteCryptoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
