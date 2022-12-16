package cat.uvic.teknos.m09.remotecrypto.threads;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;

import cat.uvic.teknos.m09.elbouzzaouiabdelkarim.cryptoutils.CryptoUtils;
import cat.uvic.teknos.m09.elbouzzaouiabdelkarim.cryptoutils.Exceptions.MissingPropertiesException;
import cat.uvic.teknos.m09.elbouzzaouiabdelkarim.cryptoutils.dto.DigestResult;

public class ServerUDPThread implements Runnable {
    private CryptoUtils cr = new CryptoUtils();
    private DatagramSocket socket;
    private DatagramPacket packetRecieved;
    private DatagramPacket packetSended;
    private boolean running;
    private byte[] buf = new byte[256];

    public ServerUDPThread(DatagramSocket client, DatagramPacket packet) {
        this.socket = client;
        this.packetRecieved = packet;
    }

    @Override
    public void run() {
        try{
        DigestResult encr;
        InetAddress address = packetRecieved.getAddress();
        int port = packetRecieved.getPort();
          encr = cr.hash(packetRecieved.getData());
        byte[] hash = new byte[10];
        if(encr!=null)
            hash = encr.getHash();
            
            var packet1= getPacket1(hash);
            var packet2= getPacket2(hash);
            
            packetSended = new DatagramPacket(packet1, packet1.length, address, port);
            socket.send(packetSended);
            packetSended = new DatagramPacket(packet2, packet2.length, address, port);
            socket.send(packetSended);

        }catch(IOException e){

        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MissingPropertiesException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    public static byte[] getPacket1(byte[] mess){
        byte[] hash = new byte[mess.length/2];
        System.arraycopy(mess,0,hash,0,hash.length);
        return hash;
    }

    public static byte[] getPacket2(byte[] mess){
        byte[] hash = new byte[mess.length-mess.length/2]; 
        System.arraycopy(mess,mess.length-mess.length/2,mess.length/2,0,hash.length); 
        return hash;
    }
}
