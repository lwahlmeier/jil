package me.lcw.jil.awt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import me.lcw.jil.Color;
import me.lcw.jil.BaseImage;
import me.lcw.jil.ImageException;
import me.lcw.jil.TestUtils;

import org.junit.Test;

public class AWTImageTests {
  @Test(expected=IOException.class)
  public void exceptionTest() throws ImageException, IOException {
    AWTImage.open("/bad/path/to/open.png");
  }

  @Test(expected=FileNotFoundException.class)
  public void exceptionTest3() throws ImageException, IOException {
    AWTImage.open("TEST.BLAH");
  }
  
  @Test
  public void checkCachedBA() {
    AWTImage img = AWTImage.fromBaseImage(TestUtils.RGBAImageGenerator());
    img.enableBAcache();
    byte[] oba = img.getByteArray();
    img.draw().line(0, 0, 100, 100, Color.RED, 10, true);
    assertFalse(Arrays.equals(oba, img.getByteArray()));
    assertTrue(Arrays.equals(img.getByteArray(), img.getByteArray()));
    assertTrue(img.getByteArray() == img.getByteArray());
    img.disableBAcache();
    assertFalse(img.getByteArray() == img.getByteArray());
  }
  
  @Test
  public void modeChageTest() throws ImageException, IOException {
    AWTImage img = AWTImage.fromBaseImage(TestUtils.RGBAImageGenerator());
    AWTImage imgL = img.changeMode(BaseImage.ImageMode.GREY8);
    imgL.changeMode(BaseImage.ImageMode.RGB24);
    imgL.changeMode(BaseImage.ImageMode.RGBA32);
    AWTImage imgRGB = img.changeMode(BaseImage.ImageMode.RGB24);
    imgRGB.changeMode(BaseImage.ImageMode.RGBA32);
    imgRGB.changeMode(BaseImage.ImageMode.GREY8);
    AWTImage imgRGBA = img.changeMode(BaseImage.ImageMode.RGBA32);
    imgRGBA.changeMode(BaseImage.ImageMode.GREY8);
    imgRGBA.changeMode(BaseImage.ImageMode.RGB24);
  }


  @Test
  public void openTiffFile() throws ImageException, IOException, NoSuchAlgorithmException {
    File tmpFile = File.createTempFile("tmpTest", ".tiff");
    AWTImage img = AWTImage.fromBaseImage(TestUtils.RGBAImageGenerator());
    img.save(tmpFile.getAbsolutePath());
    img = AWTImage.open(tmpFile.getAbsolutePath());
    assertEquals("49b127058ff72aa6b6ba9fff6ef78d5cdc0453c09a378b57d25b051790dbc9f1", TestUtils.hashByteArray(img.getByteArray()));
    tmpFile.delete();
  }

  @Test
  public void saveAndOpenJPEGFile() throws ImageException, IOException, NoSuchAlgorithmException {
    File tmpFile = File.createTempFile("img", ".jpg");
    AWTImage img = AWTImage.fromBaseImage(TestUtils.RGBAImageGenerator());
    img.save(tmpFile.getAbsolutePath());
    AWTImage img2 = AWTImage.open(tmpFile.getAbsolutePath());
    assertEquals("6c9dfd7a100110574495641f18590a282405f2082de92eef2c3777ee603e7509",TestUtils.hashByteArray(img2.getByteArray()));
    tmpFile.delete();
  }

  @Test
  public void saveAndopenPNGFile() throws ImageException, IOException, NoSuchAlgorithmException {
    File tmpFile = File.createTempFile("img", ".png");
    AWTImage img = AWTImage.fromBaseImage(TestUtils.RGBAImageGenerator());
    img.save(tmpFile.getAbsolutePath());
    AWTImage img2 = AWTImage.open(tmpFile.getAbsolutePath());  
    assertTrue(Arrays.equals(img2.getByteArray(), img.getByteArray()));
    tmpFile.delete();
  }

  @Test
  public void fillColorTestRGB() throws ImageException, IOException, NoSuchAlgorithmException {
    AWTImage img = AWTImage.create(BaseImage.ImageMode.RGB24, 200, 400);
    Color c = Color.fromRGBBytes((byte)10, (byte)220, (byte)110);
    img.fillImageWithColor(c);
    assertEquals("a745a437a586082d12e79f1d4a94550aec33adc7d08c6da682a669f1fb638569", TestUtils.hashByteArray(img.getByteArray()));
  }

  @Test
  public void fillColorTestRGBA() throws ImageException, IOException, NoSuchAlgorithmException {
    AWTImage img = AWTImage.create(BaseImage.ImageMode.RGBA32, 1203, 1226);
    Color c = Color.fromRGBABytes((byte)223,(byte)101,(byte)30, (byte)240);
    img.fillImageWithColor(c);
    assertEquals("fa61315bf8ed035d399b528207b5cbe04beed45631ae98b1f84136113f94eb38", TestUtils.hashByteArray(img.getByteArray()));
  }

  @Test
  public void fillColorTestL() throws ImageException, IOException, NoSuchAlgorithmException {
    AWTImage img = AWTImage.create(BaseImage.ImageMode.GREY8, 1440, 19887);
    Color c = Color.fromGreyByte((byte)231);
    img.fillImageWithColor(c);
    assertEquals("164be1e3cd389318de8cce67781687ebda9e98571c7592c5ec65d12b40eae091", TestUtils.hashByteArray(img.getByteArray()));
  }

  @Test
  public void cutTest() throws IOException, ImageException {
    AWTImage img = AWTImage.create(BaseImage.ImageMode.GREY8, 1440, 1988);
    Color c = Color.fromGreyByte((byte)231);
    img.fillImageWithColor(c);
    AWTImage img2 =img.cut(0, 0, 200, 200);
    AWTImage img3 =img.cut(200, 200, 200, 200);
    assertTrue(Arrays.equals(img2.getByteArray(), img3.getByteArray()));
  }

  @Test
  public void copyTest() throws IOException, ImageException {
    AWTImage img = AWTImage.create(BaseImage.ImageMode.GREY8, 1440, 1988);
    Color c = Color.fromGreyByte((byte)231);
    img.fillImageWithColor(c);
    AWTImage img2 = img.copy();
    assertTrue(Arrays.equals(img2.getByteArray(), img.getByteArray()));
    img.fillImageWithColor(Color.BLACK);
    assertTrue(!Arrays.equals(img2.getByteArray(), img.getByteArray()));
  }

  @Test
  public void fromBufferedImageTest1() {
    AWTImage img = AWTImage.create(BaseImage.ImageMode.GREY8, 1440, 1988);
    img.fillImageWithColor(Color.BLACK);

    BufferedImage bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_USHORT_GRAY);
    bi.getGraphics().drawImage(img.getBufferedImage(), 0, 0, null);
    AWTImage i2 = AWTImage.fromBufferedImage(bi);
    assertTrue(Arrays.equals(i2.getByteArray(), img.getByteArray()));
    bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
    bi.getGraphics().drawImage(img.getBufferedImage(), 0, 0, null);
    i2 = AWTImage.fromBufferedImage(bi);
    assertTrue(Arrays.equals(i2.getByteArray(), img.getByteArray()));
  }
  
  @Test
  public void fromBufferedImageTest2() throws IOException, ImageException {
    AWTImage img = AWTImage.create(BaseImage.ImageMode.RGB24, 1440, 1988);
    img.fillImageWithColor(Color.RED);
    BufferedImage bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_INDEXED);
    bi.getGraphics().drawImage(img.getBufferedImage(), 0, 0, null);
    AWTImage i2 = AWTImage.fromBufferedImage(bi);
    assertTrue(Arrays.equals(i2.getByteArray(), img.getByteArray()));
    bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_BGR);
    bi.getGraphics().drawImage(img.getBufferedImage(), 0, 0, null);
    i2 = AWTImage.fromBufferedImage(bi);
    assertTrue(Arrays.equals(i2.getByteArray(), img.getByteArray()));
    
    bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
    bi.getGraphics().drawImage(img.getBufferedImage(), 0, 0, null);
    i2 = AWTImage.fromBufferedImage(bi);
    assertTrue(Arrays.equals(i2.getByteArray(), img.getByteArray()));
    
    bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_USHORT_555_RGB);
    bi.getGraphics().drawImage(img.getBufferedImage(), 0, 0, null);
    i2 = AWTImage.fromBufferedImage(bi);
    assertTrue(Arrays.equals(i2.getByteArray(), img.getByteArray()));
    
    bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_USHORT_565_RGB);
    bi.getGraphics().drawImage(img.getBufferedImage(), 0, 0, null);
    i2 = AWTImage.fromBufferedImage(bi);
    assertTrue(Arrays.equals(i2.getByteArray(), img.getByteArray()));
    
    bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
    bi.getGraphics().drawImage(img.getBufferedImage(), 0, 0, null);
    i2 = AWTImage.fromBufferedImage(bi);
    assertTrue(Arrays.equals(i2.getByteArray(), img.getByteArray()));
  }
  
  @Test
  public void fromBufferedImageTest3() {
    AWTImage img = AWTImage.create(BaseImage.ImageMode.RGBA32, 1440, 1988);
    img.fillImageWithColor(Color.BLACK);
    BufferedImage bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
    bi.getGraphics().drawImage(img.getBufferedImage(), 0, 0, null);
    AWTImage i2 = AWTImage.fromBufferedImage(bi);
    assertTrue(Arrays.equals(i2.getByteArray(), img.getByteArray()));
    bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
    bi.getGraphics().drawImage(img.getBufferedImage(), 0, 0, null);
    i2 = AWTImage.fromBufferedImage(bi);
    assertTrue(Arrays.equals(i2.getByteArray(), img.getByteArray()));
    bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_4BYTE_ABGR_PRE);
    bi.getGraphics().drawImage(img.getBufferedImage(), 0, 0, null);
    i2 = AWTImage.fromBufferedImage(bi);
    assertTrue(Arrays.equals(i2.getByteArray(), img.getByteArray()));
    bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
    bi.getGraphics().drawImage(img.getBufferedImage(), 0, 0, null);
    i2 = AWTImage.fromBufferedImage(bi);
    assertTrue(Arrays.equals(i2.getByteArray(), img.getByteArray()));
    bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
    bi.getGraphics().drawImage(img.getBufferedImage(), 0, 0, null);
    i2 = AWTImage.fromBufferedImage(bi);
    assertTrue(Arrays.equals(i2.getByteArray(), img.getByteArray()));
    bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TRANSLUCENT);
    bi.getGraphics().drawImage(img.getBufferedImage(), 0, 0, null);
    i2 = AWTImage.fromBufferedImage(bi);
    assertTrue(Arrays.equals(i2.getByteArray(), img.getByteArray()));
  }
}
