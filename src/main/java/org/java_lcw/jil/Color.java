package org.java_lcw.jil;

/**
 * This class is how colors are passed around to/from the Image Object
 * 
 * @author lcw - Luke Wahlmeier
 *
 */
public class Color {
  private byte green = 0;
  private byte red = 0;
  private byte blue = 0;
  private byte alpha = (byte) 0xff;
  private byte grey = 0;

  /**
   * Construct the color with RGB values set
   * @param red
   * @param green
   * @param blue
   */
  public Color(byte red, byte green, byte blue) {
    setRGB((byte) red, (byte) green, (byte) blue);
  }
  /**
   * Construct the color with RGBA values set
   * @param red
   * @param green
   * @param blue
   * @param alpha
   */
  public Color(byte red, byte green, byte blue, byte alpha) {
    setRGBA((byte) red, (byte) green, (byte) blue, (byte)alpha);
  }

  /**
   * Construct the color with grey values set
   * @param grey
   */
  public Color(byte grey) {
    setL((byte) grey);
  }
  /**
   * Construct a Color object with no colors set (everything is 0)
   */
  public Color() {
  }

  public void merge(Color c) {
    double pct = ((c.alpha & 0xff) / 255.0);

    if (pct == 1.0) {
      this.setRGB(c.red, c.green, c.blue);
      this.setAlpha(c.alpha);
    } else if (pct == 0) {
      return;
    }else {
      double epct = 1.0-((c.alpha & 0xff) / 255.0);
      int tmp = (int) ((this.green & 0xff)*epct + ((c.green&0xff) * pct));

      if (tmp > 255) {
        this.green = (byte)255;
      } else {
        this.green = (byte)tmp;
      }
      tmp = (int) ((this.red & 0xff)*epct + ((c.red&0xff) * pct));
      if (tmp> 255) {
        this.red = (byte)255;
      } else {
        this.red = (byte)tmp;
      }

      tmp = (int) ((this.blue & 0xff)*epct + ((c.blue&0xff) * pct));
      if (tmp> 255) {
        this.blue = (byte)255;
      }else {
        this.blue = (byte)tmp;
      }
    }
  }

  /**
   * Set the blue value on this color object
   * @param b
   */
  public void setBlue(byte b) {
    blue = b;
  }
  public byte getBlue() {
    return blue;
  }

  /**
   * Set the Red value on this color object
   * @param r
   */
  public void setRed(byte r) {
    red = r;
  }
  public byte getRed() {
    return red;
  }

  /**
   * Set the green value on this color object
   * @param g
   */
  public void setGreen(byte g) {
    green = g;
  }
  public byte getGreen() {
    return green;
  }

  /**
   * Set the alpha value on this color object
   * @param a
   */
  public void setAlpha(byte a) {
    alpha = a;
  }
  public byte getAlpha() {
    return alpha;
  }
  /**
   * Set the RGB Value on this Color Object
   * @param r
   * @param g
   * @param b
   */
  public void setRGB(byte r, byte g, byte b) {
    red = r;
    green = g;
    blue = b;
    alpha = (byte)255;
  }
  /**
   * Set the RGBA Value on this Color Object
   * @param r
   * @param g
   * @param b
   * @param a
   */
  public void setRGBA(byte r, byte g, byte b, byte a) {
    red = r;
    green = g;
    blue = b;
    alpha = a;
  }

  /**
   * Set the Grey value on this Color Object
   * @param g
   */
  public void setGrey(byte g) {
    setL(g);
  }

  public byte getGrey() {
    return (byte) grey;//(((red&0xff)+(green&0xff)+(blue&0xff))/3);
  }
  /**
   * Set the grey value on this color object(overrides all rgb values)
   * @param g
   */
  public void setL(byte g) {
    grey = g;
    red = g;
    green = g;
    blue = g;
  }

  public String toString() {
    return "Colors: R:"+red+" G:"+green+" B:"+blue+" Alpha:"+alpha+" grey:"+getGrey();
  }
  
  public boolean equals(Color c) {
    if (c.alpha == this.alpha && 
        c.red == this.red &&
        c.green == this.green &&
        c.blue == this.blue){
      return true;
    }
    return false;
    
  }
  
}


