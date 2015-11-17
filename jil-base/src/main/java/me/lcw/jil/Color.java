package me.lcw.jil;

/**
 * This class is how colors are passed around to/from the Image Object.
 * 
 * Colors are technically *read-only*.  There are methods to make a duplicate
 * color with a changed color channel.
 * 
 */
public class Color implements Comparable<Color> {
  public static final byte MAX_BYTE = (byte)255;
  public static final byte EMPTY_BYTE = (byte)0;
  
  public static final Color BLACK = new Color(EMPTY_BYTE);
  public static final Color WHITE = new Color(MAX_BYTE);
  public static final Color GREY = new Color((byte)127);
  public static final Color RED   = new Color(MAX_BYTE, EMPTY_BYTE, EMPTY_BYTE);
  public static final Color GREEN = new Color(EMPTY_BYTE, MAX_BYTE, EMPTY_BYTE);
  public static final Color BLUE  = new Color(EMPTY_BYTE, EMPTY_BYTE, MAX_BYTE);
  public static final Color ALPHA  = new Color(EMPTY_BYTE, EMPTY_BYTE, EMPTY_BYTE, EMPTY_BYTE);
  
  private final byte green;
  private final byte red;
  private final byte blue;
  private final byte grey;
  private final byte alpha;
  

  /**
   * Construct a Grey color.  Alpha is set to max value.
   * 
   * @param grey grey color level.
   */
  public Color(byte grey) {
    this.red = grey;
    this.green = grey;
    this.blue = grey;
    this.alpha = MAX_BYTE;
    this.grey = grey;
  }

  /**
   * Construct the color with RGB values set.  Alpha will be max value.
   * 
   * @param red red color level
   * @param green green color level
   * @param blue blue color level
   */
  public Color(byte red, byte green, byte blue) {
    this(red, green, blue, MAX_BYTE);
  }
  
  /**
   * Construct the color with RGBA values set.
   * 
   * @param red red color level
   * @param green green color level
   * @param blue blue color level
   * @param alpha alpha level
   */
  public Color(byte red, byte green, byte blue, byte alpha) {
    this.red = red;
    this.green = green;
    this.blue = blue;
    this.alpha = alpha;
    this.grey = Utils.colorsToGrey(red, green, blue);
  }

  /**
   * Creates a duplicate of this color with the BlueChannel changed.
   * 
   * @param lblue value to set the blue color of the new color too.
   */
  public Color changeBlue(byte lblue) {
    return new Color(red, green, lblue, alpha);
  }
  
  /**
   * 
   * @return the value of the blue channel for this color.
   */
  public byte getBlue() {
    return blue;
  }
  
  public double getBluePct() {
    return (blue & 0xff)/255.0;
  }

  /**
   * Creates a duplicate of this color with the RedChannel changed.
   * 
   * @param lred value to set the blue color of the new color too.
   */
  public Color changeRed(byte lred) {
    return new Color(lred, green, blue, alpha);
  }
  
  /**
   * 
   * @return the value of the red channel for this color.
   */
  public byte getRed() {
    return red;
  }
  
  public double getRedPct() {
    return (red & 0xff)/255.0;
  }

  /**
   * Creates a duplicate of this color with the GreenChannel changed.
   * 
   * @param lgreen value to set the blue color of the new color too.
   */
  public Color changeGreen(byte lgreen) {
    return new Color(red, lgreen, blue, alpha);
  }
  
  /**
   * 
   * @return the value of the green channel for this color.
   */
  public byte getGreen() {
    return green;
  }
  
  public double getGreenPct() {
    return (green & 0xff)/255.0;
  }

  /**
   * Creates a duplicate of this color with the AlphaChannel changed.
   * 
   * @param lalpha value to set the alpha color of the new color too.
   */
  public Color changeAlpha(byte lalpha) {
    return new Color(red, green, blue, lalpha);
  }
  
  /**
   * 
   * @return the value of the Alpha channel for this color.
   */
  public byte getAlpha() {
    return alpha;
  }
  
  public double getAlphaPct() {
    return (alpha & 0xff)/255.0;
  }

  /**
   * Creates a duplicate of this color with the GreyChannel changed.
   * 
   * @param lgrey value to set the grey color of the new color too.
   */
  public Color changeGrey(byte lgrey) {
    return new Color(lgrey);
  }
  
  /**
   * 
   * @return the value of the Grey channel for this color.
   */
  public byte getGrey() {
    return grey;
  }
  
  public double getGreyPct() {
    return (grey & 0xff)/255.0;
  }
  
  public int getARGB() {
    return alpha<<24 & 0xff000000 | red<<16 & 0xff0000 | green<<8 & 0xff00 | blue & 0xff;
  }
  
  public int getRGBA() {
    return red<<24 & 0xff000000 | green<<16 & 0xff0000 | blue<<8 & 0xff00 |alpha & 0xff;
  }
  
  public int getRGB() {
    return red<<16 & 0xff0000 | green<<8 & 0xff00 | blue & 0xff;
  }
  
  @Override
  public String toString() {
    return "Colors: R:"+(red&0xff)+" G:"+(green&0xff)+" B:"+(blue&0xff)+" Alpha:"+(alpha&0xff)+" grey:"+(grey&0xff);
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
  
  public boolean equalsNoAlpha(Object o) {
    if(!(o instanceof Color)) {
      return false;
    }
    Color c = (Color) o;
    long t = getRGB() & 0xffffffffL;
    long ot = c.getRGB() & 0xffffffffL;
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


