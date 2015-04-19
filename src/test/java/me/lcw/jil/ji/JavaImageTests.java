package me.lcw.jil.ji;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import me.lcw.jil.ImageException;
import me.lcw.jil.JilImage;
import me.lcw.jil.TestUtils;

import org.junit.Test;



public class JavaImageTests {
  
  @Test(expected=IOException.class)
  public void exceptionTest() throws ImageException, IOException {
    JilImage.open("/bad/path/to/open.png");
  }
  
  @Test(expected=RuntimeException.class)
  public void exceptionTest2() throws ImageException, IOException {
    JilImage.fromByteArray(JilImage.MODE_RGB, 200, 200, new byte[2]);
  }
  
  @Test(expected=FileNotFoundException.class)
  public void exceptionTest3() throws ImageException, IOException {
    JilImage.open("TEST.BLAH");
  }
  
  @Test
  public void openTest1() throws ImageException, IOException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.jpg").getFile();
    JilImage.open(filename);
    JilImage.open(filename);
  }
  
  @Test
  public void modeChageTest() throws ImageException, IOException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    JilImage img = JilImage.open(filename);
    JilImage imgL = img.changeMode(JilImage.MODE_L);
    imgL.changeMode(JilImage.MODE_RGB);
    imgL.changeMode(JilImage.MODE_RGBA);
    JilImage imgRGB = img.changeMode(JilImage.MODE_RGB);
    imgRGB.changeMode(JilImage.MODE_RGBA);
    imgRGB.changeMode(JilImage.MODE_L);
    JilImage imgRGBA = img.changeMode(JilImage.MODE_RGBA);
    imgRGBA.changeMode(JilImage.MODE_L);
    imgRGBA.changeMode(JilImage.MODE_RGB);
  }
  
  @Test
  public void randomRGBAImageTest() throws IOException, ImageException {
    int h = TestUtils.getSize(40, 500);
    int w = TestUtils.getSize(40, 500);
    JilImage img = JilImage.create(JilImage.MODE_RGBA, w, h);
    img.mkRandom();
    assertEquals((img.getWidth()*img.getHeight()*img.getColors()), img.getArray().length);  }
  
  @Test
  public void randomRGBImageTest() throws IOException, ImageException {
    int h = TestUtils.getSize(40, 500);
    int w = TestUtils.getSize(40, 500);
    JilImage img = JilImage.create(JilImage.MODE_RGB, w, h);
    img.mkRandom();
    assertEquals((img.getWidth()*img.getHeight()*img.getColors()), img.getArray().length);
  }
  
  @Test
  public void randomLImageTest() throws IOException, ImageException {
    int h = TestUtils.getSize(40, 500);
    int w = TestUtils.getSize(40, 500);
    JilImage img = JilImage.create(JilImage.MODE_L, w, h);
    img.mkRandom();
    assertEquals((img.getWidth()*img.getHeight()*img.getColors()), img.getArray().length);
  }

  @Test
  public void openTiffFile() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    JilImage img = JilImage.open(filename);
    img.save("/tmp/test.tiff");
    img = JilImage.open("/tmp/test.tiff");
    assertEquals("d1d0f3e8bf13708e56fe5ecb8c151402d489cf7595cf8d2e0026ba8e5fe17dae", TestUtils.hashByteArray(img.getArray()));
    File file = new File("/tmp/test.tiff");
    file.delete();
  }
  
  @Test
  public void openJPEGFile() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.jpg").getFile();
    JilImage img = JilImage.open(filename);
    img.save("/tmp/test.png");
    assertEquals("3ba178edbaab22b850174f17989144f74b340c90543abb911f3441e5ad8b357b",TestUtils.hashByteArray(img.getArray()));
  }
  
  @Test
  public void saveJPEGFile() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    JilImage img = JilImage.open(filename);
    img.save("/tmp/test.jpg");
    File file = new File("/tmp/test.jpg");
    file.delete();
  }
  
  @Test
  public void openPNGFile() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    JilImage img = JilImage.open(filename);
    assertEquals("c57c5fe4cf97763ecbded98e82ced7faee5138adb6bd68641d009b4f4ab4c975", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Test
  public void savePNGFile() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    JilImage img = JilImage.open(filename);
    img.save("/tmp/test.png");
    File file = new File("/tmp/test.png");
    file.delete();
    assertEquals("c57c5fe4cf97763ecbded98e82ced7faee5138adb6bd68641d009b4f4ab4c975", TestUtils.hashByteArray(img.getArray()));
  }
}