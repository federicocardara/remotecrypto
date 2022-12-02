package cat.uvic.teknos.m09.remotecrypto;

import cat.uvic.teknos.m09.remotecrypto.servers.ServerHttp;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;

import java.io.IOException;
import java.net.Socket;

public class ServerHttpTest {

    @BeforeAll
    static void startServer(){
        ServerInit.main();
    }

    @Test
    void rawHttpTest() throws IOException {
        RawHttp rawHttp = new RawHttp();

        RawHttpRequest request = rawHttp.parseRequest(
                "GET /cryptoutils/hash?text=ABC HTTP/1.1\r\n" +
                        "User-Agent: curl/7.16.3 libcurl/7.16.3 OpenSSL/0.9.7l zlib/1.2.3\r\n" +
                        "Host: localhost:50002\r\n" +
                        "Accept-Language: en, mi\n+"+
                        "");


        Socket socket = new Socket("localhost", ServerHttp.PORT);
        request.writeTo(socket.getOutputStream());

        RawHttpResponse<?> response = rawHttp.parseResponse(socket.getInputStream());

// call "eagerly()" in order to download the body
        System.out.println(response.eagerly());


    }

    @AfterAll
    private static void stopServer(){
        ServerInit.join();
    }

}
