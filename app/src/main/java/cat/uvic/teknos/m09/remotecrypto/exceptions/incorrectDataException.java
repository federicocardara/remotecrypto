package cat.uvic.teknos.m09.remotecrypto.exceptions;

import java.io.IOException;

public class incorrectDataException extends RuntimeException {

    public void IncorrectDataException() {
        System.out.println("ERROR: INCORRECT DATA ENTER");
    }
}