package me.lcw.jil.parsers.tiff;

import java.io.IOException;

public class TIFFConstants {
  
  enum Endianness {BIG, LITTLE}
  
  public static int getShort(Endianness type, short data) throws IOException {
    switch(type) {
    case BIG:
      return data;
    case LITTLE:
      return (int) ( (data & 0xff)<<8 | (data>>8) & 0xff );
    default:
      throw new IOException("problems converting endianness");  
    }
  }

  
  public static long getInt(Endianness type, int data) throws IOException {
    switch(type) {
    case BIG:
      return data;
    case LITTLE:
      return (int) ((data & 0xff) <<24) | (((data >> 8) & 0xff) <<16) |
          (((data >> 16) & 0xff) <<8) | ((data >> 24) & 0xff );
    }
    throw new IOException("problems converting endianness");
  }
  
  public static class TiffDirectory {
    private int width;
    private int height;
    private byte bpp = 24;
    private int imageOffset;
    public int getWidth() {
      return width;
    }
    public void setWidth(int width) {
      this.width = width;
    }
    public int getHeight() {
      return height;
    }
    public void setHeight(int height) {
      this.height = height;
    }
    public byte getBpp() {
      return bpp;
    }
    public void setBpp(byte bpp) {
      this.bpp = bpp;
    }
    public int getImageOffset() {
      return imageOffset;
    }
    public void setImageOffset(int imageOffset) {
      this.imageOffset = imageOffset;
    }
  }

}
