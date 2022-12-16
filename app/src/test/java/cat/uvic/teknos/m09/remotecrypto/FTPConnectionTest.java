package cat.uvic.teknos.m09.remotecrypto;

import java.io.ByteArrayInputStream;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import cat.uvic.teknos.m09.elbouzzaouiabdelkarim.cryptoutils.CryptoUtils;
import cat.uvic.teknos.m09.elbouzzaouiabdelkarim.cryptoutils.Exceptions.MissingPropertiesException;
import cat.uvic.teknos.m09.elbouzzaouiabdelkarim.cryptoutils.dto.DigestResult;
import cat.uvic.teknos.m09.remotecrypto.threads.FTPConnection;

public class FTPConnectionTest {

    @Test
    void EncryptionSuccess() throws NoSuchAlgorithmException, MissingPropertiesException{
        CryptoUtils cr = new CryptoUtils();
        
        DigestResult dg = cr.hash("hola".getBytes());
        FTPConnection con = new FTPConnection();
        ByteArrayInputStream a = con.createContentFile(dg);

        var res = new ByteArrayInputStream((new String(dg.getHash()) + "\n" + dg.getAlgorithm() + "\n" + new String(dg.getSalt())).getBytes());
        Assertions.assertEquals(res,a);
    }
    
}
