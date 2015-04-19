package me.lcw.jil;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.lcw.jil.Image.ImageType;

public class Utils {
  
  public static byte colorsToGrey(byte red, byte green, byte blue) {
    return (byte) (((red&0xff)+(green&0xff)+(blue&0xff))/3);
  }
  
  public static Color mergeColors(Color first, Color second) {
    byte nr;
    byte ng;
    byte nb;
    byte na;

    double pct = ((second.getAlpha() & 0xff) / 255.0);
    if (pct == 1.0) {
      return second;
    } else if (pct == 0) {
      return first;
    }else {
      double epct = 1.0-pct;
      double srcA = ((first.getAlpha()& 0xff) / 255.0);
      double newApct = (((srcA + ((first.getAlpha()&0xff) * epct))));
      
      int tmp = (int) (255 * newApct);
      if (tmp > 255) {
        na = Color.MAX_BYTE;
      }else {
        na = (byte)tmp;
      }
      tmp = (int) (((second.getRed()&0xff)*pct)+(first.getRed() & 0xff)*epct);
      if (tmp> 255) {
        nr = Color.MAX_BYTE;
      } else {
        nr = (byte)tmp;
      }
      tmp = (int) (((second.getGreen() & 0xff)*pct)+(first.getGreen() & 0xff)*epct);
      if (tmp > 255) {
        ng = Color.MAX_BYTE;
      } else {
        ng = (byte)tmp;
      }
      tmp = (int) (((second.getBlue() & 0xff)*pct)+(first.getBlue() & 0xff)*epct);
      if (tmp> 255) {
        nb = Color.MAX_BYTE;
      }else {
        nb = (byte)tmp;
      }
      return new Color(nr, ng, nb, na);
    }
  }
  
  public static List<int[]> lineToList(int x, int y, int x2, int y2) {
    List<int[]> list = new ArrayList<int[]>();
    int w = x2 - x ;
    int h = y2 - y ;
    int dx1 = 0;
    int dy1 = 0;
    int dx2 = 0;
    int dy2 = 0 ;
    if (w<0) { 
      dx1 = -1;
    } else if (w>0) {
      dx1 = 1 ;
    }
    if (h<0) {
      dy1 = -1;
    } else if (h>0) { 
      dy1 = 1 ;
    }
    if (w<0) {
      dx2 = -1;
    } else if (w>0) {
      dx2 = 1 ;
    }
    int longest = Math.abs(w) ;
    int shortest = Math.abs(h) ;
    if (!(longest>shortest)) {
      longest = Math.abs(h) ;
      shortest = Math.abs(w) ;
      if (h<0) {
        dy2 = -1 ;
      } else if (h>0) {
        dy2 = 1 ;
      }
      dx2 = 0 ;            
    }
    int numerator = longest >> 1;
      for (int i=0;i<=longest;i++) {
        list.add(new int[]{x, y});
        numerator += shortest ;
        if (!(numerator<longest)) {
          numerator -= longest ;
          x += dx1 ;
          y += dy1 ;
        } else {
          x += dx2 ;
          y += dy2 ;
        }
      }
      return list;
  }
  
  public static ImageType getImageType(File file) throws ImageException {
    return getImageType(file.getAbsolutePath());
  }
  
  public static ImageType getImageType(String filename) throws ImageException {
    String ext = filename.substring(filename.lastIndexOf('.')+1).toLowerCase();
    if (ext.equals("tiff") || ext.equals("tif")) {
      return ImageType.TIFF;
    } else if (ext.equals("png")) {
      return ImageType.PNG;
    } else if (ext.equals("jpg") || ext.equals("jpeg")){
      return ImageType.JPEG;
    }
    throw new ImageException("Could not determen file type");
  }
  
  public static java.awt.Color toAWTColor(Color c) {
    return new java.awt.Color( c.getRed()&0xff, c.getGreen()&0xff, c.getBlue()&0xff, c.getAlpha()&0xff);
  }
  
  public static Color toJILColor(java.awt.Color c) {
    return new Color( (byte)c.getRed(), (byte)c.getGreen(), (byte)c.getBlue(), (byte)c.getAlpha());
  }
  
  public static Image biCubicSmooth(Image img, int width, int height) {
    if(width > img.getWidth() || height > img.getHeight()) {
      throw new IllegalStateException("Both width and height must be less then the current image!!");
    }
    
    int scaleFactor = 7;
    Image nimg = img;
    Image resizedImg;
    while(true) {
      int runWidth = width;
      int runHeight = height;
      if(nimg.getWidth() > width) {
        runWidth = nimg.getWidth()-Math.max(1, (nimg.getWidth()/scaleFactor)); 
        if(runWidth < width) {
          runWidth = width;
        }
      }
      
      if(nimg.getHeight() > height) {
        runHeight = nimg.getHeight()-Math.max(1, (nimg.getHeight()/scaleFactor)); 
        if(runHeight < height) {
          runHeight = height;
        }
      }
      resizedImg = nimg.resize(runWidth, runHeight, false, Image.ScaleType.CUBIC);
      nimg = resizedImg;
      if(nimg.getWidth() == width && nimg.getHeight() == height) {
        break;
      }
    }
    return resizedImg;
  }

  public static byte[] bufferedImageToByteArray(BufferedImage bufferedImage) {
    if(bufferedImage.getType() == BufferedImage.TYPE_BYTE_GRAY) {
      return ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
    }
    return intsToBytes(bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null , 0, bufferedImage.getWidth()), (byte) 32);
  }
  
  public static byte[] intsToBytes(int[] array, byte bpp) {
    byte cp = (byte) (bpp/8);
    byte[] nArray = new byte[array.length*cp];
    for (int i =0; i< array.length; i++) {
      int c = i*cp;
      nArray[c] = (byte) ((array[i] >> 16) & 0xff);
      nArray[c+1] = (byte) ((array[i] >> 8) & 0xff);
      nArray[c+2] = (byte) (array[i] & 0xff);
      if (cp == 4) {
        nArray[c+3] = (byte) ((array[i] >> 24) & 0xff);
      }
    }
    return nArray;
  }
  
  public static int[] bytesToInts(byte mode, byte[] array) {
    int[] nArray = new int[array.length/(mode/8)];
    for (int i =0; i< nArray.length; i++) {
      int c = i*(mode/8);
      if (mode == 32 ) {
      nArray[i] =  ((array[c+3] << 24 ) & 0xff000000) | ((array[c] << 16) & 0xff0000) | 
          ((array[c+1] << 8) & 0xff00) | ((array[c+2]) & 0xff);
      //Dont hit these because we upsample to 32bit before we make BufferedImage
      } else if (mode == 24) {
        nArray[i] =  ((array[c] << 16)&0xff0000) | 
            ((array[c+1] << 8)&0xff00) | ((array[c+2])&0xff);        
      } else if (mode == 8) {
        nArray[i] =  (255<<24) | ((array[c] << 16)) | 
            ((array[c] << 8)) | ((array[c]));
      }
    }
    return nArray;
  }
  
  public static int[] getAspectSize(int origWidth, int origHeight, int width, int height) {
      int nw = origWidth;
      int nh = origHeight;
      double ratio = nw/(double)nh;
      if (nw != width) {
        nw = width;
        nh = (int)Math.floor(nw/ratio);
      }
      if (nh > height) {
        nh = height;
        nw = (int)Math.floor(nh*ratio);
      }
      width = nw;
      height = nh;
      return new int[]{width, height};
  }
}
