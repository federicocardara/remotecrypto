package cat.uvic.teknos.m09.remotecrypto.FTP;

import cat.uvic.teknos.m09.elbouzzaouiabdelkarim.cryptoutils.CryptoUtils;
import cat.uvic.teknos.m09.elbouzzaouiabdelkarim.cryptoutils.Exceptions.MissingPropertiesException;
import cat.uvic.teknos.m09.remotecrypto.ConnectionsViaTelnet.Client;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Properties;

public class FTPConnection {
    static CryptoUtils cryptoUtils=new CryptoUtils();
    private boolean stop = false;
    private Thread thread;
    public  void FTPConnection() {
      try{
          this.thread = new Thread(() -> {
              try {
                  Properties properties=new Properties();
                  properties.load(FTPConnection.class.getResourceAsStream("/ftp.properties"));
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

    private void run() throws IOException, MissingPropertiesException, NoSuchAlgorithmException {
        FTPClient client= new FTPClient();
        Properties properties=new Properties();
        properties.load(FTPConnection.class.getResourceAsStream("/ftp.properties"));
        var url=String.valueOf(properties.get("ftp_url"));
        var port=Integer.parseInt(String.valueOf(properties.get("ftp_port")));
        var username=String.valueOf(properties.get("username"));
        var password=String.valueOf(properties.get("password"));
        var folder=String.valueOf(properties.get("folder"));

        client.connect(url,port);

        client.login(username,password);


        client.changeWorkingDirectory(folder);
        var files=client.listFiles();

        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        for (FTPFile file: files) {
            if(file.isFile()){
                client.retrieveFile(file.getName(),outputStream);
                var encrypted=cryptoUtils.hash(outputStream.toByteArray());
                var name =file.getName();
                int index = name.indexOf('.');
                if(index!=-1){
                    name = name.substring(0,index)+".hash";
                }
                ByteArrayInputStream data = new ByteArrayInputStream((encrypted.getHash()+"\n"+encrypted.getAlgorithm()+"\n"+encrypted.getSalt()).getBytes());
                client.storeFile(name, data);

            }
        }
        client.logout();

    }

    public void stop(){
        this.stop =true;
    }

    public void join() throws InterruptedException {
        this.thread.join();
    }
}
