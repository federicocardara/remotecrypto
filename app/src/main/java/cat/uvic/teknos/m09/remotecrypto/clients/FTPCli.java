package cat.uvic.teknos.m09.remotecrypto.clients;

import org.apache.commons.net.ftp.FTPClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class FTPCli {

    public static void main(String[] args) {
        try{
            run();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void run() throws IOException {
        var ftpClient = new FTPClient();
        ftpClient.connect("localhost");

        ftpClient.login("admin","admin1234");
        ftpClient.changeWorkingDirectory("ftp");
        ftpClient.storeFile("test3.txt",FTPCli.class.getResourceAsStream("/test6.txt"));
        ftpClient.storeFile("test4.txt",new ByteArrayInputStream("test4".getBytes()));

        if(ftpClient.listFiles("test3.txt").length!=0){
            System.out.println("OKAY");
        }

        ftpClient.logout();
    }
}
