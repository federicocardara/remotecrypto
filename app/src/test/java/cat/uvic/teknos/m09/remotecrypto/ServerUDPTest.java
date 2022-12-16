package cat.uvic.teknos.m09.remotecrypto;


import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import cat.uvic.teknos.m09.remotecrypto.exceptions.NotFoundException;
import cat.uvic.teknos.m09.remotecrypto.threads.ServerUDPThread;
public class ServerUDPTest {

    @Test
    void UDPPacketsCorrect(){
        byte[] arr1,arr2,res;
        String msg = "hola";
        arr1 = ServerUDPThread.getPacket1(msg.getBytes());
        arr2 = ServerUDPThread.getPacket2(msg.getBytes());
        res = {arr1,arr2};

        Assertions.assertEquals(msg, msg);

        Assertions.assertThrows(NotFoundException.class,()->http.controller("randompath",""));
    }
    
}
