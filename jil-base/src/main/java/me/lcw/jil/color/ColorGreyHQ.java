package me.lcw.jil.color;

import me.lcw.jil.Color;

public class ColorGreyHQ implements Color {
  private final double grey;
  
  public ColorGreyHQ(double grey) {
    this.grey = Math.min(1.0, Math.max(0.0,grey));
  }
  
  @Override
  public Color changeBlue(byte lblue) {
    return Color.fromRGBBytes(getGreyByte(), getGreyByte(), lblue);
  }

  @Override
  public Color changeBlue(double lblue) {
    return Color.fromRGBPCT(getGreyPct(), getGreyPct(), lblue);
  }

  @Override
  public byte getBlueByte() {
    return getGreyByte();
  }

  @Override
  public double getBluePct() {
    return this.getGreyPct();
  }

  @Override
  public Color changeRed(byte lred) {
    return Color.fromRGBBytes(lred, getGreyByte(), getGreyByte());
  }

  @Override
  public Color changeRed(double lred) {
    return Color.fromRGBPCT(lred, getGreyPct(), this.getGreyPct());
  }

  @Override
  public byte getRedByte() {
    return getGreyByte();
  }

  @Override
  public double getRedPct() {
    return this.getGreyPct();
  }

  @Override
  public Color changeGreen(byte lgreen) {
    return Color.fromRGBBytes(getGreyByte(), lgreen, getGreyByte());
  }

  @Override
  public Color changeGreen(double lgreen) {
    return Color.fromRGBPCT(getGreyPct(), lgreen, this.getGreyPct());
  }

  @Override
  public byte getGreenByte() {
    return getGreyByte();
  }

  @Override
  public double getGreenPct() {
    return this.getGreyPct();
  }

  @Override
  public Color changeAlpha(byte lalpha) {
    return Color.fromRGBABytes(getGreyByte(), getGreyByte(), getGreyByte(), lalpha);
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
    return (byte)(grey*255);
  }

  @Override
  public double getGreyPct() {
    return grey;
  }

  @Override
  public int getARGB() {
    return MAX_BYTE<<24 & 0xff000000 | getGreyByte()<<16 & 0xff0000 | getGreyByte()<<8 & 0xff00 | getGreyByte() & 0xff;
  }

  @Override
  public int getRGBA() {
    return getGreyByte()<<24 & 0xff000000 | getGreyByte()<<16 & 0xff0000 | getGreyByte()<<8 & 0xff00 |MAX_BYTE & 0xff;
  }

  @Override
  public int getRGB() {
    return getGreyByte()<<16 & 0xff0000 | getGreyByte()<<8 & 0xff00 | getGreyByte() & 0xff;
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
