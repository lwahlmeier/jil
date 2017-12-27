package me.lcw.jil;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class TestUtils {
  private static volatile JilImage rgbImage = null;
  private static volatile JilImage rgbaImage = null;
  private static volatile JilImage greyImage = null;
  
  private TestUtils(){};
  
  public static JilImage GreyImageGenerator() {
    if(greyImage == null) {
      JilImage img = JilImage.create(BaseImage.ImageMode.GREY8, 480, 270);
      for(int i=0; i<1080; i++) {
        double pct = i/1080.0;
        byte tmp = (byte)(256*pct);
        img.draw().line(0, i, 480, i, Color.fromGreyByte(tmp), 0, false);
      }
    }
    return greyImage.copy();
  }
  public static JilImage RGBAImageGenerator() {
    if(rgbaImage == null) {
      JilImage img = JilImage.create(BaseImage.ImageMode.RGBA32, 480, 240);
      img.fillImageWithColor(Color.ALPHA);
      Color c = Color.RED.changeAlpha((byte) 50);
      img.draw().rect(0, 0, 480, 20, c, 1, true);
      c = Color.RED.changeAlpha((byte) 100);
      img.draw().rect(0, 20, 480, 20, c, 1, true);
      c = Color.RED.changeAlpha((byte) 200);
      img.draw().rect(0, 20*2, 480, 20, c, 1, true);
      img.draw().rect(0, 20*3, 480, 20, Color.RED, 1, true);
      
      c = Color.GREEN.changeAlpha((byte) 50);
      img.draw().rect(0, 20*4, 480, 20, c, 1, true);
      c = Color.GREEN.changeAlpha((byte) 100);
      img.draw().rect(0, 20*5, 480, 20, c, 1, true);
      c = Color.GREEN.changeAlpha((byte) 200);
      img.draw().rect(0, 20*6, 480, 20, c, 1, true);
      img.draw().rect(0, 20*7, 480, 20, Color.GREEN, 1, true);
      
      c = Color.BLUE.changeAlpha((byte) 50);
      img.draw().rect(0, 20*8, 480, 20, c, 1, true);
      c = Color.BLUE.changeAlpha((byte) 100);
      img.draw().rect(0, 20*9, 480, 20, c, 1, true);
      c = Color.BLUE.changeAlpha((byte) 200);
      img.draw().rect(0, 20*10, 480, 20, c, 1, true);
      img.draw().rect(0, 20*11, 480, 20, Color.BLUE, 1, true);
      rgbaImage = img;
    }
    return rgbaImage.copy();
  }
  
  public static JilImage RGBImageGenerator() {
    if(rgbImage == null) {
      JilImage img = JilImage.create(BaseImage.ImageMode.RGB24, 480, 270);
      for(int i=0; i<img.getHeight(); i++) {
        double pct = i/(double)img.getHeight();
        byte r = (byte)(0xff * (pct*2));
        byte g = (byte)(0xff* (pct*1));
        byte b = (byte)(0xff -(0xff* (pct*2)));
        Color c = Color.fromRGBBytes(r,g,b);
        img.draw().line(0, i, img.getWidth(), i, c, 0, false);
      }
      rgbImage = img;
    }
    return rgbImage.copy();
  }
  
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
