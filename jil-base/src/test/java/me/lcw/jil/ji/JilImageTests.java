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

public class JilImageTests {
  
  @Test(expected=IOException.class)
  public void exceptionTest() throws ImageException, IOException {
    JilImage.open("/bad/path/to/open.png");
  }
  
  @Test(expected=RuntimeException.class)
  public void exceptionTest2() throws ImageException, IOException {
    JilImage.fromByteArray(JilImage.ImageMode.RGB24, 200, 200, new byte[2]);
  }
  
  @Test(expected=FileNotFoundException.class)
  public void exceptionTest3() throws ImageException, IOException {
    JilImage.open("TEST.BLAH");
  }
  
  @Test
  public void modeChageTest() throws ImageException, IOException {
    JilImage img = TestUtils.RGBAImageGenerator();
    JilImage imgL = img.changeMode(JilImage.ImageMode.GREY8);
    imgL.changeMode(JilImage.ImageMode.RGB24);
    imgL.changeMode(JilImage.ImageMode.RGBA32);
    JilImage imgRGB = img.changeMode(JilImage.ImageMode.RGB24);
    imgRGB.changeMode(JilImage.ImageMode.RGBA32);
    imgRGB.changeMode(JilImage.ImageMode.GREY8);
    JilImage imgRGBA = img.changeMode(JilImage.ImageMode.RGBA32);
    imgRGBA.changeMode(JilImage.ImageMode.GREY8);
    imgRGBA.changeMode(JilImage.ImageMode.RGB24);
  }
  
  @Test
  public void randomRGBAImageTest() throws IOException, ImageException {
    int h = TestUtils.getSize(40, 500);
    int w = TestUtils.getSize(40, 500);
    JilImage img = JilImage.create(JilImage.ImageMode.RGBA32, w, h);
    img.mkRandom();
    assertEquals((img.getWidth()*img.getHeight()*img.getColors()), img.getByteArray().length);  }
  
  @Test
  public void randomRGBImageTest() throws IOException, ImageException {
    int h = TestUtils.getSize(40, 500);
    int w = TestUtils.getSize(40, 500);
    JilImage img = JilImage.create(JilImage.ImageMode.RGB24, w, h);
    img.mkRandom();
    assertEquals((img.getWidth()*img.getHeight()*img.getColors()), img.getByteArray().length);
  }
  
  @Test
  public void randomLImageTest() throws IOException, ImageException {
    int h = TestUtils.getSize(40, 500);
    int w = TestUtils.getSize(40, 500);
    JilImage img = JilImage.create(JilImage.ImageMode.GREY8, w, h);
    img.mkRandom();
    assertEquals((img.getWidth()*img.getHeight()*img.getColors()), img.getByteArray().length);
  }

  @Test
  public void openTiffFile() throws ImageException, IOException, NoSuchAlgorithmException {
    File tmpFile = File.createTempFile("img", ".tiff");
    System.out.println(tmpFile.getAbsolutePath());
    JilImage img = TestUtils.RGBImageGenerator();
    img.save(tmpFile.getAbsolutePath());
    img = JilImage.open(tmpFile.getAbsolutePath());
    assertEquals("8f0b42f2510072bccf187838811a60c69c835427dbad481ec3d81a1d1547bb91", TestUtils.hashByteArray(img.getByteArray()));
    tmpFile.delete();
  }
}