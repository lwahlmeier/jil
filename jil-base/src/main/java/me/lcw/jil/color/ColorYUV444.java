package me.lcw.jil.color;

import me.lcw.jil.Color;
import me.lcw.jil.Utils.ColorUtils;

public class ColorYUV444 implements Color {
   
  protected final byte green;
  protected final byte red;
  protected final byte blue;
 

  /**
   * Construct the color with RGB values set.  Alpha will be max value.
   * 
   * @param red red color level
   * @param green green color level
   * @param blue blue color level
   */
  public ColorYUV444(byte red, byte green, byte blue) {
    this.red = red;
    this.green = green;
    this.blue = blue;
  }
  
  @Override
  public ColorYUV444 changeBlue(byte lblue) {
    return new ColorYUV444(red, green, lblue);
  }
  
  @Override
  public byte getBlueByte() {
    return blue;
  }
  
  @Override
  public double getBluePct() {
    return (blue & 0xff)/255.0;
  }

  @Override
  public ColorYUV444 changeRed(byte lred) {
    return new ColorYUV444(lred, green, blue);
  }
  
  @Override
  public byte getRedByte() {
    return red;
  }
  
  public double getRedPct() {
    return (red & 0xff)/255.0;
  }

  @Override
  public ColorYUV444 changeGreen(byte lgreen) {
    return new ColorYUV444(red, lgreen, blue);
  }
  
  @Override
  public byte getGreenByte() {
    return green;
  }

  @Override
  public double getGreenPct() {
    return (green & 0xff)/255.0;
  }

  @Override
  public ColorRGBA8888 changeAlpha(byte lalpha) {
    return new ColorRGBA8888(red, green, blue, lalpha);
  }
  
  @Override
  public ColorRGBAHQ changeAlpha(double lalpha) {
    return new ColorRGBAHQ(this.getRedPct(), this.getGreenPct(), this.getBluePct(), lalpha);
  }
  
  @Override
  public byte getAlphaByte() {
    return MAX_BYTE;
  }
  
  @Override
  public double getAlphaPct() {
    return 1.0;
  }

  @Override
  public ColorGrey8 changeGrey(byte lgrey) {
    return new ColorGrey8(lgrey);
  }
  
  @Override
  public byte getGreyByte() {
    return ColorUtils.colorsToGrey(red, green, blue);
  }
  
  @Override
  public double getGreyPct() {
    return (getGreyByte() & 0xff)/255.0;
  }
  
  @Override
  public int getARGB() {
    return MAX_BYTE<<24 & 0xff000000 | red<<16 & 0xff0000 | green<<8 & 0xff00 | blue & 0xff;
  }
  
  public int getRGBA() {
    return red<<24 & 0xff000000 | green<<16 & 0xff0000 | blue<<8 & 0xff00 |MAX_BYTE & 0xff;
  }
  
  public int getRGB() {
    return red<<16 & 0xff0000 | green<<8 & 0xff00 | blue & 0xff;
  }
  
  @Override
  public String toString() {
    return String.format("ColorRGBByte: R:%1.5f, G:%1.5f, B:%1.5f", this.getRedPct(), this.getGreenPct(), this.getBluePct());
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

  @Override
  public ColorRGBHQ changeBlue(double lblue) {
    return new ColorRGBHQ(this.getRedPct(), this.getGreenPct(), lblue);
  }

  @Override
  public Color changeRed(double lred) {
    return new ColorRGBHQ(lred, this.getGreenPct(), this.getBluePct());
  }

  @Override
  public Color changeGreen(double lgreen) {
    return new ColorRGBHQ(this.getRedPct(), lgreen, this.getBluePct());
  }

  @Override
  public Color changeGrey(double lgrey) {
    return new ColorGreyHQ(lgrey);
  }
}
