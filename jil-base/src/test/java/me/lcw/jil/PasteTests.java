package me.lcw.jil;

import static org.junit.Assert.assertEquals;

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
      assertEquals("28fdc4a5c457dc88d76e3285cc89f73514fdccc5926bce23b15b5c3c8f1a075d", TestUtils.hashByteArray(img.getArray()));
    }
  }
  
  @Test
  public void biggerPasteImage() throws Exception {
    if(alpha) {
      img.merge(-10, -50, subImg2);
    } else {
      img.paste(-10, -50, subImg2);
      assertEquals("b08e714cf0e740e6c8dabd5dbabb5a981beab57b50aff231818c211516aacfc0", TestUtils.hashByteArray(img.getArray()));
    }
  }
  
  @Test
  public void underShootY() throws Exception {
    if(alpha) {
      img.merge(100, -100, subImg);
    } else {
      img.paste(100, -100, subImg);
      assertEquals("319a27a933dee4637afad30a0001490f0bf4f461a00bbc5546a0ef60e3278c9e", TestUtils.hashByteArray(img.getArray()));
    }
  }
  
  @Test
  public void underShootX() throws Exception {
    if(alpha) {
      img.merge(-100, 100, subImg);
    } else {
      img.paste(-100, 100, subImg);
      assertEquals("559a6fce0ef7868b47b222db8dd460a9fe6bfcd60f14d72918eec60476924669", TestUtils.hashByteArray(img.getArray()));
    }
  }
  
  @Test
  public void overShootX() throws Exception {
    if(alpha) {
      img.merge(img.getWidth()-100, 100, subImg);
    } else {
      img.paste(img.getWidth()-100, 100, subImg);
      assertEquals("939ae155c8fcd4ad7f90534049e3a13e79bc17db8cfec29df3244bf97c46b8d2", TestUtils.hashByteArray(img.getArray()));
    }
  }
  
  @Test
  public void overShootY() throws Exception {
    if(alpha) {
      img.merge(100, img.getHeight()-100, subImg);
    } else {
      img.paste(100, img.getHeight()-100, subImg);
      assertEquals("2266a51b0c069151f61d5b55e522039b1730c83a4916d06f0cf818e69710a56a", TestUtils.hashByteArray(img.getArray()));
    }
  }
  
  @Test
  public void overShootBoth() throws Exception {
    if(alpha) {
      img.merge(img.getWidth()-100, img.getHeight()-100, subImg);
    } else {
      img.paste(img.getWidth()-100, img.getHeight()-100, subImg);
      assertEquals("84a4ec2dc88fe3a247970002dd0bd50384b30e39da992e89d743901c92d096e9", TestUtils.hashByteArray(img.getArray()));
    }
  }
  
  @Test
  public void underShootBoth() throws Exception {
    if(alpha) {
      img.merge(-100, -100, subImg);
    } else {
      img.paste(-100, -100, subImg);
      assertEquals("c401aee186d6ff5b2032e0da9bf2270ef07d0c30f80fc6a78caac3c1cd4c2643", TestUtils.hashByteArray(img.getArray()));
    }
  }
  
  
  

}
