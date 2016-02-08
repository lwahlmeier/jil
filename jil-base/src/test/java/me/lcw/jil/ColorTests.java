package me.lcw.jil;

import static org.junit.Assert.assertEquals;
import me.lcw.jil.BaseImage.MODE;
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
    assertEquals(nc.getRed(), 10);
    assertEquals(nc.getBlue(), 10);
    assertEquals(nc.getGreen(), 10);
    nc = nc.changeAlpha((byte)10);
    assertEquals(nc.getAlpha(), 10);
  }
  
  @Test
  public void whiteGreyScaleTest() {
    JilImage ji = JilImage.create(MODE.RGBA, 10, 10, Color.WHITE);
    
    for(int i=0; i<1000; i++) {
      ji = ji.changeMode(MODE.GREY);
      ji = ji.changeMode(MODE.RGB);
      ji = ji.changeMode(MODE.GREY);
      ji = ji.changeMode(MODE.RGBA);
    }
    Color c = ji.getPixel(5, 5);
    ji=ji.changeMode(MODE.GREY);
    Color c2 = ji.getPixel(5, 5);
    assertEquals(Color.WHITE, c);
    assertEquals(Color.WHITE, c2);
  }
  
  @Test
  public void blackGreyScaleTest() {
    JilImage ji = JilImage.create(MODE.RGBA, 10, 10, Color.BLACK);
    for(int i=0; i<1000; i++) {
      ji = ji.changeMode(MODE.GREY);
      ji = ji.changeMode(MODE.RGB);
      ji = ji.changeMode(MODE.GREY);
      ji = ji.changeMode(MODE.RGBA);
    }
    Color c = ji.getPixel(5, 5);
    ji=ji.changeMode(MODE.GREY);
    Color c2 = ji.getPixel(5, 5);
    assertEquals(Color.BLACK, c);
    assertEquals(Color.BLACK, c2);    
  }
  
  @Test
  public void greyGreyScaleTest() {
    JilImage ji = JilImage.create(MODE.RGBA, 10, 10, Color.GREY);
    for(int i=0; i<1000; i++) {
      ji = ji.changeMode(MODE.GREY);
      ji = ji.changeMode(MODE.RGB);
      ji = ji.changeMode(MODE.GREY);
      ji = ji.changeMode(MODE.RGBA);
    }
    Color c = ji.getPixel(5, 5);
    ji=ji.changeMode(MODE.GREY);
    Color c2 = ji.getPixel(5, 5);
    assertEquals(Color.GREY, c);
    assertEquals(Color.GREY, c2);    
  }
}
