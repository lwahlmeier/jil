package me.lcw.jil;

import me.lcw.jil.color.ColorGrey8;
import me.lcw.jil.color.ColorGreyHQ;
import me.lcw.jil.color.ColorRGB888;
import me.lcw.jil.color.ColorRGBA8888;
import me.lcw.jil.color.ColorRGBAHQ;
import me.lcw.jil.color.ColorRGBHQ;

/**
 * This class is how colors are passed around to/from the Image Object.
 * 
 * Colors are technically *read-only*.  There are methods to make a duplicate
 * color with a changed color channel.
 * 
 */
public interface Color extends Comparable<Color> {
  public static final byte MAX_BYTE = (byte)255;
  public static final byte EMPTY_BYTE = (byte)0;
  
  public static final Color BLACK = Color.fromGreyByte(EMPTY_BYTE);
  public static final Color WHITE = Color.fromGreyByte(MAX_BYTE);
  public static final Color GREY = Color.fromGreyByte((byte)127);
  public static final Color RED   = Color.fromRGBBytes(MAX_BYTE, EMPTY_BYTE, EMPTY_BYTE);
  public static final Color GREEN = Color.fromRGBBytes(EMPTY_BYTE, MAX_BYTE, EMPTY_BYTE);
  public static final Color BLUE  = Color.fromRGBBytes(EMPTY_BYTE, EMPTY_BYTE, MAX_BYTE);
  public static final Color ALPHA  = Color.fromRGBABytes(EMPTY_BYTE, EMPTY_BYTE, EMPTY_BYTE, EMPTY_BYTE);
  
  public static Color fromGreyByte(byte grey) {
    return new ColorGrey8(grey);
  }
  
  public static Color fromGreyPCT(double grey) {
    return new ColorGreyHQ(grey);
  }
  
  public static Color fromRGBBytes(byte red, byte green, byte blue) {
    return new ColorRGB888(red, green, blue);
  }
  
  public static Color fromRGBPCT(double red, double green, double blue) {
    return new ColorRGBHQ(red, green, blue);
  }
  
  public static Color fromRGBABytes(byte red, byte green, byte blue, byte alpha) {
    return new ColorRGBA8888(red, green, blue, alpha);
  }
  
  public static Color fromRGBAPCT(double red, double green, double blue, double alpha) {
    return new ColorRGBAHQ(red, green, blue, alpha);
  }
  
  public static Color fromARGB(int c) {
    byte alpha = (byte)(c >> 24);
    byte red = (byte)(c >> 16 &0xff);
    byte green = (byte)(c >> 8 &0xff);
    byte blue = (byte)(c &0xff);
    return new ColorRGBA8888(red, green, blue, alpha);
  }
  
  public static Color fromRGBA(int c) {
    byte red = (byte)(c >> 24);
    byte green = (byte)(c >> 16 &0xff);
    byte blue = (byte)(c >> 8 &0xff);
    byte alpha = (byte)(c &0xff);
    return new ColorRGBA8888(red, green, blue, alpha);
  }

  /**
   * Creates a duplicate of this color with the BlueChannel changed.
   * 
   * @param lblue value to set the blue color of the new color too.
   */
  public Color changeBlue(byte lblue);
  public Color changeBlue(double lblue);
  
  /**
   * 
   * @return the value of the blue channel for this color.
   */
  public byte getBlueByte();
  
  public double getBluePct();

  /**
   * Creates a duplicate of this color with the RedChannel changed.
   * 
   * @param lred value to set the blue color of the new color too.
   */
  public Color changeRed(byte lred);
  public Color changeRed(double lred);
  
  /**
   * 
   * @return the value of the red channel for this color.
   */
  public byte getRedByte();
  
  public double getRedPct();

  /**
   * Creates a duplicate of this color with the GreenChannel changed.
   * 
   * @param lgreen value to set the blue color of the new color too.
   */
  public Color changeGreen(byte lgreen);
  public Color changeGreen(double lgreen);
  
  /**
   * 
   * @return the value of the green channel for this color.
   */
  public byte getGreenByte();
  
  public double getGreenPct();

  /**
   * Creates a duplicate of this color with the AlphaChannel changed.
   * 
   * @param lalpha value to set the alpha color of the new color too.
   */
  public Color changeAlpha(byte lalpha);
  public Color changeAlpha(double lalpha);
  
  /**
   * 
   * @return the value of the Alpha channel for this color.
   */
  public byte getAlphaByte();
  
  public double getAlphaPct();

  /**
   * Creates a duplicate of this color with the GreyChannel changed.
   * 
   * @param lgrey value to set the grey color of the new color too.
   */
  public Color changeGrey(byte lgrey);
  public Color changeGrey(double lgrey);
  
  /**
   * 
   * @return the value of the Grey channel for this color.
   */
  public byte getGreyByte();
  
  public double getGreyPct();
  
  public int getARGB();
  
  public int getRGBA();
  
  public int getRGB();
  
  public String toString();
  
  public int hashCode();
  
  public boolean equals(Object o);
  
  public default boolean equalsNoAlpha(Object o) {
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

  public default int compareTo(Color o) {
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


