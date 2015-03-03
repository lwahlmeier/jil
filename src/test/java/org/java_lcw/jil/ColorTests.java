package org.java_lcw.jil;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ColorTests {
  
  @Test
  public void colorTest() {
    Color c = new Color();
    c.setRed((byte)10);
    c.setBlue((byte)10);
    c.setGreen((byte)10);
    assertEquals(c.getRed(), 10);
    assertEquals(c.getBlue(), 10);
    assertEquals(c.getGreen(), 10);
    c.setAlpha((byte)10);
    assertEquals(c.getAlpha(), 10);
  }
}
