package me.lcw.jil.parsers.png;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

import me.lcw.jil.BaseImage;
import me.lcw.jil.BaseImage.MODE;
import me.lcw.jil.parsers.png.PNGConstants.PNG_COLOR_TYPE;

public class PNGEncoder {

  public static byte[] encode(BaseImage bi) throws IOException {
    return encode(PNGConstants.DEFAULT_COMPRESSION, bi);
  }
  public static byte[] encode(int compression, BaseImage bi) throws IOException {
    PNG_COLOR_TYPE pct; 
    if(bi.getMode() == MODE.GREY) {
      pct = PNG_COLOR_TYPE.GREY;
    } else if(bi.getMode() == MODE.RGB) {
      pct = PNG_COLOR_TYPE.RGB;
    } else {
      pct = PNG_COLOR_TYPE.RGBA;
    }
    return encode(bi.getWidth(), bi.getHeight(), compression, pct, bi.getArray());
  }
  
  public static void encodeToFile(BaseImage bi, File file) throws IOException {
    encodeToFile(PNGConstants.DEFAULT_COMPRESSION, bi, file);
  }
  
  public static void encodeToFile(int compression, BaseImage bi, File file) throws IOException {
    PNG_COLOR_TYPE pct; 
    if(bi.getMode() == MODE.GREY) {
      pct = PNG_COLOR_TYPE.GREY;
    } else if(bi.getMode() == MODE.RGB) {
      pct = PNG_COLOR_TYPE.RGB;
    } else {
      pct = PNG_COLOR_TYPE.RGBA;
    }
    encodeToFile(bi.getWidth(), bi.getHeight(), compression, pct, bi.getArray(), file);
  }
  
  private static byte[] makeHeader(int width, int height, byte bd, PNG_COLOR_TYPE colorType) {
    CRC32 crc = new CRC32();
    byte[] finalBA = new byte[PNGConstants.HEADER_SIZE];
    ByteBuffer finalBB = ByteBuffer.wrap(finalBA);
    finalBB.put(PNGConstants.HEADER);
    finalBB.putInt(13);
    finalBB.put(PNGConstants.IHDR);
    finalBB.putInt(width);
    finalBB.putInt(height);
    finalBB.put(bd);
    finalBB.put(colorType.getValue());
    finalBB.put((byte)0);
    finalBB.put((byte)0);
    finalBB.put((byte)0);
    crc.update(finalBA, 12, 17);
    finalBB.putInt((int)crc.getValue());
    return finalBA;
  }
  
  private static byte[] compressArray(PNG_COLOR_TYPE pct, int width, int compression, byte[] data) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
    DeflaterOutputStream dos = new DeflaterOutputStream(baos, new Deflater(compression));
    
    for(int i=0; i<data.length; i+=width*pct.getBPP()) {
      dos.write(0);
      dos.write(data, i, width*pct.getBPP());
    }
    dos.finish();
    baos.flush();
    byte[] cba = baos.toByteArray();
    dos.close();
    baos.close();
    return cba;
  }

  private static void encodeToFile(int width, int height, int compression, PNG_COLOR_TYPE pct, byte[] data, File file) throws IOException {
    if(file.exists()) {
      file.delete();
    }
    CRC32 crc = new CRC32();
    byte[] cba = compressArray(pct, width, compression, data);
    RandomAccessFile raf = new RandomAccessFile(file, "rw");
    raf.write(makeHeader(width, height, (byte)8, pct));
    raf.writeInt(cba.length);
    raf.write(PNGConstants.IDAT);
    raf.write(cba);
    crc.update(PNGConstants.IDAT);
    crc.update(cba);
    raf.writeInt((int)crc.getValue());
    raf.write(PNGConstants.IEND_FOOTER);
    raf.close();
  }
  
  private static byte[] encode(int width, int height, int compression, PNG_COLOR_TYPE pct, byte[] data) throws IOException {
    CRC32 crc = new CRC32();
    byte[] cba = compressArray(pct, width, compression, data);
    byte[] finalBA = new byte[cba.length + PNGConstants.HEADER_SIZE + 12 + 12];
    ByteBuffer finalBB = ByteBuffer.wrap(finalBA);
    finalBB.put(makeHeader(width, height, (byte)8, pct));
    finalBB.putInt(cba.length);
    finalBB.put(PNGConstants.IDAT);
    finalBB.put(cba);
    crc.update(PNGConstants.IDAT);
    crc.update(cba);
    finalBB.putInt((int)crc.getValue());
    crc.reset();
    finalBB.put(PNGConstants.IEND_FOOTER);
    return finalBA;
   }
}
