/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package cat.uvic.teknos.m09.remotecrypto;

import cat.uvic.teknos.m09.remotecrypto.HTTP.Exceptions.*;
import cat.uvic.teknos.m09.remotecrypto.HTTP.ServerHttpThread;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;

class ServerTest {
    private static ServerHttpThread http;
    @BeforeAll
    static void init(){
        http = new ServerHttpThread();
    }
    @Test
    void throwNotFoundException(){
        Assertions.assertThrows(NotFoundException.class,()->http.controller("randompath",""));
    }

    @Test
    void successfullResponse() throws NotFoundException, InternalServerErrorException, BadRequestException, HttpException {
        String response = http.controller("/cryptoutils/hash","text=ABC");
        Assertions.assertFalse(response.isEmpty());
    }

    @Test
    void badRequestException(){
        Assertions.assertThrows(BadRequestException.class,()->http.controller("/cryptoutils/hash",""));
    }

    @Test
    void connectionException(){
        Assertions.assertThrows(ConnectionException.class,()->http.initConnection());
    }
}
