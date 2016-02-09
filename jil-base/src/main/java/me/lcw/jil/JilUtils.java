package me.lcw.jil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.lcw.jil.BaseImage.ImageType;
import me.lcw.jil.BaseImage.MODE;

public class JilUtils {

  public static JilImage convertMode(JilImage image, MODE toMode) {
    switch(image.getMode()) {
    case GREY: {
      switch(toMode) {
      case GREY: {
        return image.copy();
      }
      case RGB: {
        return fromGreyToRGB(image);
      }
      case YUV: {
        return fromGreyToYUV(image);
      }
      case RGBA: {
        return fromGreyToRGBA(image);
      }
      default:
        throw new IllegalStateException("Invalid Type: "+toMode);
      }
    }
    case RGB: {
      switch(toMode) {
      case GREY: {
        return fromRGBToGrey(image);
      }
      case RGB: {
        return image.copy();
      }
      case YUV: {
        return fromRGBToYUV(image);
      }
      case RGBA: {
        return fromRGBToRGBA(image);
      }
      default:
        throw new IllegalStateException("Invalid Type: "+toMode);
      }
    }
    case YUV: {
      switch(toMode) {
      case GREY: {
        return fromYUVToGrey(image);
      }
      case RGB: {
        return fromYUVToRGB(image);
      }
      case YUV: {
        return image.copy();
      }
      case RGBA: {
        return fromYUVToRGBA(image);
      }
      default:
        throw new IllegalStateException("Invalid Type: "+toMode);
      }
    }
    case RGBA: {
      switch(toMode) {
      case GREY: {
        return fromRGBAToGrey(image);
      }
      case RGB: {
        return fromRGBAToRGB(image);
      }
      case YUV: {
        return fromRGBAToYUV(image);
      }
      case RGBA: {
        return image.copy();
      }
      default:
        throw new IllegalStateException("Invalid Type: "+toMode);
      }
    }
    default:
      throw new IllegalStateException("Invalid Type: "+toMode);
    }
  }

  public static JilImage fromRGBAToRGB(JilImage ji) {
    JilImage nji = JilImage.create(MODE.RGB, ji.getWidth(), ji.getHeight());
    byte[] ba = ji.getArray();
    byte[] nba = nji.getArray();
    for(int i=0; i<(ba.length/4); i++) {
      int npos = i*3;
      int opos = i*4;
      nba[npos] = ba[opos];
      nba[npos+1] = ba[opos+1];
      nba[npos+2] = ba[opos+2];
    }
    return nji;
  }
  public static JilImage fromRGBAToYUV(JilImage ji) {
    JilImage nji = JilImage.create(MODE.YUV, ji.getWidth(), ji.getHeight());
    byte[] ba = ji.getArray();
    byte[] nba = nji.getArray();
    for(int i=0; i<ba.length/4; i++) {
      int pos = i*4;
      int npos = i*3;
      int red = ba[pos]&0xff;
      int green = ba[pos+1]&0xff;
      int blue = ba[pos+2]&0xff;
      int y = (int)((red*0.299) + (green*0.587) + ((blue*0.114)));
      int u = (int)((blue-y)*0.492);
      int v = (int)((red-y)*0.877);
      nba[npos] = (byte)y;
      nba[npos+1] = (byte)u;
      nba[npos+2] = (byte)v;
    }
    return nji;
  }

  public static JilImage fromYUVToRGB(JilImage ji) {
    JilImage nji = JilImage.create(MODE.RGB, ji.getWidth(), ji.getHeight());
    byte[] ba = ji.getArray();
    byte[] nba = nji.getArray();
    for(int i=0; i<(nba.length/3); i++) {
      int pos = i*3;
      int y = ba[pos]&0xff;
      int u = ba[pos+1]&0xff;
      int v = ba[pos+2]&0xff;
      nba[pos] = (byte)(y+(1.140*v));
      nba[pos+1] = (byte)(y-(0.395*u)-(0.581*v));
      nba[pos+2] = (byte)(y+(2.033*u));
    }
    return nji;
  }

  public static JilImage fromYUVToRGBA(JilImage ji) {
    JilImage nji = JilImage.create(MODE.RGB, ji.getWidth(), ji.getHeight());
    byte[] ba = ji.getArray();
    byte[] nba = nji.getArray();
    for(int i=0; i<(nba.length/3); i++) {
      int pos = i*3;
      int opos = i*4;
      int y = ba[pos]&0xff;
      int u = ba[pos+1]&0xff;
      int v = ba[pos+2]&0xff;
      nba[opos] = (byte)(y+(1.140*v));
      nba[opos+1] = (byte)(y-(0.395*u)-(0.581*v));
      nba[opos+2] = (byte)(y+(2.032*u));
      nba[opos+3] = (byte)255;
    }
    return nji;
  }

  public static JilImage fromYUVToGrey(JilImage ji) {
    JilImage nji = JilImage.create(MODE.GREY, ji.getWidth(), ji.getHeight());
    byte[] ba = ji.getArray();
    byte[] nba = nji.getArray();
    for(int i=0; i<(nba.length); i++) {
      nba[i] = ba[i*3];
    }
    return nji;
  }

  public static JilImage fromRGBToRGBA(JilImage ji) {
    JilImage nji = JilImage.create(MODE.RGBA, ji.getWidth(), ji.getHeight());
    byte[] ba = ji.getArray();
    byte[] nba = nji.getArray();
    for(int i=0; i<(ba.length/3); i++) {
      int npos = i*4;
      int opos = i*3;
      nba[npos] = ba[opos];
      nba[npos+1] = ba[opos+1];
      nba[npos+2] = ba[opos+2];
      nba[npos+3] = (byte)255;
    }
    return nji;
  }

  public static JilImage fromRGBToYUV(JilImage ji) {
    JilImage nji = JilImage.create(MODE.YUV, ji.getWidth(), ji.getHeight());
    byte[] ba = ji.getArray();
    byte[] nba = nji.getArray();
    for(int i=0; i<nba.length; i+=3) {
      double red = (ba[i]&0xff)/255.0;
      double green = (ba[i+1]&0xff)/255.0;
      double blue = (ba[i+2]&0xff)/255.0;
      double y = ((red*0.299) + (green*0.587) + ((blue*0.114)));
      double u = ((blue-y)*0.492);
      double v = ((red-y)*0.877);
      System.out.println(red+":"+green+":"+blue);
      System.out.println("yuv:"+y+":"+u+":"+v);
      nba[i] = (byte)(y*255);
      nba[i+1] = (byte)(u*255);
      nba[i+2] = (byte)(v*255);
      System.out.println(nba[i]+":"+nba[i+1]+":"+nba[i+2]);
    }
    return nji;
  }

  public static JilImage fromRGBToGrey(JilImage ji) {
    JilImage nji = JilImage.create(MODE.GREY, ji.getWidth(), ji.getHeight());
    byte[] ba = ji.getArray();
    byte[] nba = nji.getArray();
    for(int i=0; i<nba.length; i++) {
      int pos = i*3;
      nba[i] = JilUtils.colorsToGrey(ba[pos], ba[pos+1], ba[pos+2]);
    }
    return nji;
  }

  public static JilImage fromRGBAToGrey(JilImage ji) {
    JilImage nji = JilImage.create(MODE.GREY, ji.getWidth(), ji.getHeight());
    byte[] ba = ji.getArray();
    byte[] nba = nji.getArray();
    for(int i=0; i<nba.length; i++) {
      int pos = i*4;
      nba[i] = JilUtils.colorsToGrey(ba[pos], ba[pos+1], ba[pos+2]);
    }
    return nji;
  }

  public static JilImage fromGreyToRGB(JilImage ji) {
    JilImage nji = JilImage.create(MODE.RGB, ji.getWidth(), ji.getHeight());
    byte[] ba = ji.getArray();
    byte[] nba = nji.getArray();
    for(int i=0; i<ba.length; i++) {
      int pos = i*3;
      nba[pos] = ba[i];
      nba[pos+1] = ba[i];
      nba[pos+2] = ba[i];
    }
    return nji;
  }

  public static JilImage fromGreyToYUV(JilImage ji) {
    JilImage nji = JilImage.create(MODE.YUV, ji.getWidth(), ji.getHeight());
    byte[] ba = ji.getArray();
    byte[] nba = nji.getArray();
    for(int i=0; i<ba.length; i++) {
      int pos = i*3;
      nba[pos] = ba[i];
      nba[pos+1] = 0;
      nba[pos+2] = 0;
    }
    return nji;
  }

  public static JilImage fromGreyToRGBA(JilImage ji) {
    JilImage nji = JilImage.create(MODE.RGBA, ji.getWidth(), ji.getHeight());
    byte[] ba = ji.getArray();
    byte[] nba = nji.getArray();
    for(int i=0; i<ba.length; i++) {
      int pos = i*4;
      nba[pos] = ba[i];
      nba[pos+1] = ba[i];
      nba[pos+2] = ba[i];
      nba[pos+3] = (byte)255;
    }
    return nji;
  }
  
  public static byte colorsToGrey(byte red, byte green, byte blue) {
    double r = (((red&0xff)*0.2126));//+(red&0xff));
    double g = (((green&0xff)*0.7152));//+(green&0xff));
    double b = (((blue&0xff)*0.0722));//+(blue&0xff));
    return (byte) Math.ceil((r+g+b));
  }

  public static Color mergeColors(Color first, Color second) {
    double napct = (second.getAlphaPct()+(first.getAlphaPct()*(1-second.getAlphaPct())));
    byte na = (byte) Math.round(napct*255);
    byte nr = (byte) Math.round(((second.getRedPct()*second.getAlphaPct() + 
        first.getRedPct()*first.getAlphaPct()*(1-second.getAlphaPct()))/napct)*255);
    byte nb = (byte) Math.round(((second.getBluePct()*second.getAlphaPct() + 
        first.getBluePct()*first.getAlphaPct()*(1-second.getAlphaPct()))/napct)*255);
    byte ng = (byte) Math.round(((second.getGreenPct()*second.getAlphaPct() + 
        first.getGreenPct()*first.getAlphaPct()*(1-second.getAlphaPct()))/napct)*255);
    Color nC = new Color(nr, ng, nb, na);
    return nC;
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

  public static BaseImage biCubicSmooth(BaseImage img, int width, int height) {
    if(width > img.getWidth() || height > img.getHeight()) {
      throw new IllegalStateException("Both width and height must be less then the current image!!");
    }

    int scaleFactor = 7;
    BaseImage nimg = img;
    BaseImage resizedImg;
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
      resizedImg = nimg.resize(runWidth, runHeight, false, BaseImage.ScaleType.CUBIC);
      nimg = resizedImg;
      if(nimg.getWidth() == width && nimg.getHeight() == height) {
        break;
      }
    }
    return resizedImg;
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
