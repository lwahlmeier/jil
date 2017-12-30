package me.lcw.jil;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;

import me.lcw.jil.BaseImage.ImageMode;

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
  public void loopTest() {
    for(int i=0; i<1000; i++) {
      Color c1 = Color.fromGreyByte((byte)ThreadLocalRandom.current().nextInt());
      Color c2 = Color.fromRGBBytes(c1.getRedByte(), c1.getGreenByte(), c1.getBlueByte());
      Color c3 = Color.fromRGBABytes(c1.getRedByte(), c1.getGreenByte(), c1.getBlueByte(), c1.getAlphaByte());
      
      Color c4 = Color.fromGreyPCT(c1.getGreyPct());
      Color c5 = Color.fromRGBAPCT(c1.getRedPct(), c1.getGreenPct(), c1.getBluePct(), c1.getAlphaPct());
      Color c6 = Color.fromRGBPCT(c1.getRedPct(), c1.getGreenPct(), c1.getBluePct());
      
      assertEquals(c1, c2);
      assertEquals(c1, c3);
      assertEquals(c2, c3);
      assertEquals(c1, c4);
      assertEquals(c1, c5);
      assertEquals(c1, c6);
    }
  }
  
  @Test
  public void loopLQtoHQTest() {
    for(int i=0; i<1000; i++) {
      Color c1 = Color.fromRGBABytes((byte)ThreadLocalRandom.current().nextInt(), (byte)ThreadLocalRandom.current().nextInt(), (byte)ThreadLocalRandom.current().nextInt(), (byte)ThreadLocalRandom.current().nextInt());
      Color c2 = Color.fromRGBBytes(c1.getRedByte(), c1.getGreenByte(), c1.getBlueByte());
      Color c3 = Color.fromGreyByte(c1.getGreyByte());
      
      Color c4 = Color.fromRGBAPCT(c1.getRedPct(), c1.getGreenPct(), c1.getBluePct(), c1.getAlphaPct());
      Color c5 = Color.fromRGBPCT(c1.getRedPct(), c1.getGreenPct(), c1.getBluePct());
      Color c6 = Color.fromGreyPCT(c1.getGreyPct());
      
      assertEquals(c1, c4);
      assertEquals(c2, c5);
      assertEquals(c3, c6);
    }
  }

  
  @Test
  public void MaxRedTest() {
    Color c1 = Color.fromRGBBytes(Color.MAX_BYTE, Color.EMPTY_BYTE, Color.EMPTY_BYTE);
    Color c2 = Color.fromRGBABytes(c1.getRedByte(), c1.getGreenByte(), c1.getBlueByte(), c1.getAlphaByte());

    Color c3 = Color.fromRGBPCT(c1.getRedPct(), c1.getGreenPct(), c1.getBluePct());
    Color c4 = Color.fromRGBAPCT(c1.getRedPct(), c1.getGreenPct(), c1.getBluePct(), c1.getAlphaPct());

    assertEquals(c1, c2);
    assertEquals(c1, c3);
    assertEquals(c2, c3);
    assertEquals(c1, c4);
  }
  
  @Test
  public void MaxGreenTest() {
    Color c1 = Color.fromRGBBytes(Color.EMPTY_BYTE, Color.MAX_BYTE, Color.EMPTY_BYTE);
    Color c2 = Color.fromRGBABytes(c1.getRedByte(), c1.getGreenByte(), c1.getBlueByte(), c1.getAlphaByte());

    Color c3 = Color.fromRGBPCT(c1.getRedPct(), c1.getGreenPct(), c1.getBluePct());
    Color c4 = Color.fromRGBAPCT(c1.getRedPct(), c1.getGreenPct(), c1.getBluePct(), c1.getAlphaPct());

    assertEquals(c1, c2);
    assertEquals(c1, c3);
    assertEquals(c2, c3);
    assertEquals(c1, c4);
  }
  
  @Test
  public void MaxBlueTest() {
    Color c1 = Color.fromRGBBytes(Color.EMPTY_BYTE, Color.EMPTY_BYTE, Color.MAX_BYTE);
    Color c2 = Color.fromRGBABytes(c1.getRedByte(), c1.getGreenByte(), c1.getBlueByte(), c1.getAlphaByte());

    Color c3 = Color.fromRGBPCT(c1.getRedPct(), c1.getGreenPct(), c1.getBluePct());
    Color c4 = Color.fromRGBAPCT(c1.getRedPct(), c1.getGreenPct(), c1.getBluePct(), c1.getAlphaPct());

    assertEquals(c1, c2);
    assertEquals(c1, c3);
    assertEquals(c2, c3);
    assertEquals(c1, c4);
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
