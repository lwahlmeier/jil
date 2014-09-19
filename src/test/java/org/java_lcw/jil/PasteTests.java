package org.java_lcw.jil;

import java.util.Random;

import org.junit.After;
import org.junit.Test;


public abstract class PasteTests {
  Random rnd = new Random();
  Image img;
  Image subImg;
  Image subImg2;
  boolean alpha = false;
  String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
  String filename2 = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
  
  public abstract void start() throws Exception;
  
  @After
  public void end() {
    System.gc();
  }
  
  public static void addAlphaToImage(byte alpha, Image img) {
    for(int y=0; y<img.getHeight(); y++) {
      for(int x=0; x<img.getWidth(); x++){
        Color q = img.getPixel(x, y);
        q.setAlpha((byte)100);
        img.setPixel(x, y, q);
      }
    }
  }
  
  @Test
  public void normal() throws Exception {
    img.paste(100, 100, subImg, alpha);
  }
  
  @Test
  public void biggerPasteImage() throws Exception {
    img.paste(-1280, -50, subImg2, alpha);
  }
  
  @Test
  public void underShootY() throws Exception {
    img.paste(100, -100, subImg, alpha);
  }
  
  @Test
  public void underShootX() throws Exception {
    img.paste(-100, 100, subImg, alpha);
  }
  
  @Test
  public void overShootX() throws Exception {
    img.paste(1100, 100, subImg, alpha);
  }
  
  @Test
  public void overShootY() throws Exception {
    img.paste(100, 750, subImg, alpha);
  }
  
  @Test
  public void overShootBoth() throws Exception {
    img.paste(1100, 750, subImg, alpha);
  }
  
  @Test
  public void underShootBoth() throws Exception {
    img.paste(-100, -100, subImg, alpha);
  }
  
  
  

}
