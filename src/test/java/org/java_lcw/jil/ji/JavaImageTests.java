package org.java_lcw.jil.ji;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.java_lcw.jil.Image;
import org.java_lcw.jil.ImageException;
import org.java_lcw.jil.JavaImage;
import org.java_lcw.jil.TestUtils;
import org.junit.Test;



public class JavaImageTests {
  
  @Test(expected=IOException.class)
  public void exceptionTest() throws ImageException, IOException {
    JavaImage.open("/bad/path/to/open.png");
  }
  
  @Test(expected=RuntimeException.class)
  public void exceptionTest2() throws ImageException, IOException {
    JavaImage.fromByteArray(JavaImage.MODE_RGB, 200, 200, new byte[2]);
  }
  
  @Test(expected=FileNotFoundException.class)
  public void exceptionTest3() throws ImageException, IOException {
    JavaImage.open("TEST.BLAH");
  }
  
  @Test
  public void openTest1() throws ImageException, IOException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.jpg").getFile();
    JavaImage.open(filename);
    JavaImage.open(filename);
  }
  
  @Test
  public void modeChageTest() throws ImageException, IOException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    JavaImage img = JavaImage.open(filename);
    JavaImage imgL = img.changeMode(JavaImage.MODE_L);
    imgL.changeMode(JavaImage.MODE_RGB);
    imgL.changeMode(JavaImage.MODE_RGBA);
    JavaImage imgRGB = img.changeMode(JavaImage.MODE_RGB);
    imgRGB.changeMode(JavaImage.MODE_RGBA);
    imgRGB.changeMode(JavaImage.MODE_L);
    JavaImage imgRGBA = img.changeMode(JavaImage.MODE_RGBA);
    imgRGBA.changeMode(JavaImage.MODE_L);
    imgRGBA.changeMode(JavaImage.MODE_RGB);
  }
  
  @Test
  public void randomRGBAImageTest() throws IOException, ImageException {
    int h = TestUtils.getSize(40, 500);
    int w = TestUtils.getSize(40, 500);
    JavaImage img = JavaImage.create(JavaImage.MODE_RGBA, w, h);
    img.mkRandom();
    assertEquals((img.getWidth()*img.getHeight()*img.getColors()), img.getArray().length);  }
  
  @Test
  public void randomRGBImageTest() throws IOException, ImageException {
    int h = TestUtils.getSize(40, 500);
    int w = TestUtils.getSize(40, 500);
    JavaImage img = JavaImage.create(JavaImage.MODE_RGB, w, h);
    img.mkRandom();
    assertEquals((img.getWidth()*img.getHeight()*img.getColors()), img.getArray().length);
  }
  
  @Test
  public void randomLImageTest() throws IOException, ImageException {
    int h = TestUtils.getSize(40, 500);
    int w = TestUtils.getSize(40, 500);
    JavaImage img = JavaImage.create(JavaImage.MODE_L, w, h);
    img.mkRandom();
    assertEquals((img.getWidth()*img.getHeight()*img.getColors()), img.getArray().length);
  }

  @Test
  public void openTiffFile() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    JavaImage img = JavaImage.open(filename);
    img.save("/tmp/test.tiff");
    img = JavaImage.open("/tmp/test.tiff");
    assertEquals("d1d0f3e8bf13708e56fe5ecb8c151402d489cf7595cf8d2e0026ba8e5fe17dae", TestUtils.hashByteArray(img.getArray()));
    File file = new File("/tmp/test.tiff");
    file.delete();
  }
  
  @Test
  public void openJPEGFile() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.jpg").getFile();
    JavaImage img = JavaImage.open(filename);
    img.save("/tmp/test.png");
    assertEquals("3ba178edbaab22b850174f17989144f74b340c90543abb911f3441e5ad8b357b",TestUtils.hashByteArray(img.getArray()));
  }
  
  @Test
  public void saveJPEGFile() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    JavaImage img = JavaImage.open(filename);
    img.save("/tmp/test.jpg");
    File file = new File("/tmp/test.jpg");
    file.delete();
  }
  
  @Test
  public void openPNGFile() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    JavaImage img = JavaImage.open(filename);
    assertEquals("c57c5fe4cf97763ecbded98e82ced7faee5138adb6bd68641d009b4f4ab4c975", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Test
  public void savePNGFile() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    JavaImage img = JavaImage.open(filename);
    img.save("/tmp/test.png");
    File file = new File("/tmp/test.png");
    file.delete();
    assertEquals("c57c5fe4cf97763ecbded98e82ced7faee5138adb6bd68641d009b4f4ab4c975", TestUtils.hashByteArray(img.getArray()));
  }
}