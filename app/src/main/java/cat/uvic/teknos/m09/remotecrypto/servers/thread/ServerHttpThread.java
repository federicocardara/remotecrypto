package cat.uvic.teknos.m09.remotecrypto.servers.thread;

import cat.uvic.teknos.m09.elbouzzaouiabdelkarim.cryptoutils.CryptoUtils;
import cat.uvic.teknos.m09.elbouzzaouiabdelkarim.cryptoutils.dto.DigestResult;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.body.BodyReader;

import java.net.Socket;

public class ServerHttpThread implements Runnable {
    CryptoUtils cryptoUtils = new CryptoUtils();
    RawHttp http = new RawHttp();
    private Socket client;

    public ServerHttpThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {

            RawHttpRequest request = http.parseRequest(client.getInputStream());
            if (request.
                    getUri().getPath().contains("/cryptoutils/hash")) {
                String query = request.getUri().getQuery();
                System.out.println(query);
                DigestResult digest = cryptoUtils.hash(query.split("=")[1].getBytes());
                System.out.println(new String(digest.getHash()));
                String str = digest.getHash().toString();
                http.parseResponse("HTTP/1.1 200 OK\n" +
                        "Content-Type: text/plain\n" +
                        "Content-Length: 9\n" +
                        "\n" +
                        str
                        ).writeTo(client.getOutputStream());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
