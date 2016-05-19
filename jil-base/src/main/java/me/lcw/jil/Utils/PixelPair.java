package me.lcw.jil.Utils;

public class PixelPair {
  public final int x;
  public final int y;

  public PixelPair(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public int hashCode() {
    return x+y;
  }

  @Override
  public boolean equals(Object o) {
    if(o instanceof PixelPair) {
      PixelPair pp = (PixelPair)o;
      if(pp.x == x && pp.y == y) {
        return true;
      }
    }
    return false;
  }
}