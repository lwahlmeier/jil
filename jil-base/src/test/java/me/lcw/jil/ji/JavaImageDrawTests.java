package me.lcw.jil.ji;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import me.lcw.jil.BaseImage;
import me.lcw.jil.Color;
import me.lcw.jil.ImageException;
import me.lcw.jil.JilImage;
import me.lcw.jil.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JavaImageDrawTests {
  public BaseImage img;
  public BaseImage img200;
  public BaseImage img400;
  public BaseImage img400a;
  public String endHash;
  
  @Before
  public void start() throws ImageException, IOException {
    img = TestUtils.RGBAImageGenerator();
    img200 = JilImage.create(JilImage.MODE.RGB, 200, 200);
    img400 = JilImage.create(JilImage.MODE.RGB, 400, 400);
    img400a = JilImage.create(JilImage.MODE.RGBA, 400, 400);
  }
  
  @After
  public void end() {
    img = null;
    img200 = null;
    img400 = null;
    img400a = null;
    endHash = null;
    System.gc();
  }
  
  @Test
  public void floodFillTest1() throws Exception {
    BaseImage img = JilImage.create(BaseImage.MODE.RGB, 200, 200);
    img.fillImageWithColor(Color.WHITE);
    img.draw().rect(30, 30, 140, 140, Color.RED, 1, true);
    img.draw().rect(80, 80, 80, 80, Color.GREEN, 1, true);
    assertEquals("63a715ea35d3509ade22d5c69f7a95077b04130e6384303f8ccd83c2578584a5", TestUtils.hashByteArray(img.getArray()));
    //Doing this on red should not work
    img.draw().floodFill(78, 78, Color.BLUE, Color.RED, false);
    assertEquals("63a715ea35d3509ade22d5c69f7a95077b04130e6384303f8ccd83c2578584a5", TestUtils.hashByteArray(img.getArray()));
    //This changes the outside
    img.draw().floodFill(0, 0, Color.BLUE, Color.RED, false);
    assertEquals("aa260abcfbe77d4a98d72c638771db8b691670510d508458ce36fabc41200ffa", TestUtils.hashByteArray(img.getArray()));
    //Changes the inside
    img.draw().floodFill(81, 81, Color.BLUE, Color.RED, false);
    assertEquals("1dee5a3950bcd1c313cc8f2b6d5e62c3a5415230236dfb33f0c8140e7b9df2ff", TestUtils.hashByteArray(img.getArray()));
    //Change inside back
    img.draw().floodFill(81, 81, Color.GREEN, Color.RED, false);
    assertEquals("aa260abcfbe77d4a98d72c638771db8b691670510d508458ce36fabc41200ffa", TestUtils.hashByteArray(img.getArray()));
    //Change outside back to white
    img.draw().floodFill(0, 0, Color.WHITE, null, false);
    assertEquals("63a715ea35d3509ade22d5c69f7a95077b04130e6384303f8ccd83c2578584a5", TestUtils.hashByteArray(img.getArray()));
    //System.out.println(TestUtils.hashByteArray(img.getArray()));
    //img.save("/tmp/test.png");
  }
  
  @Test
  public void floodFillTest2() throws Exception {
    BaseImage img = JilImage.create(BaseImage.MODE.RGBA, 200, 200);
    img.fillImageWithColor(new Color(Color.MAX_BYTE, Color.MAX_BYTE, Color.MAX_BYTE, (byte)200));
    img.draw().rect(30, 30, 140, 140, Color.RED, 1, true);
    img.draw().rect(80, 80, 80, 80, Color.GREEN, 1, true);

    assertEquals("e15dd57053d023c607ba264249b17897c6c918e0205a20edc1e5e245b330811e", TestUtils.hashByteArray(img.getArray()));
    //Doing this on red should not work
    img.draw().floodFill(78, 78, Color.BLUE, Color.RED, true);

    assertEquals("e15dd57053d023c607ba264249b17897c6c918e0205a20edc1e5e245b330811e", TestUtils.hashByteArray(img.getArray()));
    //This changes the outside
    img.draw().floodFill(0, 0, Color.BLUE, Color.RED, true);

    assertEquals("8a0e9327bbe083a1fbaad4f29b8df3aad0c186a11669daca5bb19e453a341a89", TestUtils.hashByteArray(img.getArray()));
    //Changes the inside
    img.draw().floodFill(81, 81, Color.BLUE, Color.RED, true);

    assertEquals("bcac8631c9b7f3cea4053e59ae24c7640f546a0e42f38d200766ec72c742d3d8", TestUtils.hashByteArray(img.getArray()));
    //Change inside back
    img.draw().floodFill(81, 81, Color.GREEN, Color.RED, true);

    assertEquals("8a0e9327bbe083a1fbaad4f29b8df3aad0c186a11669daca5bb19e453a341a89", TestUtils.hashByteArray(img.getArray()));
    //Change outside back to white
    img.draw().floodFill(0, 0, Color.WHITE, null, true);
    assertEquals("e15dd57053d023c607ba264249b17897c6c918e0205a20edc1e5e245b330811e", TestUtils.hashByteArray(img.getArray()));
    //System.out.println(TestUtils.hashByteArray(img.getArray()));
    //img.save("/tmp/test.png");
  }
  
  @Test
  public void rectTest() throws ImageException, IOException, NoSuchAlgorithmException {
    img.draw().rect(10, 10, 100, 10, Color.GREEN, 5, false);
    //AWTImage.fromBaseImage(img).save("/tmp/test.png");
    assertEquals("e2d46933ac330334a92995eca64f0db70e7966c7d2f38a7420a0443a38f395ff", TestUtils.hashByteArray(img.getArray()));
  } 
  
  @Test
  public void fillColorTest() throws ImageException, IOException, NoSuchAlgorithmException {
    img200.draw().rect(10, 10, 10, 10, Color.GREY, 1, true);
    img200.draw().rect(50, 50, 10, 10, Color.WHITE, 1, false);
    img200.draw().fillColor(0, 0, Color.RED);
    assertEquals("7b3b3952bf8e4af176dfddd92c2f275c023572e9751629a3de5b7c1a9698a991", TestUtils.hashByteArray(img200.getArray()));
  } 
  
  @Test
  public void circleTest() throws ImageException, IOException, NoSuchAlgorithmException {
    Color c;

    c = new Color((byte)100,(byte)100,(byte)100);
    img400.draw().fillColor(0, 0, c);
    
    //Center Circle no fill
    c = new Color((byte)255,(byte)255,(byte)255);
    img400.draw().circle(200, 200, 200, c, 1, false);
    
    //Manually fill it
    c = new Color((byte)145,(byte)28,(byte)222);
    img400.draw().fillColor(200, 200, c);
    
    //Draw at 0,0 and have it fill
    c = new Color((byte)145,(byte)28,(byte)22);
    img400.draw().circle(0, 0, 200, c, 1, true);
    
    //Draw at 400x400 and no fill 1px wide
    c = new Color((byte)245,(byte)228,(byte)22);
    img400.draw().circle(400, 400, 200, c, 1, false);

    assertEquals("c8da98f88b48090892577530f04093ac6cb14f46c1ea9ab7d1a61f6ba92eb31a", TestUtils.hashByteArray(img400.getArray()));
  } 
  
  @Test
  public void lineTest() throws ImageException, IOException, NoSuchAlgorithmException {
    Color c;

    //grey canvas
    c = new Color((byte)100,(byte)100,(byte)100);
    img400.draw().fillColor(0, 0, c);
    
    //Horizontal line all the way through
    c = new Color((byte)200,(byte)12,(byte)100);
    img400.draw().line(-100, 200, 500, 200, c, 5, false);
    
    //Vertical line all the way through
    c = new Color((byte)142,(byte)114,(byte)176);
    img400.draw().line(200, -100, 200, 500, c, 5, false);
    
    //left to right line all the way through
    c = new Color((byte)44,(byte)214,(byte)55);
    img400.draw().line(-100, -100, 500, 500, c, 5, false);
    
    //right to left line all the way through
    c = new Color((byte)144,(byte)114,(byte)55);
    img400.draw().line( 500, -100, -100, 500, c, 5, false);

    assertEquals("c9c8b60b0949367ea2ed91f49d6881a6ead95dd08d71827b22844841a129da6d", TestUtils.hashByteArray(img400.getArray()));
  }
  
  
  @Test
  public void lineTestWithAlpha() throws ImageException, IOException, NoSuchAlgorithmException {   
    Color c;
    //grey canvas
    c = new Color((byte)100,(byte)100,(byte)100);
    img400a.draw().fillColor(0, 0, c);
    
    //Horizontal line all the way through
    c = new Color((byte)200,(byte)12,(byte)100, (byte)100);
    img400a.draw().line(-100, 200, 500, 200, c, 5, true);
    
    //Vertical line all the way through
    c = new Color((byte)142,(byte)114,(byte)176, (byte)100);
    img400a.draw().line(200, -100, 200, 500, c, 5, true);
    
    //left to right line all the way through
    c = new Color((byte)44,(byte)214,(byte)55, (byte)100);
    img400a.draw().line(-100, -100, 500, 500, c, 5, true);
    
    //right to left line all the way through
    c = new Color((byte)144,(byte)114,(byte)55, (byte)100);
    img400a.draw().line(400, 0, 0, 400, c, 5, true);

    assertEquals("c8b7fb4575b1d695c06615b03423cbacb70a760b58f9beaeba6df4f882a499c0", TestUtils.hashByteArray(img400a.getArray()));
  }
  
}
