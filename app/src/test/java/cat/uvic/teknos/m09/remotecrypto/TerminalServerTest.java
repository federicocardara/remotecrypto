/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package cat.uvic.teknos.m09.remotecrypto;

import cat.uvic.teknos.m09.remotecrypto.servers.ClusterServer;
import cat.uvic.teknos.m09.remotecrypto.servers.TerminalServer;
import org.junit.jupiter.api.Test;

class TerminalServerTest {
    @Test void turnOnServers() {
        ClusterServer clusterServer=new ClusterServer(5001,5002);
        clusterServer.turnOnServers();
    }
}
