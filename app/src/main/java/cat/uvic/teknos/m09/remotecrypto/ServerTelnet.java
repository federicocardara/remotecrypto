package cat.uvic.teknos.m09.remotecrypto;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executors;

public class ServerTelnet  {

    public static final int PORT = 50001;

    public static void main(String[] args) throws IOException {
        var server = new ServerSocket(PORT);

        var threadExecutor = Executors.newFixedThreadPool(3); // Up to 3 clients at the same time

        var client = server.accept(); // Server stopped till a client connection
        var thread = new ServerTelnetThread(client);
        threadExecutor.execute(new ServerTelnetThread(client)); // Runs run method from the clientThread
    }

}
