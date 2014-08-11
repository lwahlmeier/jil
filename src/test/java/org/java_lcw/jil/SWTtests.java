package org.java_lcw.jil;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.eclipse.swt.graphics.ImageData;
import org.java_lcw.jil.Image.ImageException;
import org.junit.Test;

public class SWTtests {
 
  
  @Test
  public void swtFromTest1() throws IOException, ImageException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage1.png").getFile();
    ImageData img = new ImageData(filename);
    Image newImg = Image.fromImageData(img);
    assertEquals("5570085b90c95f3166f866116d679b67b2c52103ee08d6543499adb113450710", ImageTest.hashByteArray(newImg.getArray()));
  }
  
  @Test
  public void swtFromTest2() throws IOException, ImageException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    ImageData img = new ImageData(filename);
    Image newImg = Image.fromImageData(img);
    assertEquals("d1d0f3e8bf13708e56fe5ecb8c151402d489cf7595cf8d2e0026ba8e5fe17dae", ImageTest.hashByteArray(newImg.getArray()));
  }
  
  @Test
  public void swtFromTest3() throws IOException, ImageException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    ImageData img = new ImageData(filename);
    Image newImg = Image.fromImageData(img);
    assertEquals("3ba178edbaab22b850174f17989144f74b340c90543abb911f3441e5ad8b357b", ImageTest.hashByteArray(newImg.getArray()));
  }
  
  @Test
  public void swtFromTestBW() throws IOException, ImageException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImageBW.png").getFile();
    ImageData img = new ImageData(filename);
    Image newImg = Image.fromImageData(img);
    assertEquals("dd075dd38db8a7a749c9faab6d1b8d5c66aaf1992e6961e4cb64f6d99c53c00f", ImageTest.hashByteArray(newImg.getArray()));
  }
  
  
  @Test
  public void swtToTestRGBA() throws IOException, ImageException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage1.png").getFile();
    Image img = Image.open(filename);
    String origHash = ImageTest.hashByteArray(img.getArray());
    assertEquals("8d284842be14a7ad0b4c2b20fc39a1271caf361223ca1734373085f6e3217c86", origHash);
  }
  
  
  @Test
  public void swtToTest1() throws IOException, ImageException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage1.png").getFile();
    Image img = Image.open(filename);
    //Switch to RGB known issue with fromImageData
    img = img.changeMode(Image.MODE_RGB);
    String origHash = ImageTest.hashByteArray(img.getArray());
    ImageData ID = img.toImageData();
    img = Image.fromImageData(ID);
    String newHash = ImageTest.hashByteArray(img.getArray());
    assertEquals(origHash, newHash);
  }
  
  @Test
  public void swtToTest2() throws IOException, ImageException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    Image img = Image.open(filename);
    img = img.changeMode(Image.MODE_RGB);
    String origHash = ImageTest.hashByteArray(img.getArray());
    ImageData ID = img.toImageData();
    img = Image.fromImageData(ID);
    String newHash = ImageTest.hashByteArray(img.getArray());
    assertEquals(origHash, newHash);
  }
  
  @Test
  public void swtToTest3() throws IOException, ImageException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    Image img = Image.open(filename);
    img = img.changeMode(Image.MODE_L);
    String origHash = ImageTest.hashByteArray(img.getArray());
    ImageData ID = img.toImageData();
    img = Image.fromImageData(ID);
    //Comes in as 24bit from fromImageData
    img = img.changeMode(Image.MODE_L);
    String newHash = ImageTest.hashByteArray(img.getArray());
    assertEquals(origHash, newHash);
  }
}
