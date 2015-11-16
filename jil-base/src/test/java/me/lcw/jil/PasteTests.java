package me.lcw.jil;

import java.util.Random;

import org.junit.After;
import org.junit.Test;


public abstract class PasteTests {
  public Random rnd = new Random();
  public BaseImage img;
  public BaseImage subImg;
  public BaseImage subImg2;
  public boolean alpha = false;
  
  public abstract void start() throws Exception;
  
  @After
  public void end() {
    System.gc();
  }
  
  public static void addAlphaToImage(byte alpha, BaseImage img) {
    for(int y=0; y<img.getHeight(); y++) {
      for(int x=0; x<img.getWidth(); x++){
        Color q = img.getPixel(x, y);
        img.setPixel(x, y, q.changeAlpha((byte)100));
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
      img.merge(img.getWidth()-100, 100, subImg);
    } else {
      img.paste(img.getWidth()-100, 100, subImg);
    }
  }
  
  @Test
  public void overShootY() throws Exception {
    if(alpha) {
      img.merge(100, img.getHeight()-100, subImg);
    } else {
      img.paste(100, img.getHeight()-100, subImg);
    }
  }
  
  @Test
  public void overShootBoth() throws Exception {
    if(alpha) {
      img.merge(img.getWidth()-100, img.getHeight()-100, subImg);
    } else {
      img.paste(img.getWidth()-100, img.getHeight()-100, subImg);
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
