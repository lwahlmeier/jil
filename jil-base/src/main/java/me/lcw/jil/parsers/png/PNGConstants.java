package me.lcw.jil.parsers.png;

public class PNGConstants {
  protected static int DEFAULT_COMPRESSION = 7;
  protected static final byte[] HEADER = new byte[] {
    (byte)0x89, (byte)0x50, (byte)0x4E, (byte)0x47, 
    (byte)0x0D, (byte)0x0A, (byte)0x1A, (byte)0x0A 
  };
  protected static final byte[] IHDR = "IHDR".getBytes();
  protected static final byte[] IDAT = "IDAT".getBytes();
  protected static final byte[] IEND = "IEND".getBytes();
  protected static final byte[] IEND_FOOTER = new byte[] {
      (byte)0,   (byte)0,   (byte)0,   (byte)0,
      (byte)0x49,(byte)0x45,(byte)0x4e,(byte)0x44,
      (byte)0xae,(byte)0x42,(byte)0x60,(byte)0x82
    };
  
  protected static final int HEADER_SIZE = 33;
  public static enum PNG_COLOR_TYPE {
    GREY((byte)0, 1), 
    RGB((byte)2, 3),
    RGBA((byte)6, 4);

    private final byte val;
    private final int bpp;
    
    PNG_COLOR_TYPE(byte val, int bpp) {
      this.val = val;
      this.bpp = bpp;
    }
    
    public byte getValue() {
      return val;
    }
    
    public int getBPP() {
      return bpp;
    }
    
  };
}
