package cat.uvic.teknos.m09.remotecrypto.servers.thread;

import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.body.BodyReader;

import java.net.Socket;

public class ServerHttpThread implements Runnable {
    RawHttp http = new RawHttp();
    private Socket client;

    public ServerHttpThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {

            RawHttpRequest request = http.parseRequest(client.getInputStream());
            if (request.getUri().getPath().equals("/cryptoutils/hash")) {
                BodyReader reader = request.getBody().orElse(null);

                http.parseResponse("HTTP/1.1 200 OK\n" +
                        "Content-Type: text/plain\n" +
                        "Content-Length: 9\n" +
                        "\n" +
                        "something").writeTo(client.getOutputStream());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
