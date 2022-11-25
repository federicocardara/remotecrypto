package cat.uvic.teknos.m09.remotecrypto;

import cat.uvic.teknos.m09.remotecrypto.thread.ServerHttpThread;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ServerHttp {
    private static RawHttp http = new RawHttp();
    public static int PORT = 50002;

    public ServerHttp() throws IOException {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        ServerSocket server = new ServerSocket(PORT);

        Socket client  = server.accept();
        pool.execute(new ServerHttpThread(client));

    }
}
