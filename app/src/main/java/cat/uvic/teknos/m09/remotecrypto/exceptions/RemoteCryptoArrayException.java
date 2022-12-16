package cat.uvic.teknos.m09.remotecrypto.exceptions;

import rawhttp.core.RawHttp;
import java.io.IOException;
import java.io.OutputStream;

public class RemoteCryptoArrayException extends ArrayIndexOutOfBoundsException {
    public RemoteCryptoArrayException(String[] query, RawHttp http, OutputStream outputStream) {
        if (query.length==0) {
            try {
                http.parseResponse("HTTP/1.1 400 Bad Request\n" +
                        "Content-Type: text/plain\n" +
                        "Content-Length: 0\n" +
                        "\n").writeTo(outputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}