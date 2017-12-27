package me.lcw.jil.color;

import me.lcw.jil.Color;

public class ColorRGBA8888 extends ColorRGB888 {
   
  private final byte alpha;
 

    
  /**
   * Construct the color with RGBA values set.
   * 
   * @param red red color level
   * @param green green color level
   * @param blue blue color level
   * @param alpha alpha level
   */
  public ColorRGBA8888(byte red, byte green, byte blue, byte alpha) {
    super(red, green, blue);
    this.alpha = alpha;
  }

  @Override
  public ColorRGBA8888 changeRed(byte lred) {
    return new ColorRGBA8888(lred, green, blue, alpha);
  }
  
  @Override
  public ColorRGBA8888 changeGreen(byte lgreen) {
    return new ColorRGBA8888(red, lgreen, blue, alpha);
  }
  
  @Override
  public ColorRGB888 changeBlue(byte lblue) {
    return new ColorRGBA8888(red, green, lblue, alpha);
  }

  @Override
  public ColorRGBA8888 changeAlpha(byte lalpha) {
    return new ColorRGBA8888(red, green, blue, lalpha);
  }
  
  @Override
  public ColorRGBAHQ changeBlue(double lblue) {
    return new ColorRGBAHQ(this.getRedPct(), this.getGreenPct(), lblue, this.getAlphaPct());
  }

  @Override
  public ColorRGBAHQ changeRed(double lred) {
    return new ColorRGBAHQ(lred, this.getGreenPct(), this.getBluePct(), this.getAlphaPct());
  }

  @Override
  public ColorRGBAHQ changeGreen(double lgreen) {
    return new ColorRGBAHQ(this.getRedPct(), lgreen, this.getBluePct(), this.getAlphaPct());
  }
  
  @Override
  public ColorRGBAHQ changeAlpha(double lalpha) {
    return new ColorRGBAHQ(this.getRedPct(), this.getGreenPct(), this.getBluePct(), lalpha);
  }
  
  @Override
  public byte getAlphaByte() {
    return alpha;
  }
  
  @Override
  public double getAlphaPct() {
    return (alpha & 0xff)/255.0;
  }

  public int getARGB() {
    return alpha<<24 & 0xff000000 | red<<16 & 0xff0000 | green<<8 & 0xff00 | blue & 0xff;
  }
  
  public int getRGBA() {
    return red<<24 & 0xff000000 | green<<16 & 0xff0000 | blue<<8 & 0xff00 |alpha & 0xff;
  }
  
  @Override
  public String toString() {
    return String.format("ColorRGBAByte: R:%1.5f, G:%1.5f, B:%1.5f, A:%1.5f", this.getRedPct(), this.getGreenPct(), this.getBluePct(), this.getAlphaPct());
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
