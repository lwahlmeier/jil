package me.lcw.jil.color;

import me.lcw.jil.Color;

public class ColorGrey8 implements Color {
  private final byte grey;
  
  public ColorGrey8(byte grey) {
    this.grey = grey;
  }

  @Override
  public Color changeBlue(byte lblue) {
    return Color.fromRGBBytes(grey, grey, lblue);
  }

  @Override
  public Color changeBlue(double lblue) {
    return Color.fromRGBPCT(getGreyPct(), getGreyPct(), lblue);
  }

  @Override
  public byte getBlueByte() {
    return grey;
  }

  @Override
  public double getBluePct() {
    return this.getGreyPct();
  }

  @Override
  public Color changeRed(byte lred) {
    return Color.fromRGBBytes(lred, grey, grey);
  }

  @Override
  public Color changeRed(double lred) {
    return Color.fromRGBPCT(lred, getGreyPct(), this.getGreyPct());
  }

  @Override
  public byte getRedByte() {
    return grey;
  }

  @Override
  public double getRedPct() {
    return this.getGreyPct();
  }

  @Override
  public Color changeGreen(byte lgreen) {
    return Color.fromRGBBytes(grey, lgreen, grey);
  }

  @Override
  public Color changeGreen(double lgreen) {
    return Color.fromRGBPCT(getGreyPct(), lgreen, this.getGreyPct());
  }

  @Override
  public byte getGreenByte() {
    return grey;
  }

  @Override
  public double getGreenPct() {
    return this.getGreyPct();
  }

  @Override
  public Color changeAlpha(byte lalpha) {
    return Color.fromRGBABytes(grey, grey, grey, lalpha);
  }

  @Override
  public Color changeAlpha(double lalpha) {
    return Color.fromRGBAPCT(getGreyPct(), this.getGreyPct(), this.getGreyPct(), lalpha);
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
  public Color changeGrey(byte lgrey) {
    return Color.fromGreyByte(lgrey);
  }

  @Override
  public Color changeGrey(double lgrey) {
    return Color.fromGreyPCT(lgrey);
  }

  @Override
  public byte getGreyByte() {
    return grey;
  }

  @Override
  public double getGreyPct() {
    return grey/255.0;
  }

  @Override
  public int getARGB() {
    return MAX_BYTE<<24 & 0xff000000 | grey<<16 & 0xff0000 | grey<<8 & 0xff00 | grey & 0xff;
  }

  @Override
  public int getRGBA() {
    return grey<<24 & 0xff000000 | grey<<16 & 0xff0000 | grey<<8 & 0xff00 |MAX_BYTE & 0xff;
  }

  @Override
  public int getRGB() {
    return grey<<16 & 0xff0000 | grey<<8 & 0xff00 | grey & 0xff;
  }

  @Override
  public String toString() {
    return String.format("ColorGreyByte: G:%1.5f", this.getGreyPct());
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
}
