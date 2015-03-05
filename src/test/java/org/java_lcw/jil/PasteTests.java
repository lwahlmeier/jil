package org.java_lcw.jil;

import java.util.Random;

import org.junit.After;
import org.junit.Test;


public abstract class PasteTests {
  public Random rnd = new Random();
  public Image img;
  public Image subImg;
  public Image subImg2;
  public boolean alpha = false;
  public String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
  public String filename2 = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
  
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
    if(alpha) {
      img.merge(100, 100, subImg);
    } else {
      img.paste(100, 100, subImg);
    }
  }
  
  @Test
  public void biggerPasteImage() throws Exception {
    if(alpha) {
      img.merge(-1280, -50, subImg2);
    } else {
      img.paste(-1280, -50, subImg2);
    }
  }
  
  @Test
  public void underShootY() throws Exception {
    if(alpha) {
      img.merge(100, -100, subImg);
    } else {
      img.paste(100, -100, subImg);
    }
  }
  
  @Test
  public void underShootX() throws Exception {
    if(alpha) {
      img.merge(-100, 100, subImg);
    } else {
      img.paste(-100, 100, subImg);
    }
  }
  
  @Test
  public void overShootX() throws Exception {
    if(alpha) {
      img.merge(1100, 100, subImg);
    } else {
      img.paste(1100, 100, subImg);
    }
  }
  
  @Test
  public void overShootY() throws Exception {
    if(alpha) {
      img.merge(100, 750, subImg);
    } else {
      img.paste(100, 750, subImg);
    }
  }
  
  @Test
  public void overShootBoth() throws Exception {
    if(alpha) {
      img.merge(1100, 750, subImg);
    } else {
      img.paste(1100, 750, subImg);
    }
  }
  
  @Test
  public void underShootBoth() throws Exception {
    if(alpha) {
      img.merge(-100, -100, subImg);
    } else {
      img.paste(-100, -100, subImg);
    }
  }
  
  
  

}
