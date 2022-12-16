package cat.uvic.teknos.m09.remotecrypto.threads;

import cat.uvic.teknos.m09.elbouzzaouiabdelkarim.cryptoutils.CryptoUtils;
import cat.uvic.teknos.m09.elbouzzaouiabdelkarim.cryptoutils.Exceptions.MissingPropertiesException;
import cat.uvic.teknos.m09.elbouzzaouiabdelkarim.cryptoutils.PropertiesImp;
import cat.uvic.teknos.m09.elbouzzaouiabdelkarim.cryptoutils.dto.DigestResult;
import cat.uvic.teknos.m09.remotecrypto.exceptions.ConnectionException;
import cat.uvic.teknos.m09.remotecrypto.exceptions.FTPFileCreationException;
import cat.uvic.teknos.m09.remotecrypto.exceptions.FTPFilesNotFound;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.net.ConnectException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class FTPConnection extends PropertiesImp {

    private static final String PROPERTIES_PATH = "/ftp.properties";
    static CryptoUtils cryptoUtils = new CryptoUtils();
    private boolean stop = false;
    private FTPClient client;
    private Thread thread;

    public FTPConnection() {
        super(PROPERTIES_PATH);
    }
    /**
     * Initialize thread for in time of ftp.properties declared, create .hash files from files in ftp directory
     */
    public void init(){
        this.thread = new Thread(() -> {
            var timeApp = String.valueOf(this.props.get("time"));
            Date time;
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            while (!stop) {
                time = new Date();
                String strDate = formatter.format(time);
                if (timeApp.equals(strDate)) {
                    run();
                }
            }
        });

        this.thread.start();
    }

    private void run() {
        try {
            initConnection();
            var files = getFilesFromDirectory();
            generateHash(files);
            client.logout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    /** 
     * @throws ConnectException
     */
    private void initConnection() throws ConnectException {
        try {
            var url = String.valueOf(this.props.get("host_url"));
            var port = Integer.parseInt(String.valueOf(this.props.get("host_port")));
            var username = String.valueOf(this.props.get("username"));
            var password = String.valueOf(this.props.get("password"));

            this.client = new FTPClient();
            client.connect(url, port);
            client.login(username, password);
        } catch (IOException e) {
            throw new ConnectException();
        }
    }

    
    /** 
     * @param files
     * @throws MissingPropertiesException
     * @throws ConnectionException
     * @throws NoSuchAlgorithmException
     */
    private void generateHash(FTPFile[] files) throws MissingPropertiesException, ConnectionException, NoSuchAlgorithmException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            for (FTPFile file : files) {
                if (file.isFile()) {
                    client.retrieveFile(file.getName(), outputStream);
                    var encrypted = cryptoUtils.hash(outputStream.toByteArray());
                    var name = file.getName();
                    int index = name.indexOf('.');
                    if (index != -1) {
                        name = name.substring(0, index) + ".hash";
                    }
                    createFileBash(encrypted, name);
                }
            }
        } catch (MissingPropertiesException | NoSuchAlgorithmException e) {
            throw e;
        } catch (IOException e) {
            throw new ConnectionException();
        } catch (FTPFileCreationException e) {
            e.printStackTrace();
        }
    }

    
    /** 
     * @param encrypted
     * @param name
     * @throws FTPFileCreationException
     */
    private void createFileBash(DigestResult encrypted, String name) throws FTPFileCreationException {
        ByteArrayInputStream data = createContentFile(encrypted);
        try {
            client.storeFile(name, data);
        } catch (IOException e) {
            throw new FTPFileCreationException();
        }
    }

    public ByteArrayInputStream createContentFile(DigestResult encrypted){
        return new ByteArrayInputStream((new String(encrypted.getHash()) + "\n" + encrypted.getAlgorithm() + "\n" + new String(encrypted.getSalt())).getBytes());
    }

    
    /** 
     * @return FTPFile[]
     * @throws FTPFilesNotFound
     */
    private FTPFile[] getFilesFromDirectory() throws FTPFilesNotFound {
        try {
            var folder = String.valueOf(this.props.get("folder"));
            client.changeWorkingDirectory(folder);
            return client.listFiles();
        } catch (IOException e) {
            throw new FTPFilesNotFound();
        }
    }

    /**
     * stop the loop of  thread to stop it
     */
    public void stop() {
        this.stop = true;
    }

    
    /** 
     * @throws InterruptedException
     */
    public void join() throws InterruptedException {
        this.thread.join();
    }
}
