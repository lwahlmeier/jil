package org.java_lcw.jil;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.java_lcw.jil.ImageException;

public class TiffFile {
  
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

  public static Endianness getType(RandomAccessFile stream) throws IOException {
    stream.seek(0L);
    byte[] head = new byte[2];
    int fortytwo;
    Endianness bt;
    //2 bytes for header (they are the same)
    head[0] = stream.readByte();
    head[1] = stream.readByte();

    if (head[0] == 'I' && head[1] == 'I') {
      bt = Endianness.LITTLE;
    } else if (head[0] == 'M' && head[1] == 'M'){ //There are 2 bytes...
      bt = Endianness.BIG;
    } else {
      throw new IOException("Unknown File Type (NOT TIFF)");
    }    
    //Next we need a short 42 This is really a check to make sure we got endianness right
    fortytwo = getShort(bt, stream.readShort());

    if (fortytwo != 42) {
      throw new IOException("Could not read TIFF file");
    }
    return bt;
  }
  
  public static TiffDirectory getTiffDirectory(Endianness bt, RandomAccessFile data) throws IOException {
    TiffDirectory td = new TiffDirectory();
    data.seek(4);
    long pos = getInt(bt, data.readInt());

    data.seek(pos);
    int items = getShort(bt, data.readShort());
    
    for(int i = 0; i < items; i++) {
      
      short tmp = data.readShort();
      int entry = getShort(bt, tmp);
      int type = getShort(bt, data.readShort());
      getInt(bt, data.readInt()); //size, dont care
      //System.out.println("ENTRY:"+entry+" TYPE:"+type+" SIZE:"+size);
      if (entry == 0x100) {
        if (type == 3) {
          td.setWidth(getShort(bt,data.readShort()));
          data.readShort();
        } else {
          td.setWidth((int)getInt(bt,data.readInt()));
        }
        
      } else if(entry == 0x101) {
        if (type == 3) {
          td.setHeight(getShort(bt,data.readShort()));
          data.readShort();
        } else {
          td.setHeight((int)getInt(bt,data.readInt()));
        }
      } else if(entry == 0x106) {
        int colorType =  getShort(bt,data.readShort());
        getShort(bt,data.readShort());
        if (colorType != 2) {
          //throw new IOException("TIFF not an RGB: "+colorType);
        }
      } else if(entry == 0x111) {
        if (type == 3) {
          td.setImageOffset(getShort(bt,data.readShort()));
          data.readShort();          
        } else {
          td.setImageOffset((int)getInt(bt,data.readInt()));
        }
      } else {
        if (type == 3) {
          data.readShort();
          data.readShort();
        } else {
          //System.out.println("unknown:"+getInt(bt, data.readInt()));
        }
      }
    }
    return td;
  }
    
  
  public static JilImage open(String filename) throws IOException, ImageException{
    RandomAccessFile file = new RandomAccessFile(filename, "r");
    Endianness bt = getType(file);
    TiffDirectory td = getTiffDirectory(bt, file);
    file.seek(td.getImageOffset());
    byte[] data = new byte[td.getHeight()*td.getWidth()*3];
    file.read(data);
    return JilImage.fromByteArray(JilImage.MODE_RGB, td.getWidth(), td.getHeight(), data);
  }
  
  public static void save(String saveTo, Image image) throws IOException, ImageException {
    final byte[] data = image.changeMode(Image.MODE_RGB).getArray();
    DataOutputStream out = new DataOutputStream(new FileOutputStream(saveTo));
    //TODO: Nothing seems to work with bigI here anymore??
    Endianness bt = Endianness.LITTLE;
    out.write('I');
    out.write('I');
    out.writeShort(getShort(bt, (short)42));
    out.writeInt((int) getInt(bt, 8));
    
    out.writeShort(getShort(bt, (short)9));
    long offset = out.size()+(9*12)+4;
    
    /*Write Height*/
    out.writeShort(getShort(bt, (short)256));
    out.writeShort(getShort(bt, (short)4));
    out.writeInt((int) getInt(bt, (int)1));
    out.writeInt((int) getInt(bt, image.getWidth()));
    /*Write Width*/
    out.writeShort(getShort(bt, (short)257));
    out.writeShort(getShort(bt, (short)4));
    out.writeInt((int) getInt(bt, (int)1));
    out.writeInt((int) getInt(bt, image.getHeight()));
    /*Bits per Sample(color)*/
    out.writeShort(getShort(bt, (short)258));
    out.writeShort(getShort(bt, (short)3));
    out.writeInt((int) getInt(bt, (int)3));
    out.writeShort(getShort(bt, (short) offset));
    out.writeShort(getShort(bt, (short) 0));
    offset+=6;
    /*Compression*/
    out.writeShort(getShort(bt, (short)259));
    out.writeShort(getShort(bt, (short)3));
    out.writeInt((int) getInt(bt, (int)1));
    out.writeShort(getShort(bt, (short)1)); //Uncompressed
    out.writeShort(getShort(bt, (short)0));
    /*Photometric Interpretation*/
    out.writeShort(getShort(bt, (short)262));
    out.writeShort(getShort(bt, (short)3));
    out.writeInt((int) getInt(bt, (int)1));
    out.writeShort(getShort(bt, (short)2)); //2 = RGB
    out.writeShort(getShort(bt, (short)0));
    /*Strips Offset, where the image starts...*/
    out.writeShort(getShort(bt, (short)273));
    out.writeShort(getShort(bt, (short)4));
    out.writeInt((int) getInt(bt, (int)1));
    out.writeInt((int) getInt(bt, (int)offset));
    offset+=data.length;
    /*Samples Per Pixel*/
    out.writeShort(getShort(bt, (short)277));
    out.writeShort(getShort(bt, (short)3));
    out.writeInt((int) getInt(bt, (int)1));
    out.writeShort(getShort(bt, (short)3));
    out.writeShort(getShort(bt, (short)0));
    /*Rows Per Strip*/
    out.writeShort(getShort(bt, (short)278));
    out.writeShort(getShort(bt, (short)4));
    out.writeInt((int) getInt(bt, (int)1));
    out.writeInt((int) getInt(bt,image.getHeight()));
    /*Strip Byte Count*/
    out.writeShort(getShort(bt, (short)279));
    out.writeShort(getShort(bt, (short)4));
    out.writeInt((int) getInt(bt, (int)1));
    out.writeInt((int) getInt(bt, (int) image.getWidth()*image.getHeight()*(3)));
    /*end of header*/
    out.writeInt((int) getInt(bt, 0));
    
    /*byte counts*/
    out.writeShort(getShort(bt, (short)8));
    out.writeShort(getShort(bt, (short)8));
    out.writeShort(getShort(bt, (short)8));
    
    out.write(data);
    out.close();
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
