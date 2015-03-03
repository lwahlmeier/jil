package org.java_lcw.jil.ji;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.java_lcw.jil.Color;
import org.java_lcw.jil.Image;
import org.java_lcw.jil.ImageException;
import org.java_lcw.jil.JavaImage;
import org.java_lcw.jil.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JavaImageDrawTests {
  public String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
  public Image img;
  public Image img200;
  public Image img400;
  public Image img400a;
  public String endHash;
  
  @Before
  public void start() throws ImageException, IOException {
    img = JavaImage.open(filename);
    img200 = JavaImage.create(JavaImage.MODE_RGB, 200, 200);
    img400 = JavaImage.create(JavaImage.MODE_RGB, 400, 400);
    img400a = JavaImage.create(JavaImage.MODE_RGBA, 400, 400);
  }
  
  @After
  public void end() {
    
  }
  
  @Test
  public void rectTest() throws ImageException, IOException, NoSuchAlgorithmException {
    img.getImageDrawer().drawRect(10, 10, 100, 10, Color.GREEN, 5, false);
    
    assertEquals("c99082a82740e9f35eddc9a582c3befd28389ab3759bf69e3d8b4e073f45a06f", TestUtils.hashByteArray(img.getArray()));
  } 
  
  @Test
  public void fillColorTest() throws ImageException, IOException, NoSuchAlgorithmException {
    img200.getImageDrawer().drawRect(10, 10, 10, 10, Color.GREY, 1, true);
    img200.getImageDrawer().drawRect(50, 50, 10, 10, Color.WHITE, 1, false);
    img200.getImageDrawer().fillColor(0, 0, Color.RED);
    img200.save("/tmp/test.png");
    assertEquals("7b3b3952bf8e4af176dfddd92c2f275c023572e9751629a3de5b7c1a9698a991", TestUtils.hashByteArray(img200.getArray()));
  } 
  
  @Test
  public void circleTest() throws ImageException, IOException, NoSuchAlgorithmException {
    Color c;

    c = new Color((byte)100,(byte)100,(byte)100);
    img400.getImageDrawer().fillColor(0, 0, c);
    
    //Center Circle no fill
    c = new Color((byte)255,(byte)255,(byte)255);
    img400.getImageDrawer().drawCircle(200, 200, 200, c, 1, false);
    
    //Manually fill it
    c = new Color((byte)145,(byte)28,(byte)222);
    img400.getImageDrawer().fillColor(200, 200, c);
    
    //Draw at 0,0 and have it fill
    c = new Color((byte)145,(byte)28,(byte)22);
    img400.getImageDrawer().drawCircle(0, 0, 200, c, 1, true);
    
    //Draw at 400x400 and no fill 1px wide
    c = new Color((byte)245,(byte)228,(byte)22);
    img400.getImageDrawer().drawCircle(400, 400, 200, c, 1, false);

    assertEquals("c8da98f88b48090892577530f04093ac6cb14f46c1ea9ab7d1a61f6ba92eb31a", TestUtils.hashByteArray(img400.getArray()));
  } 
  
  @Test
  public void lineTest() throws ImageException, IOException, NoSuchAlgorithmException {
    Color c;

    //grey canvas
    c = new Color((byte)100,(byte)100,(byte)100);
    img400.getImageDrawer().fillColor(0, 0, c);
    
    //Horizontal line all the way through
    c = new Color((byte)200,(byte)12,(byte)100);
    img400.getImageDrawer().drawLine(-100, 200, 500, 200, c, 5);
    
    //Vertical line all the way through
    c = new Color((byte)142,(byte)114,(byte)176);
    img400.getImageDrawer().drawLine(200, -100, 200, 500, c, 5);
    
    //left to right line all the way through
    c = new Color((byte)44,(byte)214,(byte)55);
    img400.getImageDrawer().drawLine(-100, -100, 500, 500, c, 5);
    
    //right to left line all the way through
    c = new Color((byte)144,(byte)114,(byte)55);
    img400.getImageDrawer().drawLine( 500, -100, -100, 500, c, 5);

    assertEquals("c9c8b60b0949367ea2ed91f49d6881a6ead95dd08d71827b22844841a129da6d", TestUtils.hashByteArray(img400.getArray()));
  }
  
  
  @Test
  public void lineTestWithAlpha() throws ImageException, IOException, NoSuchAlgorithmException {   
    Color c;
    //grey canvas
    c = new Color((byte)100,(byte)100,(byte)100);
    img400a.getImageDrawer().fillColor(0, 0, c);
    
    //Horizontal line all the way through
    c = new Color((byte)200,(byte)12,(byte)100, (byte)100);
    img400a.getImageDrawer().drawLine(-100, 200, 500, 200, c, 5, true);
    
    //Vertical line all the way through
    c = new Color((byte)142,(byte)114,(byte)176, (byte)100);
    img400a.getImageDrawer().drawLine(200, -100, 200, 500, c, 5, true);
    
    //left to right line all the way through
    c = new Color((byte)44,(byte)214,(byte)55, (byte)100);
    img400a.getImageDrawer().drawLine(-100, -100, 500, 500, c, 5, true);
    
    //right to left line all the way through
    c = new Color((byte)144,(byte)114,(byte)55, (byte)100);
    img400a.getImageDrawer().drawLine(400, 0, 0, 400, c, 5, true);

    //System.out.println(ImageTest.hashByteArray(aimg.getArray()));
    assertEquals("47cc9e85cf826f57ff25539b9813b822003cc1fde5e1d32972fd695f6c626fbd", TestUtils.hashByteArray(img400a.getArray()));
  }
  
}
