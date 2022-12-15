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
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Properties;

public class ClientFTP {
    static CryptoUtils cu = new CryptoUtils();
    private boolean stop = false;
    private Thread thread;
    public static void main(String[] args) {
        try{
            run();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void FTPConnection(){
        try{
            this.thread = new Thread(() -> {
                try {
                    Properties properties=new Properties();
                    properties.load(ClientFTP.class.getResourceAsStream("/ftpInfo.properties"));
                    var timeApp=String.valueOf(properties.get("time"));
                    LocalDateTime time;
                    SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
                    while(!stop){
                        time= LocalDateTime.now();
                        String strDate=formatter.format(time);
                        if(timeApp.equals(strDate)){
                            run();
                        }
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (MissingPropertiesException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            });

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private static void run() throws IOException, MissingPropertiesException, NoSuchAlgorithmException {
        Properties properties=new Properties();
        properties.load(ClientFTP.class.getResourceAsStream("/ftp.properties"));
        String url=String.valueOf(properties.get("ftp_url"));
        int port=Integer.parseInt(String.valueOf(properties.get("ftp_port")));
        String userName=String.valueOf(properties.get("username"));
        String password=String.valueOf(properties.get("password"));
        String directory=String.valueOf(properties.get("folder"));

        FTPClient client = new FTPClient();
        client.connect(url,port);
        client.login(userName,password);
        client.changeWorkingDirectory(directory);

        FTPFile[] files = client.listFiles("ftp");
        ByteArrayOutputStream exitFiles = new ByteArrayOutputStream();

        for (FTPFile file: files){
            if(file.isFile()){
                client.retrieveFile(file.getName(),exitFiles);
                DigestResult encryptedFile = cu.hash(exitFiles.toByteArray());

                String fileName=file.getName();
                int fileIndex = fileName.indexOf('.');
                if(fileIndex!=-1){
                    fileName = fileName.substring(0,fileIndex)+".hash";
                }

                ByteArrayInputStream data = new ByteArrayInputStream((encryptedFile.getHash()+"\n"+
                        encryptedFile.getAlgorithm()+"\n"+encryptedFile.getSalt()).getBytes());

                client.storeFile(fileName, data);
            }
        }
        client.logout();
    }

    public void join() throws InterruptedException {
        this.thread.join();
    }
}
