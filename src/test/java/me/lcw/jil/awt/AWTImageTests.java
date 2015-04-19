package me.lcw.jil.awt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import me.lcw.jil.AWTImage;
import me.lcw.jil.Color;
import me.lcw.jil.Image;
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
  public void openTest1() throws ImageException, IOException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.jpg").getFile();
    AWTImage.open(filename);
    AWTImage.open(filename);
  }

  @Test
  public void modeChageTest() throws ImageException, IOException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    AWTImage img = AWTImage.open(filename);
    AWTImage imgL = img.changeMode(Image.MODE_L);
    imgL.changeMode(Image.MODE_RGB);
    imgL.changeMode(Image.MODE_RGBA);
    AWTImage imgRGB = img.changeMode(Image.MODE_RGB);
    imgRGB.changeMode(Image.MODE_RGBA);
    imgRGB.changeMode(Image.MODE_L);
    AWTImage imgRGBA = img.changeMode(Image.MODE_RGBA);
    imgRGBA.changeMode(Image.MODE_L);
    imgRGBA.changeMode(Image.MODE_RGB);
  }


  @Test
  public void openTiffFile() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    AWTImage img = AWTImage.open(filename);
    img.save("/tmp/test.tiff");
    img = AWTImage.open("/tmp/test.tiff");
    assertEquals("d1d0f3e8bf13708e56fe5ecb8c151402d489cf7595cf8d2e0026ba8e5fe17dae", TestUtils.hashByteArray(img.getArray()));
    File file = new File("/tmp/test.tiff");
    file.delete();
  }

  @Test
  public void openJPEGFile() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.jpg").getFile();
    AWTImage img = AWTImage.open(filename);
    img.save("/tmp/test.png");
    assertEquals("3ba178edbaab22b850174f17989144f74b340c90543abb911f3441e5ad8b357b",TestUtils.hashByteArray(img.getArray()));
  }

  @Test
  public void saveJPEGFile() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    AWTImage img = AWTImage.open(filename);
    img.save("/tmp/test.jpg");
    File file = new File("/tmp/test.jpg");
    file.delete();
  }

  @Test
  public void openPNGFile() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    AWTImage img = AWTImage.open(filename);
    assertEquals("c57c5fe4cf97763ecbded98e82ced7faee5138adb6bd68641d009b4f4ab4c975", TestUtils.hashByteArray(img.getArray()));
  }

  @Test
  public void savePNGFile() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    AWTImage img = AWTImage.open(filename);
    img.save("/tmp/test.png");
    File file = new File("/tmp/test.png");
    file.delete();
    assertEquals("c57c5fe4cf97763ecbded98e82ced7faee5138adb6bd68641d009b4f4ab4c975", TestUtils.hashByteArray(img.getArray()));
  }

  @Test
  public void fillColorTestRGB() throws ImageException, IOException, NoSuchAlgorithmException {
    AWTImage img = AWTImage.create(Image.MODE_RGB, 200, 400);
    Color c = new Color((byte)10, (byte)220, (byte)110);
    img.fillImageWithColor(c);
    assertEquals("a745a437a586082d12e79f1d4a94550aec33adc7d08c6da682a669f1fb638569", TestUtils.hashByteArray(img.getArray()));
  }

  @Test
  public void fillColorTestRGBA() throws ImageException, IOException, NoSuchAlgorithmException {
    AWTImage img = AWTImage.create(Image.MODE_RGBA, 1203, 1226);
    Color c = new Color((byte)223,(byte)101,(byte)30, (byte)240);
    img.fillImageWithColor(c);
    assertEquals("fa61315bf8ed035d399b528207b5cbe04beed45631ae98b1f84136113f94eb38", TestUtils.hashByteArray(img.getArray()));
  }

  @Test
  public void fillColorTestL() throws ImageException, IOException, NoSuchAlgorithmException {
    AWTImage img = AWTImage.create(Image.MODE_L, 1440, 19887);
    Color c = new Color((byte)231);
    img.fillImageWithColor(c);
    assertEquals("164be1e3cd389318de8cce67781687ebda9e98571c7592c5ec65d12b40eae091", TestUtils.hashByteArray(img.getArray()));
  }

  @Test
  public void cutTest() throws IOException, ImageException {
    AWTImage img = AWTImage.create(Image.MODE_L, 1440, 1988);
    Color c = new Color((byte)231);
    img.fillImageWithColor(c);
    AWTImage img2 =img.cut(0, 0, 200, 200);
    AWTImage img3 =img.cut(200, 200, 200, 200);
    assertTrue(Arrays.equals(img2.getArray(), img3.getArray()));
  }

  @Test
  public void copyTest() throws IOException, ImageException {
    AWTImage img = AWTImage.create(Image.MODE_L, 1440, 1988);
    Color c = new Color((byte)231);
    img.fillImageWithColor(c);
    AWTImage img2 = img.copy();
    assertTrue(Arrays.equals(img2.getArray(), img.getArray()));
    img.fillImageWithColor(Color.BLACK);
    assertTrue(!Arrays.equals(img2.getArray(), img.getArray()));
  }

  @Test
  public void fromBufferedImageTest1() {
    AWTImage img = AWTImage.create(Image.MODE_L, 1440, 1988);
    img.fillImageWithColor(Color.BLACK);
    BufferedImage bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
    bi.getGraphics().drawImage(img.getBufferedImage(), 0, 0, null);
    AWTImage i2 = AWTImage.fromBufferedImage(bi);
    assertTrue(Arrays.equals(i2.getArray(), img.getArray()));
    bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_USHORT_GRAY);
    bi.getGraphics().drawImage(img.getBufferedImage(), 0, 0, null);
    i2 = AWTImage.fromBufferedImage(bi);
    assertTrue(Arrays.equals(i2.getArray(), img.getArray()));
    bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
    bi.getGraphics().drawImage(img.getBufferedImage(), 0, 0, null);
    i2 = AWTImage.fromBufferedImage(bi);
    assertTrue(Arrays.equals(i2.getArray(), img.getArray()));
  }
  
  @Test
  public void fromBufferedImageTest2() throws IOException, ImageException {
    AWTImage img = AWTImage.create(Image.MODE_RGB, 1440, 1988);
    img.fillImageWithColor(Color.RED);
    BufferedImage bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_INDEXED);
    bi.getGraphics().drawImage(img.getBufferedImage(), 0, 0, null);
    AWTImage i2 = AWTImage.fromBufferedImage(bi);
    assertTrue(Arrays.equals(i2.getArray(), img.getArray()));
    bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_BGR);
    bi.getGraphics().drawImage(img.getBufferedImage(), 0, 0, null);
    i2 = AWTImage.fromBufferedImage(bi);
    assertTrue(Arrays.equals(i2.getArray(), img.getArray()));
    
    bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
    bi.getGraphics().drawImage(img.getBufferedImage(), 0, 0, null);
    i2 = AWTImage.fromBufferedImage(bi);
    assertTrue(Arrays.equals(i2.getArray(), img.getArray()));
    
    bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_USHORT_555_RGB);
    bi.getGraphics().drawImage(img.getBufferedImage(), 0, 0, null);
    i2 = AWTImage.fromBufferedImage(bi);
    assertTrue(Arrays.equals(i2.getArray(), img.getArray()));
    
    bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_USHORT_565_RGB);
    bi.getGraphics().drawImage(img.getBufferedImage(), 0, 0, null);
    i2 = AWTImage.fromBufferedImage(bi);
    assertTrue(Arrays.equals(i2.getArray(), img.getArray()));
    
    bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
    bi.getGraphics().drawImage(img.getBufferedImage(), 0, 0, null);
    i2 = AWTImage.fromBufferedImage(bi);
    assertTrue(Arrays.equals(i2.getArray(), img.getArray()));
  }
  
  @Test
  public void fromBufferedImageTest3() {
    AWTImage img = AWTImage.create(Image.MODE_RGBA, 1440, 1988);
    img.fillImageWithColor(Color.BLACK);
    BufferedImage bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
    bi.getGraphics().drawImage(img.getBufferedImage(), 0, 0, null);
    AWTImage i2 = AWTImage.fromBufferedImage(bi);
    assertTrue(Arrays.equals(i2.getArray(), img.getArray()));
    bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_4BYTE_ABGR_PRE);
    bi.getGraphics().drawImage(img.getBufferedImage(), 0, 0, null);
    i2 = AWTImage.fromBufferedImage(bi);
    assertTrue(Arrays.equals(i2.getArray(), img.getArray()));
    bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
    bi.getGraphics().drawImage(img.getBufferedImage(), 0, 0, null);
    i2 = AWTImage.fromBufferedImage(bi);
    assertTrue(Arrays.equals(i2.getArray(), img.getArray()));
    bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
    bi.getGraphics().drawImage(img.getBufferedImage(), 0, 0, null);
    i2 = AWTImage.fromBufferedImage(bi);
    assertTrue(Arrays.equals(i2.getArray(), img.getArray()));
    bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TRANSLUCENT);
    bi.getGraphics().drawImage(img.getBufferedImage(), 0, 0, null);
    i2 = AWTImage.fromBufferedImage(bi);
    assertTrue(Arrays.equals(i2.getArray(), img.getArray()));
  }
}
