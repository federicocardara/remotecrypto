package cat.uvic.teknos.m09.remotecrypto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class ServerTest {
    @Test void appHasAGreeting() {
        Server classUnderTest = new Server();
        assertNotNull(classUnderTest.toString(), "app should have a greeting");
    }
}
