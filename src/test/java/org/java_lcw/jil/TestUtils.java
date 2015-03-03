package org.java_lcw.jil;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class TestUtils {
  
  public static int getSize() {
    return getSize(1, 500);
  }
  public static int getSize(int max) {
    return getSize(1, max);
  }
  public static int getSize(int min, int max) {
    Random rnd = new Random();
    int w = 0;
    if (max <= Byte.MAX_VALUE) {
      while (w < min || w > max) {
        w = rnd.nextInt() & 0xff;
      }
    } else if (max <= Short.MAX_VALUE) {
      while (w < min || w > max) {
        w = rnd.nextInt() & 0xffff;
      }
    } else {
      while (w < min || w > max) {
        w = rnd.nextInt();
      }
    }
    return w;
  }
  
  public static String hashFile(String filename) throws NoSuchAlgorithmException, IOException {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    FileInputStream fis = new FileInputStream(filename);
    byte[] dataBytes = new byte[1024];
    
    int nread = 0; 
    while ((nread = fis.read(dataBytes)) != -1) {
      md.update(dataBytes, 0, nread);
    };
    byte[] mdbytes = md.digest();
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < mdbytes.length; i++) {
      sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
    }
    fis.close();
    return sb.toString();
  }
  
  public static String hashByteArray(byte[] data) throws NoSuchAlgorithmException, IOException {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    md.update(data);
    byte[] mdbytes = md.digest();
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < mdbytes.length; i++) {
      sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
    }
    return sb.toString();
  }
}
