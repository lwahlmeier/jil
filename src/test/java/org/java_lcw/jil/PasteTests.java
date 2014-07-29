package org.java_lcw.jil;

import static org.junit.Assert.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.java_lcw.jil.Image.ImageException;
import org.junit.Before;
import org.junit.Test;


public class PasteTests {
  Random rnd = new Random();
  Image img;
  Image subImg;
  String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
  String filename2 = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
  
  @Before
  public void start() throws ImageException, IOException{
    img = Image.open(filename);
    subImg = Image.open(filename2);
    subImg = subImg.resize(300, 300);
  }
  
  @Test
  public void underShootYNoAlpha() throws ImageException, IOException, NoSuchAlgorithmException {
    img.paste(100, -100, subImg);
    assertEquals("486b4fd69c2a51c23363a9197b82a7090c3fa382fc63ee33e425ad9f19eab6e9", ImageTest.hashByteArray(img.toArray()));
  }
  
  @Test
  public void underShootXNoAlpha() throws ImageException, IOException, NoSuchAlgorithmException {
    img.paste(-100, 100, subImg);
    assertEquals("c22de96063def07a302961da7b2601a27c2b74510ec3cd11edc2531257a48f2e", ImageTest.hashByteArray(img.toArray()));
  }
  
  @Test
  public void overShootXNoAlpha() throws ImageException, IOException, NoSuchAlgorithmException {
    img.paste(1100, 100, subImg);
    assertEquals("cb3705a2713948d5b52c47ab916f7913937842d21aaf474fbc52b1cba28e6212", ImageTest.hashByteArray(img.toArray()));
  }
  
  @Test
  public void overShootYNoAlpha() throws ImageException, IOException, NoSuchAlgorithmException {
    img.paste(100, 750, subImg);
    assertEquals("d415660ef3b3df478d2fd76b8301d7ccdad06957d617f15561378b1e7e191460", ImageTest.hashByteArray(img.toArray()));
  }
  
  @Test
  public void overShootBothNoAlpha() throws ImageException, IOException, NoSuchAlgorithmException {
    img.paste(1100, 750, subImg);
    assertEquals("5baed67a9e93e74e31f0380e8906d280271a3596dcb234c298de27e1f2b10563", ImageTest.hashByteArray(img.toArray()));
  }
  
  @Test
  public void underShootBothNoAlpha() throws ImageException, IOException, NoSuchAlgorithmException {
    img.paste(-100, -100, subImg);
    assertEquals("8d57db74f7b80be234de72f3e8f36c5c4f60ebcd8f0720fa169d0191cd2fbd7e", ImageTest.hashByteArray(img.toArray()));
  }
  
  
  @Test
  public void underShootBothAlpha() throws ImageException, IOException, NoSuchAlgorithmException {
    for(int y=0; y<subImg.getHeight(); y++) {
      for(int x=0; x<subImg.getWidth(); x++){
        Color q = subImg.getPixel(x, y);
        q.setAlpha((byte)100);
        subImg.setPixel(x, y, q);
      }
    }
    img.paste(-100, -100, subImg, true);
    assertEquals("cb56c3396321454226285cbccd4e4a6c84061f6621bf45d3557150d8adc6cc69", ImageTest.hashByteArray(img.toArray()));
  }
  
  @Test
  public void NormalAlpha() throws ImageException, IOException, NoSuchAlgorithmException {
    for(int y=0; y<subImg.getHeight(); y++) {
      for(int x=0; x<subImg.getWidth(); x++){
        Color q = subImg.getPixel(x, y);
        q.setAlpha((byte)100);
        subImg.setPixel(x, y, q);
      }
    }
    img.paste(100, 100, subImg, true);
    assertEquals("e0e13ce55f469797062fcd0ed251991e660974a91068c27352cd49db73cec6d6", ImageTest.hashByteArray(img.toArray()));
  }
  
  
  

}
