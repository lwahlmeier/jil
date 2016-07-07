package me.lcw.jil.Utils;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import me.lcw.jil.BaseImage.MODE;
import me.lcw.jil.Color;
import me.lcw.jil.ImageException;
import me.lcw.jil.JilImage;

public class ImageCompareTests {

  
  @Test
  public void simpleCompareTest() throws ImageException, IOException {
    JilImage ji = JilImage.create(MODE.RGBA, 640, 480, Color.RED);
    JilImage ji2 = JilImage.create(MODE.RGBA, 640, 480, Color.RED);
    ji.draw().line(0, 0, 640, 480, Color.GREEN, 10, false);
    ji2.draw().line(640, 0, 0, 480, Color.BLUE, 10, false);
    ji2.draw().line(640, 0, 0, 480, Color.RED, 1, false);
    ji2.draw().rect(300, 220, 40, 40, Color.BLACK, 100, false);
    JilImage cji = ImageCompareUtils.diffImages(ji, ji2);
    JilImage jic = ji.copy();
    jic.merge(0, 0, cji);
    assertEquals(ji2, jic);
    cji = ImageCompareUtils.diffImages(ji2, ji);
    JilImage ji2c = ji2.copy();
    ji2c.merge(0, 0, cji);
    assertEquals(ji, ji2c);
  }
  
  
  @Test
  public void simpleCompareTest2() throws ImageException, IOException {
    JilImage ji = JilImage.create(MODE.RGB, 640, 480, Color.RED);
    JilImage ji2 = JilImage.create(MODE.RGB, 640, 480, Color.RED);
    ji.draw().line(0, 0, 640, 480, Color.GREEN, 10, false);
    ji2.draw().line(640, 0, 0, 480, Color.BLUE, 10, false);
    ji2.draw().line(640, 0, 0, 480, Color.RED, 1, false);
    ji2.draw().rect(300, 220, 40, 40, Color.BLACK, 100, false);
    JilImage cji = ImageCompareUtils.diffImages(ji, ji2);
    JilImage jic = ji.copy();
    jic.merge(0, 0, cji);
    assertEquals(ji2, jic);
    cji = ImageCompareUtils.diffImages(ji2, ji);
    JilImage ji2c = ji2.copy();
    ji2c.merge(0, 0, cji);
    assertEquals(ji, ji2c);
  }
  
  @Test(expected=IllegalStateException.class)
  public void simpleCompareTest3() throws ImageException, IOException {
    JilImage ji = JilImage.create(MODE.RGB, 640, 480, Color.RED);
    JilImage ji2 = JilImage.create(MODE.RGB, 640, 481, Color.RED);
    ji.draw().line(0, 0, 640, 480, Color.GREEN, 10, false);
    ji2.draw().line(640, 0, 0, 480, Color.BLUE, 10, false);
    ji2.draw().line(640, 0, 0, 480, Color.RED, 1, false);
    ji2.draw().rect(300, 220, 40, 40, Color.BLACK, 100, false);
    ImageCompareUtils.diffImages(ji, ji2);
  }
}
