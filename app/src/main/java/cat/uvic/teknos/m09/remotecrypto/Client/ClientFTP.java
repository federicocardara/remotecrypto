package cat.uvic.teknos.m09.remotecrypto.Client;

import cat.uvic.teknos.m09.elbouzzaouiabdelkarim.cryptoutils.CryptoUtils;
import cat.uvic.teknos.m09.elbouzzaouiabdelkarim.cryptoutils.Exceptions.MissingPropertiesException;
import cat.uvic.teknos.m09.elbouzzaouiabdelkarim.cryptoutils.dto.DigestResult;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class ClientFTP {
    static CryptoUtils cu = new CryptoUtils();
    public static void main(String[] args) {
        try{
            run();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void run() throws IOException, MissingPropertiesException, NoSuchAlgorithmException {
        FTPClient client = new FTPClient();
        client.connect("localhost");

        client.login("admin","admin1234");
        client.changeWorkingDirectory("ftp");
        FTPFile[] files = client.listFiles("ftp");

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        for (FTPFile file: files){
            if(file.isFile()){
                client.retrieveFile(file.getName(),out);
                DigestResult encryptedFile = cu.hash(out.toByteArray());
                client.storeFile(file.getName()+".hash",new ByteArrayInputStream((encryptedFile.getHash()+"\n"+encryptedFile.getAlgorithm()+"\n"+encryptedFile.getSalt()).getBytes()));
            }
        }
        client.storeFile("test3.txt",ClientFTP.class.getResourceAsStream("/test6.txt"));

        if(client.listFiles("test3.txt").length!=0){
            System.out.println("OKAY");
        }
        client.logout();
    }
}
