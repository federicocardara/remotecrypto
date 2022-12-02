package cat.uvic.teknos.m09.remotecrypto;

import org.apache.commons.net.ftp.FTPClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class FtpClientTest {
    public static void main(String[] args) throws IOException {
        var ftpClient = new FTPClient();

        ftpClient.connect("localhost");
        ftpClient.login("user", "user");

        ftpClient.changeWorkingDirectory("dir1");

        ftpClient.storeFile("test3.txt", FtpClientTest.class.getResourceAsStream("/test3.txt"));
        ftpClient.storeFile("test4.txt",new ByteArrayInputStream("test4.txt".getBytes()));

        if (ftpClient.listFiles("test3.txt").length == 1){
            System.out.println("test3.txt stored in the server");
        }

        ftpClient.logout();

    }
}
