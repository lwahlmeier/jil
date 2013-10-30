package org.java_image;

import static org.junit.Assert.assertEquals;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.imageio.ImageIO;

import org.java_image.Image.ImageException;
import org.junit.Test;

public class AWTtests {
  
  @Test
  public void awtTest1() throws IOException, ImageException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    File file = new File(filename);
    Image newImg = Image.fromBufferedImage(ImageIO.read(file));
    assertEquals("2740469d3f7d98b6fbb713f12826395be721bf5bf58bb90b9118ea2b6b3505ff", ImageTest.hashByteArray(newImg.toArray()));
  }
  @Test
  public void awtTest2() throws IOException, ImageException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    File file = new File(filename);
    Image newImg = Image.fromBufferedImage(ImageIO.read(file));
    assertEquals("c57c5fe4cf97763ecbded98e82ced7faee5138adb6bd68641d009b4f4ab4c975", ImageTest.hashByteArray(newImg.toArray()));
  }
  @Test
  public void awtTest3() throws IOException, ImageException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    File file = new File(filename);
    Image newImg = Image.fromBufferedImage(ImageIO.read(file));
    assertEquals("2740469d3f7d98b6fbb713f12826395be721bf5bf58bb90b9118ea2b6b3505ff", ImageTest.hashByteArray(newImg.toArray()));
  }
  
  @Test
  public void awtFromTestBW() throws IOException, ImageException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImageBW.png").getFile();
    File file = new File(filename);
    Image newImg = Image.fromBufferedImage(ImageIO.read(file));
    assertEquals("6e6e224898e25dd4f4fceb08342df7f8d661622c08e8c3e77c59d9ed27e5f8ca", ImageTest.hashByteArray(newImg.toArray()));
  }

  
  
  @Test
  public void awtToTest1() throws IOException, ImageException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    Image img = Image.open(filename);
    BufferedImage BI = img.toBufferedImage();
    assertEquals("2740469d3f7d98b6fbb713f12826395be721bf5bf58bb90b9118ea2b6b3505ff", ImageTest.hashByteArray(img.toArray()));
  }
  
  @Test
  public void awtToTest2() throws IOException, ImageException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    Image img = Image.open(filename);
    img = img.changeMode(Image.MODE_RGB);
    BufferedImage BI = img.toBufferedImage();
    assertEquals("3ba178edbaab22b850174f17989144f74b340c90543abb911f3441e5ad8b357b", ImageTest.hashByteArray(img.toArray()));
  }
  
  @Test
  public void awtToTest3() throws IOException, ImageException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    Image img = Image.open(filename);
    img = img.changeMode(Image.MODE_L);
    BufferedImage BI = img.toBufferedImage();
    assertEquals("023edd93087ecbb7c73694d68827beb091886a5811a457b708b5976aea4bdf27", ImageTest.hashByteArray(img.toArray()));
  }
}
