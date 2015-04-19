package me.lcw.jil;

import static org.junit.Assert.assertEquals;
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
}
