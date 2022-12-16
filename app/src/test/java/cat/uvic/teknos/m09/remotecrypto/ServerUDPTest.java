package cat.uvic.teknos.m09.remotecrypto;


import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import com.google.common.io.ByteArrayDataInput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import cat.uvic.teknos.m09.remotecrypto.exceptions.NotFoundException;
import cat.uvic.teknos.m09.remotecrypto.threads.ServerUDPThread;
public class ServerUDPTest {

    @Test
    void partitionMessageSuccess(){
        byte[] arr1,arr2;
        String msg = "hola";
        arr1 = ServerUDPThread.getPacket1(msg.getBytes());
        arr2 = ServerUDPThread.getPacket2(msg.getBytes());

        var res = (new String(arr1, StandardCharsets.UTF_8)+new String(arr2,StandardCharsets.UTF_8));
        Assertions.assertEquals(msg, res);
    }
    
}
