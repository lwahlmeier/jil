package me.lcw.jil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import me.lcw.jil.Color;
import me.lcw.jil.Image;
import me.lcw.jil.ImageException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public abstract class CutAndCopyTests {
  public static final String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
  public static final String filename2 = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
  public Image img;
  public Image subImg;
  public Image subImg2;
  
  @Before
  public abstract void start() throws Exception; 
  
  @After
  public void end() {
    img = null;
    subImg = null;
    subImg2 = null;
    System.gc();

  }
  
  @Test
  public void simpleCut() throws NoSuchAlgorithmException, IOException, ImageException {
    subImg = img.cut(0, 0, 200, 200);
    subImg2 = img.cut(200, 200, 200, 200);
    System.out.println(TestUtils.hashByteArray(subImg.getArray()));
    System.out.println(TestUtils.hashByteArray(subImg2.getArray()));
    assertEquals("138b9daeb8b89cd6dd1e38348bd590850001b8dbf0217bd23d1122bed2d7c24d", TestUtils.hashByteArray(subImg.getArray()));
    assertEquals("d1cea1907241e4f6e171929586622a73c9fe7a23f82f16ed36fd43ef4a7a874c", TestUtils.hashByteArray(subImg2.getArray()));
  }
  
  @Test
  public void cutOfCut() throws NoSuchAlgorithmException, IOException, ImageException {
    subImg = img.cut(0, 0, 800, 800);
    subImg2 = subImg.cut(200, 200, 200, 200);
    System.out.println(TestUtils.hashByteArray(subImg.getArray()));
    System.out.println(TestUtils.hashByteArray(subImg2.getArray()));
    assertEquals("659648a104be81536eba519d337ab39ce5cfbcf2b6bd8143d249f18df9b4ca86", TestUtils.hashByteArray(subImg.getArray()));
    assertEquals("d1cea1907241e4f6e171929586622a73c9fe7a23f82f16ed36fd43ef4a7a874c", TestUtils.hashByteArray(subImg2.getArray()));
  }
  
  @Test
  public void simpleCopy() {
    subImg = img.copy();
    subImg2 = subImg.cut(200, 200, 200, 200);
    assertTrue(Arrays.equals(img.getArray(), subImg.getArray()));
    img.getImageDrawer().drawLine(0, 0, 200, 200, Color.BLACK, 5, false);
    assertTrue(!Arrays.equals(img.getArray(), subImg.getArray()));
  }

}
