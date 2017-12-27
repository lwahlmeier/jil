package me.lcw.jil.parsers.tiff;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import me.lcw.jil.BaseImage;
import me.lcw.jil.parsers.tiff.TIFFConstants.Endianness;

public class TIFFEncoder {

  public static byte[] encode(BaseImage bi) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    encode(dos, bi);
    dos.flush();
    dos.close();
    return baos.toByteArray();
  }
  
  public static void encodeToFile(BaseImage bi, File file) throws IOException {
    if(file.exists()) {
      file.delete();
    }
    RandomAccessFile raf = new RandomAccessFile(file, "rw");
    encode(raf, bi);
    raf.close();
  }
  
  private static void encode(DataOutput out, BaseImage bi) throws IOException {
    final byte[] data = bi.changeMode(BaseImage.ImageMode.RGB24).getByteArray();
    //TODO: Nothing seems to work with bigI here anymore??
    Endianness bt = Endianness.LITTLE;
    out.write('I');
    out.write('I');
    out.writeShort(TIFFConstants.getShort(bt, (short)42));
    out.writeInt((int) TIFFConstants.getInt(bt, 8));
    
    out.writeShort(TIFFConstants.getShort(bt, (short)9));
    long offset = 10+(9*12)+4;
    
    /*Write Height*/
    out.writeShort(TIFFConstants.getShort(bt, (short)256));
    out.writeShort(TIFFConstants.getShort(bt, (short)4));
    out.writeInt((int) TIFFConstants.getInt(bt, (int)1));
    out.writeInt((int) TIFFConstants.getInt(bt, bi.getWidth()));
    /*Write Width*/
    out.writeShort(TIFFConstants.getShort(bt, (short)257));
    out.writeShort(TIFFConstants.getShort(bt, (short)4));
    out.writeInt((int) TIFFConstants.getInt(bt, (int)1));
    out.writeInt((int) TIFFConstants.getInt(bt, bi.getHeight()));
    /*Bits per Sample(color)*/
    out.writeShort(TIFFConstants.getShort(bt, (short)258));
    out.writeShort(TIFFConstants.getShort(bt, (short)3));
    out.writeInt((int) TIFFConstants.getInt(bt, (int)3));
    out.writeShort(TIFFConstants.getShort(bt, (short) offset));
    out.writeShort(TIFFConstants.getShort(bt, (short) 0));
    offset+=6;
    /*Compression*/
    out.writeShort(TIFFConstants.getShort(bt, (short)259));
    out.writeShort(TIFFConstants.getShort(bt, (short)3));
    out.writeInt((int) TIFFConstants.getInt(bt, (int)1));
    out.writeShort(TIFFConstants.getShort(bt, (short)1)); //Uncompressed
    out.writeShort(TIFFConstants.getShort(bt, (short)0));
    /*Photometric Interpretation*/
    out.writeShort(TIFFConstants.getShort(bt, (short)262));
    out.writeShort(TIFFConstants.getShort(bt, (short)3));
    out.writeInt((int) TIFFConstants.getInt(bt, (int)1));
    out.writeShort(TIFFConstants.getShort(bt, (short)2)); //2 = RGB
    out.writeShort(TIFFConstants.getShort(bt, (short)0));
    /*Strips Offset, where the image starts...*/
    out.writeShort(TIFFConstants.getShort(bt, (short)273));
    out.writeShort(TIFFConstants.getShort(bt, (short)4));
    out.writeInt((int) TIFFConstants.getInt(bt, (int)1));
    out.writeInt((int) TIFFConstants.getInt(bt, (int)offset));
    offset+=data.length;
    /*Samples Per Pixel*/
    out.writeShort(TIFFConstants.getShort(bt, (short)277));
    out.writeShort(TIFFConstants.getShort(bt, (short)3));
    out.writeInt((int) TIFFConstants.getInt(bt, (int)1));
    out.writeShort(TIFFConstants.getShort(bt, (short)3));
    out.writeShort(TIFFConstants.getShort(bt, (short)0));
    /*Rows Per Strip*/
    out.writeShort(TIFFConstants.getShort(bt, (short)278));
    out.writeShort(TIFFConstants.getShort(bt, (short)4));
    out.writeInt((int) TIFFConstants.getInt(bt, (int)1));
    out.writeInt((int) TIFFConstants.getInt(bt,bi.getHeight()));
    /*Strip Byte Count*/
    out.writeShort(TIFFConstants.getShort(bt, (short)279));
    out.writeShort(TIFFConstants.getShort(bt, (short)4));
    out.writeInt((int) TIFFConstants.getInt(bt, (int)1));
    out.writeInt((int) TIFFConstants.getInt(bt, bi.getWidth()*bi.getHeight()*(3)));
    /*end of header*/
    out.writeInt((int) TIFFConstants.getInt(bt, 0));
    
    /*byte counts*/
    out.writeShort(TIFFConstants.getShort(bt, (short)8));
    out.writeShort(TIFFConstants.getShort(bt, (short)8));
    out.writeShort(TIFFConstants.getShort(bt, (short)8));
    
    out.write(data);

  }
  
  
}
