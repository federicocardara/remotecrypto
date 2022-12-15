package cat.uvic.teknos.m09.remotecrypto.servers.thread;

import cat.uvic.teknos.m09.elbouzzaouiabdelkarim.cryptoutils.CryptoUtils;
import cat.uvic.teknos.m09.elbouzzaouiabdelkarim.cryptoutils.Exceptions.MissingPropertiesException;
import cat.uvic.teknos.m09.elbouzzaouiabdelkarim.cryptoutils.PropertiesImp;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.net.ConnectException;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Properties;

public class FTPConnection extends PropertiesImp {

    private static final String PROPERTIES_PATH ="/ftp.properties";
    static CryptoUtils cryptoUtils=new CryptoUtils();
    private boolean stop = false;
    private FTPClient client;
    private Thread thread;

    public  FTPConnection() {
        super(PROPERTIES_PATH);
      try{
          this.thread = new Thread(() -> {
              try {
                  var timeApp=String.valueOf(this.props.get("time"));
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

        initConnection();

        Properties properties=new Properties();
        properties.load(FTPConnection.class.getResourceAsStream("/ftp.properties"));

        var folder=String.valueOf(properties.get("folder"));




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

    private void initConnection() throws ConnectException {
        try{

            var url=String.valueOf(this.props.get("ftp_url"));
            var port=Integer.parseInt(String.valueOf(this.props.get("ftp_port")));
            var username=String.valueOf(this.props.get("username"));
            var password=String.valueOf(this.props.get("password"));

            this.client= new FTPClient();
            client.connect(url,port);
            client.login(username,password);
        } catch (IOException e) {
            throw new ConnectException();
        }


    }

    public void stop(){
        this.stop =true;
    }

    public void join() throws InterruptedException {
        this.thread.join();
    }
}
