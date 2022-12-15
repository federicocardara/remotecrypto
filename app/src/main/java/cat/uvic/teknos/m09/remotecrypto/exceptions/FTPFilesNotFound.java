package cat.uvic.teknos.m09.remotecrypto.exceptions;

public class FTPFilesNotFound extends Exception{

    public FTPFilesNotFound() {
        super("the directory not have files");
    }
}
