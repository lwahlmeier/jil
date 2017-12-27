package me.lcw.jil.scalers;

import me.lcw.jil.BaseImage.ImageMode;
import me.lcw.jil.Color;
import me.lcw.jil.JilImage;

public class BiLinearScaler {
  private JilImage srcImage;
  private BiLinearScaler() {
  }

  public static JilImage scale(JilImage srcImage, int newWidth, int newHeight) {
    final BiLinearScaler tmp = new BiLinearScaler();
    tmp.srcImage = srcImage;

    JilImage newImage = JilImage.create(srcImage.getMode(), newWidth, newHeight);
    final float x_ratio = ((float)(srcImage.getWidth()))/newWidth ;
    final float y_ratio = ((float)(srcImage.getHeight()))/newHeight ;
    float x_diff, y_diff;
    int px, py, py1, px1;
    for (int y=0; y < newHeight; y++){
      py = (int)(y_ratio * y);
      y_diff = (y_ratio * y) - py;

      py1 = tmp.clampYPos(py+1);

      for (int x=0; x < newWidth; x++){
        px = (int)(x_ratio * x);
        x_diff = (x_ratio * x) - px;
        px1 = tmp.clampXPos(px+1);

        Color nc = reduceColors(
            tmp.srcImage.getMode(),
            tmp.srcImage.getPixel(px, py),
            tmp.srcImage.getPixel(px1, py),
            tmp.srcImage.getPixel(px, py1),
            tmp.srcImage.getPixel(px1, py1), x_diff, y_diff);
        newImage.setPixel(x, y, nc);
      }
    }
    return newImage;    
  }

  private int clampXPos(int x) {
    if(x < 0) {
      return 0;
    } else if (x > (srcImage.getWidth()-1)) {
      return (srcImage.getWidth()-1);
    }
    return x;
  }

  private int clampYPos(int y) {
    if(y < 0) {
      return 0;
    } else if (y > (srcImage.getHeight()-1)) {
      return (srcImage.getHeight()-1);
    }
    return y;
  }

  private static Color reduceColors(ImageMode im, Color c1, Color c2, Color c3, Color c4, float x_diff, float y_diff) {
    switch(im) {
    case GREYHQ:
      return Color.fromGreyPCT(
          (
              (c1.getGreyPct())*(1-x_diff)*(1-y_diff) +  
              (c2.getGreyPct())*(x_diff)  *(1-y_diff) +
              (c3.getGreyPct())*(y_diff)  *(1-x_diff) +
              (c4.getGreyPct())*(x_diff)  *(y_diff) ));
    case RGBHQ:
      return Color.fromRGBPCT(
          (
              (c1.getRedPct())*(1-x_diff)*(1-y_diff) +  
              (c2.getRedPct())*(x_diff)  *(1-y_diff) +
              (c3.getRedPct())*(y_diff)  *(1-x_diff) +
              (c4.getRedPct())*(x_diff)  *(y_diff) ),
          (
              (c1.getGreenPct())*(1-x_diff)*(1-y_diff) +  
              (c2.getGreenPct())*(x_diff)  *(1-y_diff) +
              (c3.getGreenPct())*(y_diff)  *(1-x_diff) +
              (c4.getGreenPct())*(x_diff)  *(y_diff) ),
          (
              (c1.getBluePct())*(1-x_diff)*(1-y_diff) +  
              (c2.getBluePct())*(x_diff)  *(1-y_diff) +
              (c3.getBluePct())*(y_diff)  *(1-x_diff) +
              (c4.getBluePct())*(x_diff)  *(y_diff) ));
    case RGBAHQ:
      return Color.fromRGBAPCT(
          (
              (c1.getRedPct())*(1-x_diff)*(1-y_diff) +  
              (c2.getRedPct())*(x_diff)  *(1-y_diff) +
              (c3.getRedPct())*(y_diff)  *(1-x_diff) +
              (c4.getRedPct())*(x_diff)  *(y_diff) ),
          (
              (c1.getGreenPct())*(1-x_diff)*(1-y_diff) +  
              (c2.getGreenPct())*(x_diff)  *(1-y_diff) +
              (c3.getGreenPct())*(y_diff)  *(1-x_diff) +
              (c4.getGreenPct())*(x_diff)  *(y_diff) ),
          (
              (c1.getBluePct())*(1-x_diff)*(1-y_diff) +  
              (c2.getBluePct())*(x_diff)  *(1-y_diff) +
              (c3.getBluePct())*(y_diff)  *(1-x_diff) +
              (c4.getBluePct())*(x_diff)  *(y_diff) ),
          (
              (c1.getAlphaPct())*(1-x_diff)*(1-y_diff) +  
              (c2.getAlphaPct())*(x_diff)  *(1-y_diff) +
              (c3.getAlphaPct())*(y_diff)  *(1-x_diff) +
              (c4.getAlphaPct())*(x_diff)  *(y_diff) ));
    case GREY8:
      return Color.fromGreyByte(
          (byte) (
              (c1.getGreyByte()&0xff)*(1-x_diff)*(1-y_diff) +  
              (c2.getGreyByte()&0xff)*(x_diff)  *(1-y_diff) +
              (c3.getGreyByte()&0xff)*(y_diff)  *(1-x_diff) +
              (c4.getGreyByte()&0xff)*(x_diff)  *(y_diff) ));
    case RGB24:
      return Color.fromRGBBytes(
          (byte) (
              (c1.getRedByte()&0xff)*(1-x_diff)*(1-y_diff) +  
              (c2.getRedByte()&0xff)*(x_diff)  *(1-y_diff) +
              (c3.getRedByte()&0xff)*(y_diff)  *(1-x_diff) +
              (c4.getRedByte()&0xff)*(x_diff)  *(y_diff) ),
          (byte) (
              (c1.getGreenByte()&0xff)*(1-x_diff)*(1-y_diff) +  
              (c2.getGreenByte()&0xff)*(x_diff)  *(1-y_diff) +
              (c3.getGreenByte()&0xff)*(y_diff)  *(1-x_diff) +
              (c4.getGreenByte()&0xff)*(x_diff)  *(y_diff) ),
          (byte) (
              (c1.getBlueByte()&0xff)*(1-x_diff)*(1-y_diff) +  
              (c2.getBlueByte()&0xff)*(x_diff)  *(1-y_diff) +
              (c3.getBlueByte()&0xff)*(y_diff)  *(1-x_diff) +
              (c4.getBlueByte()&0xff)*(x_diff)  *(y_diff) ));
    case RGBA32:
      return Color.fromRGBABytes(
          (byte) (
              (c1.getRedByte()&0xff)*(1-x_diff)*(1-y_diff) +  
              (c2.getRedByte()&0xff)*(x_diff)  *(1-y_diff) +
              (c3.getRedByte()&0xff)*(y_diff)  *(1-x_diff) +
              (c4.getRedByte()&0xff)*(x_diff)  *(y_diff) ),
          (byte) (
              (c1.getGreenByte()&0xff)*(1-x_diff)*(1-y_diff) +  
              (c2.getGreenByte()&0xff)*(x_diff)  *(1-y_diff) +
              (c3.getGreenByte()&0xff)*(y_diff)  *(1-x_diff) +
              (c4.getGreenByte()&0xff)*(x_diff)  *(y_diff) ),
          (byte) (
              (c1.getBlueByte()&0xff)*(1-x_diff)*(1-y_diff) +  
              (c2.getBlueByte()&0xff)*(x_diff)  *(1-y_diff) +
              (c3.getBlueByte()&0xff)*(y_diff)  *(1-x_diff) +
              (c4.getBlueByte()&0xff)*(x_diff)  *(y_diff) ),
          (byte) (
              (c1.getAlphaByte()&0xff)*(1-x_diff)*(1-y_diff) +  
              (c2.getAlphaByte()&0xff)*(x_diff)  *(1-y_diff) +
              (c3.getAlphaByte()&0xff)*(y_diff)  *(1-x_diff) +
              (c4.getAlphaByte()&0xff)*(x_diff)  *(y_diff) ));
    default:
      throw new IllegalStateException();
    }
  }
}
