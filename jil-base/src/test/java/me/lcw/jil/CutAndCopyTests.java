package me.lcw.jil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public abstract class CutAndCopyTests {
  public BaseImage img;
  public BaseImage subImg;
  public BaseImage subImg2;
  
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
    subImg = img.cut(0, 0, 20, 20);
    subImg2 = img.cut(50, 50, 20, 20);
    System.out.println(TestUtils.hashByteArray(subImg.getArray()));
    System.out.println(TestUtils.hashByteArray(subImg2.getArray()));
    assertEquals("cccd2030459d1dfd6a29d6e27277e0a1d65b21b20c6bb7cf5b29c66c6e7e0c69", TestUtils.hashByteArray(subImg.getArray()));
    assertEquals("f9fefc2bbcebd298d5aef14d99c60a1142afa69bd462718bf409d3a2c245a36d", TestUtils.hashByteArray(subImg2.getArray()));
  }
  
  @Test
  public void cutOfCut() throws NoSuchAlgorithmException, IOException, ImageException {
    subImg = img.cut(0, 0, 80, 80);
    subImg2 = subImg.cut(20, 20, 20, 20);
    assertEquals("801913350e88f77ad24ec3c18369112352b73d65bea1b9a4205d3a74635355f7", TestUtils.hashByteArray(subImg.getArray()));
    assertEquals("37aed22e27d9707f539398f5b14a39ad7a2e235fa8313ff5a91779d44460bd99", TestUtils.hashByteArray(subImg2.getArray()));
  }
  
  @Test
  public void simpleCopy() {
    subImg = img.copy();
    subImg2 = subImg.cut(100, 100, 100, 100);
    assertTrue(Arrays.equals(img.getArray(), subImg.getArray()));
    img.draw().line(0, 0, 200, 200, Color.BLACK, 5, false);
    assertTrue(!Arrays.equals(img.getArray(), subImg.getArray()));
  }

}
