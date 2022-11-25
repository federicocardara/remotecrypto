package cat.uvic.teknos.m09.remotecrypto;

import org.junit.jupiter.api.Test;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;

import java.io.IOException;
import java.net.Socket;

public class ServerHttpTest {
    @Test void rawHttpTest() throws IOException {
        RawHttp rawHttp = new RawHttp();

        RawHttpRequest request = rawHttp.parseRequest(
                "GET /localhost:50002/cryptoutils/hash?text=ABC" +
                        "User-Agent: Carlota" +
                        "Host: localhost" +
                        "Accept-Language: en, mi");
        Socket socket = new Socket("localhost", 5002);
        request.writeTo(socket.getOutputStream());

    }

}
