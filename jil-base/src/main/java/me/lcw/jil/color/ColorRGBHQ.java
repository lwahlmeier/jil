package me.lcw.jil.color;

import me.lcw.jil.Color;
import me.lcw.jil.Utils.ColorUtils;

public class ColorRGBHQ implements Color {
   
  protected final double green;
  protected final double red;
  protected final double blue;
 

  /**
   * Construct the color with RGB values set.  Alpha will be max value.
   * 
   * @param red red color level
   * @param green green color level
   * @param blue blue color level
   */
  public ColorRGBHQ(double red, double green, double blue) {
    this.red = Math.min(1.0,Math.max(0.0,red));
    this.green = Math.min(1.0, Math.max(0.0,green));
    this.blue = Math.min(1.0, Math.max(0.0,blue));
  }
  
  @Override
  public ColorRGBHQ changeBlue(byte lblue) {
    return new ColorRGBHQ(red, green, lblue/255.0);
  }
  
  @Override
  public byte getBlueByte() {
    return (byte)(blue*255);
  }
  
  @Override
  public double getBluePct() {
    return blue;
  }

  @Override
  public ColorRGBHQ changeRed(byte lred) {
    return new ColorRGBHQ(lred/255.0, green, blue);
  }
  
  @Override
  public byte getRedByte() {
    return (byte)(red*255);
  }
  
  public double getRedPct() {
    return red;
  }

  @Override
  public ColorRGBHQ changeGreen(byte lgreen) {
    return new ColorRGBHQ(red, lgreen/255.0, blue);
  }
  
  @Override
  public byte getGreenByte() {
    return (byte)(red*255);
  }

  @Override
  public double getGreenPct() {
    return green;
  }

  @Override
  public ColorRGBAHQ changeAlpha(byte lalpha) {
    return new ColorRGBAHQ(red, green, blue, lalpha/255.0);
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
    return (byte)(ColorUtils.colorsToGrey(red, green, blue)*255);
  }
  
  @Override
  public double getGreyPct() {
    return ColorUtils.colorsToGrey(red, green, blue);
  }
  
  @Override
  public int getARGB() {
    return MAX_BYTE<<24 & 0xff000000 | getRedByte()<<16 & 0xff0000 | getGreenByte()<<8 & 0xff00 | getBlueByte() & 0xff;
  }
  
  public int getRGBA() {
    return getRedByte()<<24 & 0xff000000 | getGreenByte()<<16 & 0xff0000 | getBlueByte()<<8 & 0xff00 |MAX_BYTE & 0xff;
  }
  
  public int getRGB() {
    return getRedByte()<<16 & 0xff0000 | getGreenByte()<<8 & 0xff00 | getBlueByte() & 0xff;
  }
  
  @Override
  public String toString() {
    return String.format("ColorRGBHQ: R:%1.5f, G:%1.5f, B:%1.5f", this.getRedPct(), this.getGreenPct(), this.getBluePct());
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

  @Override
  public Color changeAlpha(double lalpha) {
    return new ColorRGBAHQ(this.getRedPct(), this.getGreenPct(), this.getBluePct(), lalpha);
  }
}
