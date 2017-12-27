package me.lcw.jil.color;

import me.lcw.jil.Color;

public class ColorRGBAHQ extends ColorRGBHQ {
   protected final double alpha;
 

  /**
   * Construct the color with RGB values set.  Alpha will be max value.
   * 
   * @param red red color level
   * @param green green color level
   * @param blue blue color level
   */
  public ColorRGBAHQ(double red, double green, double blue, double alpha) {
    super(red, green, blue);
    this.alpha = Math.min(1.0, Math.max(0.0,alpha));
  }
  
  @Override
  public ColorRGBAHQ changeBlue(byte lblue) {
    return new ColorRGBAHQ(red, green, lblue/255.0, alpha);
  }

  @Override
  public ColorRGBAHQ changeRed(byte lred) {
    return new ColorRGBAHQ(lred/255.0, green, blue, alpha);
  }

  @Override
  public ColorRGBAHQ changeGreen(byte lgreen) {
    return new ColorRGBAHQ(red, lgreen/255.0, blue, alpha);
  }

  @Override
  public ColorRGBAHQ changeAlpha(byte lalpha) {
    return new ColorRGBAHQ(red, green, blue, lalpha/255.0);
  }
  
  @Override
  public byte getAlphaByte() {
    return (byte)(alpha*255);
  }
  
  @Override
  public double getAlphaPct() {
    return alpha;
  }
  
  @Override
  public int getARGB() {
    return getAlphaByte()<<24 & 0xff000000 | getRedByte()<<16 & 0xff0000 | getGreenByte()<<8 & 0xff00 | getBlueByte() & 0xff;
  }
  
  @Override
  public int getRGBA() {
    return getRedByte()<<24 & 0xff000000 | getGreenByte()<<16 & 0xff0000 | getBlueByte()<<8 & 0xff00 |getAlphaByte() & 0xff;
  }
  
  @Override
  public String toString() {
    return String.format("ColorRGBAHQ: R:%1.5f, G:%1.5f, B:%1.5f, A:%1.5f", this.getRedPct(), this.getGreenPct(), this.getBluePct(), this.getAlphaPct());
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
  public ColorRGBAHQ changeBlue(double lblue) {
    return new ColorRGBAHQ(this.getRedPct(), this.getGreenPct(), lblue, this.getAlphaPct());
  }

  @Override
  public Color changeRed(double lred) {
    return new ColorRGBAHQ(lred, this.getGreenPct(), this.getBluePct(), this.getAlphaPct());
  }

  @Override
  public Color changeGreen(double lgreen) {
    return new ColorRGBAHQ(this.getRedPct(), lgreen, this.getBluePct(), this.getAlphaPct());
  }

  @Override
  public Color changeGrey(double lgrey) {
    return new ColorGreyHQ(lgrey);
  }
}
