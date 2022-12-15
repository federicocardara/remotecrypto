package cat.uvic.teknos.m09.remotecrypto.clients;

import cat.uvic.teknos.m09.elbouzzaouiabdelkarim.cryptoutils.CryptoUtils;
import cat.uvic.teknos.m09.elbouzzaouiabdelkarim.cryptoutils.Exceptions.MissingPropertiesException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.security.NoSuchAlgorithmException;

public class FTPCli {
        static CryptoUtils cr = new CryptoUtils();
    public static void main(String[] args) {
        try{
            run();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void run() throws IOException, MissingPropertiesException, NoSuchAlgorithmException {
        var ftpClient = new FTPClient();
        ftpClient.connect("localhost");

        ftpClient.login("admin","admin1234");
        ftpClient.changeWorkingDirectory("ftp");
        var f = ftpClient.listFiles("ftp");

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        for (FTPFile file: f){
            if(file.isFile()){
                ftpClient.retrieveFile(file.getName(),out);
                var encdrypted = cr.hash(out.toByteArray());
                ftpClient.storeFile(file.getName()+".hash",new ByteArrayInputStream((encdrypted.getHash()+"\n"+encdrypted.getAlgorithm()+"\n"+encdrypted.getSalt()).getBytes()));
            }
        }
        ftpClient.storeFile("test3.txt",FTPCli.class.getResourceAsStream("/test6.txt"));

        if(ftpClient.listFiles("test3.txt").length!=0){
            System.out.println("OKAY");
        }

        ftpClient.logout();
    }
}
