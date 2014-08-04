package org.java_lcw.jil;

/**
 * This class is how colors are passed around to/from the Image Object
 * 
 * @author lcw - Luke Wahlmeier
 *
 */
public class Color implements Comparable<Color> {
  private static final byte MAX_BYTE = (byte)255;
  private static final byte EMPTY_BYTE = (byte)0;
  
  public static final Color BLACK = new Color(EMPTY_BYTE);
  public static final Color WHITE = new Color(MAX_BYTE);
  public static final Color GREY = new Color((byte)127);
  public static final Color RED   = new Color(MAX_BYTE, EMPTY_BYTE, EMPTY_BYTE);
  public static final Color GREEN = new Color(EMPTY_BYTE, MAX_BYTE, EMPTY_BYTE);
  public static final Color BLUE  = new Color(EMPTY_BYTE, EMPTY_BYTE, MAX_BYTE);
  public static final Color ALPHA  = new Color(EMPTY_BYTE, EMPTY_BYTE, EMPTY_BYTE, EMPTY_BYTE);
  
  private byte green = EMPTY_BYTE;
  private byte red = EMPTY_BYTE;
  private byte blue = EMPTY_BYTE;
  private byte grey = EMPTY_BYTE;
  private byte alpha = MAX_BYTE;

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
   * Construct the color object with an int thats ARGB
   * @param color
   */


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
    grey = EMPTY_BYTE;
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
    grey = EMPTY_BYTE;
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
    grey = EMPTY_BYTE;
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
    grey = EMPTY_BYTE;
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
    grey = EMPTY_BYTE;
  }

  /**
   * Set the Grey value on this Color Object
   * @param g
   */
  public void setGrey(byte g) {
    setL(g);
  }
  
  /**
   * Get the grey value on this color object(overrides all rgb values)
   * @return the grey scale of this color
   */
  public byte getGrey() {
    if(grey == EMPTY_BYTE) {
      grey = (byte) (((red&0xff)+(green&0xff)+(blue&0xff))/3);
    }
    return grey;
  }
  
  /**
   * Set the grey value on this color object(overrides all rgb values)
   * @param g
   */
  public void setL(byte g) {
    red = g;
    green = g;
    blue = g;
  }
  
  public void setARGB(int color) {
    setRGBA((byte)((color >> 16) & 0xFF), 
        (byte)((color >>  8) & 0xFF), 
        (byte)(color & 0xFF), 
        (byte)((color >> 24) & 0xFF));
  }
  
  public int getARGB() {
    return alpha<<24 & 0xff000000 | red<<16 & 0xff0000 | green<<8 & 0xff00 | blue & 0xff;
  }
  
  @Override
  public String toString() {
    return "Colors: R:"+red+" G:"+green+" B:"+blue+" Alpha:"+alpha+" grey:"+getGrey();
  }
  
  @Override
  public int hashCode() {
    return getARGB();
  }
  
  @Override
  public boolean equals(Object o) {
    if(!(o instanceof Color)) {
      return false;
    }

    Color c = (Color) o;
    long t = getARGB() & 0xffffffffL;
    long ot = c.getARGB() & 0xffffffffL;
    if (t == ot){
      return true;
    }
    return false;
    
  }

  @Override
  public int compareTo(Color o) {
    long t = getARGB() & (long)0xffffffffL;
    long ot = o.getARGB() & (long)0xffffffffL;
    if(t > ot) {
      return 1;
    } else if (t < ot) {
      return -1;
    }
    return 0;
  }
  
}


