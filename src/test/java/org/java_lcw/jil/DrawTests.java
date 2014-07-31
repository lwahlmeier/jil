package org.java_lcw.jil;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.java_lcw.jil.Image.ImageException;
import org.junit.Test;

public class DrawTests {
  
  @Test
  public void rectTest() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    Image img = Image.open(filename);
    Color c = new Color((byte)0,(byte)255,(byte)0);
    Draw.rect(img, 10, 10, 100, 10, c, 5, false);
    //Image img2 = Image.create(Image.MODE_RGB, 200, 200);
    assertEquals("9e7a90f3f3e7bb287e1aa01bbcb2d427ba958af2f1616baa1e6d5d65219746d0", ImageTest.hashByteArray(img.getArray()));
  } 
  
  @Test
  public void fillColorTest() throws ImageException, IOException, NoSuchAlgorithmException {
    Image img = Image.create(Image.MODE_RGB, 200, 200);
    Color c;
    c = new Color((byte)120,(byte)120,(byte)120);
    Draw.rect(img, 10, 10, 10, 10, c, 1, true);
    c = new Color((byte)84,(byte)201,(byte)12);
    Draw.fillColor(img, 0, 0, c);
    assertEquals("9483be16958c7dd03492b611a41c7dd33d6a5874f52b023a623042872bfb8671", ImageTest.hashByteArray(img.getArray()));
  } 
  
  @Test
  public void circleTest() throws ImageException, IOException, NoSuchAlgorithmException {
    Image img = Image.create(Image.MODE_RGB, 400, 400);
    Color c;

    c = new Color((byte)100,(byte)100,(byte)100);
    Draw.fillColor(img, 0, 0, c);
    
    //Center Circle no fill
    c = new Color((byte)255,(byte)255,(byte)255);
    Draw.circle(img, 200, 200, 200, c, 1, false);
    //Manually fill it
    c = new Color((byte)145,(byte)28,(byte)222);
    Draw.fillColor(img, 200, 200, c);
    
    //Draw at 0,0 and have it fill
    c = new Color((byte)145,(byte)28,(byte)22);
    Draw.circle(img, 0, 0, 200, c, 1, true);
    
    //Draw at 400x400 and no fill 1px wide
    c = new Color((byte)245,(byte)228,(byte)22);
    Draw.circle(img, 400, 400, 200, c, 1, false);
    
    System.out.println(ImageTest.hashByteArray(img.getArray()));
    assertEquals("c8da98f88b48090892577530f04093ac6cb14f46c1ea9ab7d1a61f6ba92eb31a", ImageTest.hashByteArray(img.getArray()));
  } 
  
  @Test
  public void lineTest() throws ImageException, IOException, NoSuchAlgorithmException {
    Image img = Image.create(Image.MODE_RGB, 400, 400);
    Color c;

    //grey canvas
    c = new Color((byte)100,(byte)100,(byte)100);
    Draw.fillColor(img, 0, 0, c);
    
    //Horizontal line all the way through
    c = new Color((byte)200,(byte)12,(byte)100);
    Draw.line(img, -100, 200, 500, 200, c, 5);
    
    //Vertical line all the way through
    c = new Color((byte)142,(byte)114,(byte)176);
    Draw.line(img, 200, -100, 200, 500, c, 5);
    
    //left to right line all the way through
    c = new Color((byte)44,(byte)214,(byte)55);
    Draw.line(img, -100, -100, 500, 500, c, 5);
    
    //right to left line all the way through
    c = new Color((byte)144,(byte)114,(byte)55);
    Draw.line(img, 500, -100, -100, 500, c, 5);
    assertEquals("c9c8b60b0949367ea2ed91f49d6881a6ead95dd08d71827b22844841a129da6d", ImageTest.hashByteArray(img.getArray()));
  }
  
  
  @Test
  public void lineTestWithAlpha() throws ImageException, IOException, NoSuchAlgorithmException {   

    Image img = Image.create(Image.MODE_RGBA, 400, 400);
    Color c;
    //grey canvas
    c = new Color((byte)100,(byte)100,(byte)100);
    Draw.fillColor(img, 0, 0, c);
    
    //Horizontal line all the way through
    c = new Color((byte)200,(byte)12,(byte)100, (byte)100);
    Draw.line(img, -100, 200, 500, 200, c, 5, true);
    
    //Vertical line all the way through
    c = new Color((byte)142,(byte)114,(byte)176, (byte)100);
    Draw.line(img, 200, -100, 200, 500, c, 5, true);
    
    //left to right line all the way through
    c = new Color((byte)44,(byte)214,(byte)55, (byte)100);
    Draw.line(img, -100, -100, 500, 500, c, 5, true);
    
    //right to left line all the way through
    c = new Color((byte)144,(byte)114,(byte)55, (byte)100);
    Draw.line(img, 500, -100, -100, 500, c, 5, true);
    
    assertEquals("47cc9e85cf826f57ff25539b9813b822003cc1fde5e1d32972fd695f6c626fbd", ImageTest.hashByteArray(img.getArray()));
  }
}
