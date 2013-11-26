package org.java_lcw.jil;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.java_lcw.jil.Image.ImageException;
import org.junit.Test;

public class DrawTests {
  
  @Test
  public void rectTest() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    Image img = Image.open(filename);
    Color c = new Color((byte)0,(byte)255,(byte)0);
    Draw.rect(img, 10, 10, 100, 10, c, 5, false);
    img.save("/tmp/test.png");


  } 
  
  @Test
  public void fillColorTest() throws ImageException, IOException, NoSuchAlgorithmException {

    Image img = Image.create(Image.MODE_RGB, 200, 200);
    Color c;
    Random r = new Random();

    c = new Color((byte)r.nextInt(255),(byte)r.nextInt(255),(byte)r.nextInt(255));
    Draw.rect(img, 10, 10, 10, 10, c, 1, true);
  
    c = new Color((byte)r.nextInt(255),(byte)r.nextInt(255),(byte)r.nextInt(255));
    Draw.fillColor(img, 0, 0, c);

  
    img.save("/tmp/test.png");

  } 
  
  @Test
  public void circleTest() throws ImageException, IOException, NoSuchAlgorithmException {

    Image img = Image.create(Image.MODE_RGB, 400, 400);
    Color c;
    Random r = new Random();

    c = new Color((byte)100,(byte)100,(byte)100);
    
    Draw.fillColor(img, 0, 0, c);
    c = new Color((byte)255,(byte)255,(byte)255);
    
    Draw.circle(img, 200, 200, 200, c, 10, false);
    
    
    c = new Color((byte)255,(byte)0,(byte)0);
    Draw.fillColor(img, 200, 200, c);
    
    Draw.circle(img, 0, 0, 200, c, 1, true);
    
    Draw.circle(img, 400, 400, 200, c, 10, false);
  
    img.save("/tmp/test.png");

  } 
  
  @Test
  public void lineTest() throws ImageException, IOException, NoSuchAlgorithmException {

    Image img = Image.create(Image.MODE_RGB, 400, 400);
    Color c;
    Random r = new Random();

    c = new Color((byte)100,(byte)100,(byte)100);
    
    Draw.fillColor(img, 0, 0, c);
    c = new Color((byte)255,(byte)255,(byte)255);
    
    Draw.line(img, -100, -100, 200, 500, c, 5);
    img.save("/tmp/test.png");
  }
}
