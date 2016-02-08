package me.lcw.jil.parsers.tiff;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import me.lcw.jil.BaseImage;
import me.lcw.jil.JilImage;
import me.lcw.jil.parsers.tiff.TIFFConstants.Endianness;
import me.lcw.jil.parsers.tiff.TIFFConstants.TiffDirectory;

public class TIFFDecoder {

  public static JilImage decode(byte[] ba) throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(bais); 
    Endianness e = getType(dis);
    TiffDirectory td = getTiffDirectory(e, dis);
    byte[] data = new byte[td.getHeight()*td.getWidth()*3];
    System.arraycopy(ba, td.getImageOffset(), data, 0, data.length);
    return JilImage.fromByteArray(BaseImage.MODE.RGB, td.getWidth(), td.getHeight(), data);
  }
  
  public static JilImage decodeFromFile(File file) throws IOException {
    RandomAccessFile raf = new RandomAccessFile(file, "r");
    Endianness e = getType(raf);
    TiffDirectory td = getTiffDirectory(e, raf);
    raf.seek(td.getImageOffset());
    byte[] data = new byte[td.getHeight()*td.getWidth()*3];
    raf.read(data);
    raf.close();
    return JilImage.fromByteArray(BaseImage.MODE.RGB, td.getWidth(), td.getHeight(), data);
  }
  
  
  
  private static Endianness getType(DataInput stream) throws IOException {
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
    fortytwo = TIFFConstants.getShort(bt, stream.readShort());

    if (fortytwo != 42) {
      throw new IOException("Could not read TIFF file");
    }
    return bt;
  }
  
  private static TiffDirectory getTiffDirectory(Endianness bt, DataInput data) throws IOException {
    TiffDirectory td = new TiffDirectory();
    long pos = TIFFConstants.getInt(bt, data.readInt());
    if(pos > 8) {
      while(pos > 8) {
        data.readByte();
        pos--;
      }
    }

    int items = TIFFConstants.getShort(bt, data.readShort());
    
    for(int i = 0; i < items; i++) {
      
      short tmp = data.readShort();
      int entry = TIFFConstants.getShort(bt, tmp);
      int type = TIFFConstants.getShort(bt, data.readShort());
      TIFFConstants.getInt(bt, data.readInt()); //size, dont care
      if (entry == 0x100) {
        if (type == 3) {
          td.setWidth(TIFFConstants.getShort(bt,data.readShort()));
          data.readShort();
        } else {
          td.setWidth((int)TIFFConstants.getInt(bt,data.readInt()));
        }
        
      } else if(entry == 0x101) {
        if (type == 3) {
          td.setHeight(TIFFConstants.getShort(bt,data.readShort()));
          data.readShort();
        } else {
          td.setHeight((int)TIFFConstants.getInt(bt,data.readInt()));
        }
      } else if(entry == 0x106) {
        int colorType =  TIFFConstants.getShort(bt,data.readShort());
        TIFFConstants.getShort(bt,data.readShort());
        if (colorType != 2) {
          //throw new IOException("TIFF not an RGB: "+colorType);
        }
      } else if(entry == 0x111) {
        if (type == 3) {
          td.setImageOffset(TIFFConstants.getShort(bt,data.readShort()));
          data.readShort();          
        } else {
          td.setImageOffset((int)TIFFConstants.getInt(bt,data.readInt()));
        }
      } else {
        if (type == 3) {
          data.readShort();
          data.readShort();
        } else {

        }
      }
    }
    return td;
  }
}
