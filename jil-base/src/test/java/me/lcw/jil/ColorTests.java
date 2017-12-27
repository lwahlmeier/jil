package me.lcw.jil;

import static org.junit.Assert.assertEquals;
import me.lcw.jil.BaseImage.ImageMode;
import me.lcw.jil.Color;

import org.junit.Test;

public class ColorTests {
  
  @Test
  public void colorTest() {
    Color c = Color.BLACK;
    Color nc;
    nc = c.changeRed((byte)10);
    nc = nc.changeBlue((byte)10);
    nc = nc.changeGreen((byte)10);
    assertEquals(nc.getRedByte(), 10);
    assertEquals(nc.getBlueByte(), 10);
    assertEquals(nc.getGreenByte(), 10);
    nc = nc.changeAlpha((byte)10);
    assertEquals(nc.getAlphaByte(), 10);
  }
  
  @Test
  public void whiteGreyScaleTest() {
    JilImage ji = JilImage.create(ImageMode.RGBA32, 10, 10, Color.WHITE);
    
    for(int i=0; i<1000; i++) {
      ji = ji.changeMode(ImageMode.GREY8);
      ji = ji.changeMode(ImageMode.RGB24);
      ji = ji.changeMode(ImageMode.GREY8);
      ji = ji.changeMode(ImageMode.RGBA32);
    }
    Color c = ji.getPixel(5, 5);
    ji=ji.changeMode(ImageMode.GREY8);
    Color c2 = ji.getPixel(5, 5);
    assertEquals(Color.WHITE, c);
    assertEquals(Color.WHITE, c2);
  }
  
  @Test
  public void blackGreyScaleTest() {
    JilImage ji = JilImage.create(ImageMode.RGBA32, 10, 10, Color.BLACK);
    for(int i=0; i<1000; i++) {
      ji = ji.changeMode(ImageMode.GREY8);
      ji = ji.changeMode(ImageMode.RGB24);
      ji = ji.changeMode(ImageMode.GREY8);
      ji = ji.changeMode(ImageMode.RGBA32);
    }
    Color c = ji.getPixel(5, 5);
    ji=ji.changeMode(ImageMode.GREY8);
    Color c2 = ji.getPixel(5, 5);
    assertEquals(Color.BLACK, c);
    assertEquals(Color.BLACK, c2);    
  }
  
  @Test
  public void greyGreyScaleTest() {
    JilImage ji = JilImage.create(ImageMode.RGBA32, 10, 10, Color.GREY);
    for(int i=0; i<1000; i++) {
      ji = ji.changeMode(ImageMode.GREY8);
      ji = ji.changeMode(ImageMode.RGB24);
      ji = ji.changeMode(ImageMode.GREY8);
      ji = ji.changeMode(ImageMode.RGBA32);
    }
    Color c = ji.getPixel(5, 5);
    ji=ji.changeMode(ImageMode.GREY8);
    Color c2 = ji.getPixel(5, 5);
    assertEquals(Color.GREY, c);
    assertEquals(Color.GREY, c2);    
  }
}
