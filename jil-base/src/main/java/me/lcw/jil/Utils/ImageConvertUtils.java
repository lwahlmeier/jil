package me.lcw.jil.Utils;

import me.lcw.jil.BaseImage.ImageMode;
import me.lcw.jil.JilByteImage;
import me.lcw.jil.JilImage;

public class ImageConvertUtils {
  public static JilImage convertMode(JilImage ji, ImageMode im) {
    JilByteImage nji;
    switch(im) {
    case RGBAHQ:{
      if(ji.getMode() == im) {
        return ji.copy();
      }
      nji = new JilByteImage(ImageMode.RGBAHQ, ji.getWidth(), ji.getHeight());
    } break;
    case RGBHQ:{
      if(ji.getMode() == im) {
        return ji.copy();
      }
      nji = new JilByteImage(ImageMode.RGBHQ, ji.getWidth(), ji.getHeight());
    } break;
    case GREYHQ:{
      if(ji.getMode() == im) {
        return ji.copy();
      }
      nji = new JilByteImage(ImageMode.GREYHQ, ji.getWidth(), ji.getHeight());
    } break;
    case RGBA32:{
      if(ji.getMode() == im) {
        return ji.copy();
      }
      nji = new JilByteImage(ImageMode.RGBA32, ji.getWidth(), ji.getHeight());
    } break;
    case RGB24:{
      if(ji.getMode() == im) {
        return ji.copy();
      }
      nji = new JilByteImage(ImageMode.RGB24, ji.getWidth(), ji.getHeight());
    } break;
    case GREY8:{
      if(ji.getMode() == im) {
        return ji.copy();
      }
      nji = new JilByteImage(ImageMode.GREY8, ji.getWidth(), ji.getHeight());
    } break;
    default:
      throw new IllegalStateException("Mode must be a byte mode! "+im);
    }

    for(int y=0; y<ji.getHeight(); y++) {
      for(int x=0; x<ji.getWidth(); x++) {
        nji.setPixel(x, y, ji.getPixel(x, y));
      }
    }
    return nji;
  }

  public static JilImage fromYUVToRGB(JilByteImage ji) {
    JilImage nji = JilImage.create(ImageMode.RGB24, ji.getWidth(), ji.getHeight());
    byte[] ba = ji.getByteArray();
    byte[] nba = nji.getByteArray();
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
    JilImage nji = JilImage.create(ImageMode.RGB24, ji.getWidth(), ji.getHeight());
    byte[] ba = ji.getByteArray();
    byte[] nba = nji.getByteArray();
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
    JilImage nji = JilImage.create(ImageMode.GREY8, ji.getWidth(), ji.getHeight());
    byte[] ba = ji.getByteArray();
    byte[] nba = nji.getByteArray();
    for(int i=0; i<(nba.length); i++) {
      nba[i] = ba[i*3];
    }
    return nji;
  }

  public static JilByteImage toByteImage(JilImage ji, ImageMode im) {
    JilByteImage nji;
    switch(im) {
    case RGBA32:{
      nji = new JilByteImage(ImageMode.RGBA32, ji.getWidth(), ji.getHeight());
    } break;
    case RGB24:{
      nji = new JilByteImage(ImageMode.RGB24, ji.getWidth(), ji.getHeight());
    } break;
    case GREY8:{
      nji = new JilByteImage(ImageMode.GREY8, ji.getWidth(), ji.getHeight());
    } break;
    default:
      throw new IllegalStateException("Mode must be a byte mode! "+im);
    }

    for(int y=0; y<ji.getHeight(); y++) {
      for(int x=0; y<ji.getWidth(); x++) {
        nji.setPixel(x, y, ji.getPixel(x, y));
      }
    }
    return nji;
  }


}
