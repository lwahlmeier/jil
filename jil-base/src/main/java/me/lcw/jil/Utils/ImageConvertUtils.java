package me.lcw.jil.Utils;

import me.lcw.jil.JilImage;
import me.lcw.jil.BaseImage.MODE;
import me.lcw.jil.Color;

public class ImageConvertUtils {
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
      case RGBA: {
        return fromRGBToRGBA(image);
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

  public static JilImage fromRGBToGrey(JilImage ji) {
    JilImage nji = JilImage.create(MODE.GREY, ji.getWidth(), ji.getHeight());
    byte[] ba = ji.getArray();
    byte[] nba = nji.getArray();
    for(int i=0; i<nba.length; i++) {
      int pos = i*3;
      nba[i] = Color.colorsToGrey(ba[pos], ba[pos+1], ba[pos+2]);
    }
    return nji;
  }

  public static JilImage fromRGBAToGrey(JilImage ji) {
    JilImage nji = JilImage.create(MODE.GREY, ji.getWidth(), ji.getHeight());
    byte[] ba = ji.getArray();
    byte[] nba = nji.getArray();
    for(int i=0; i<nba.length; i++) {
      int pos = i*4;
      nba[i] = Color.colorsToGrey(ba[pos], ba[pos+1], ba[pos+2]);
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

}
