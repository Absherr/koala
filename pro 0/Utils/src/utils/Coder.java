package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author ADAM
 */
public class Coder 
{
     public static synchronized String code(String pmPass) throws NoSuchAlgorithmException { 
        MessageDigest lvMd = MessageDigest.getInstance("SHA-1");
        lvMd.update(pmPass.getBytes());
 
        byte byteData[] = lvMd.digest();
 
        StringBuffer lvSb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         lvSb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return lvSb.toString();
    }
}
