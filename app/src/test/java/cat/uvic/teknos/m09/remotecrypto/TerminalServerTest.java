package cat.uvic.teknos.m09.remotecrypto;

import cat.uvic.teknos.m09.polsane.cryptoutils.CryptoUtils;
import cat.uvic.teknos.m09.remotecrypto.servers.RemoteCrypto;
import cat.uvic.teknos.m09.remotecrypto.servers.HttpServer;
import cat.uvic.teknos.m09.remotecrypto.servers.TerminalServer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TerminalServerTest {
    @Test void turnOnServers(){
        RemoteCrypto clusterServer=new RemoteCrypto(50001,50002);
        clusterServer.turnOnServers();
    }
    @Test void turnOnHttpServer(){
        HttpServer httpServer=new HttpServer(50001);
        httpServer.turnOnServer();
    }
    @Test void turnOnTerminalServer(){
        TerminalServer terminalServer=new TerminalServer(50002);
        terminalServer.turnOnServer();
    }
    @Test void crypto(){
        String str="1234";
        var hash=CryptoUtils.hash(str.getBytes());
        var encrByteArr=CryptoUtils.encrypt(str.getBytes(),"123");
        var decrText=new String(CryptoUtils.decrypt(encrByteArr,"123"));
        assertTrue(decrText.equals(str));
        assertNotNull(hash!=null);
        System.out.println(new String(hash.getHash()));
    }
}
