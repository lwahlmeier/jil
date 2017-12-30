package me.lcw.jil.scalers;

import me.lcw.jil.Color;
import me.lcw.jil.JilByteImage;
import me.lcw.jil.JilImage;

public class BiCubicScaler {
  private JilImage srcImage;

  private BiCubicScaler() {

  }

  public static JilImage scale(JilImage srcImage, int newWidth, int newHeight) {
    BiCubicScaler tmp = new BiCubicScaler();
    tmp.srcImage = srcImage;
    return tmp.doScale(newWidth, newHeight);
  }

  private JilImage doScale(int newWidth, int newHeight) {
    JilByteImage newImage = (JilByteImage)JilImage.create(srcImage.getMode(), newWidth, newHeight);
    float x_ratio = ((float)(srcImage.getWidth()))/newWidth ;
    float y_ratio = ((float)(srcImage.getHeight()))/newHeight ;
    float x_diff, y_diff;
    int px, py;
    for (int y = 0; y < newHeight; y++){
      py = (int)(y_ratio * y);
      y_diff = (y_ratio * y) - py;
      for (int x = 0; x < newWidth; x++){
        px = (int)(x_ratio * x);
        x_diff = (x_ratio * x) - px;
        Color nc = getInterpolatedPixel2(px, py, x_diff, y_diff);
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
  
  private byte getInterpolatedValues(byte c0, byte c1, byte c2, byte c3, float t) {
    double p0 = c0 & 0xff;
    double p1 = c1 & 0xff;
    double p2 = c2 & 0xff;
    double p3 = c3 & 0xff;

    double q0 = 2*p1;
    double q1 = p2-p0;
    double q2 = 2*p0-5*p1+4*p2-p3;
    double q3 = -p0+3*p1-3*p2+p3;
    double res = Math.round((q3*t*t*t + q2*t*t + q1*t + q0)/2);
    
    //Constrain to a Byte
    if(res < 0) res = 0;
    if(res > 255) res = 255;

    return (byte)res;
  }

  private Color getInterpolatedValues(Color c0, Color c1, Color c2, Color c3, float t) {
    switch(srcImage.getMode()) {
    case GREY8: {
      byte g = getInterpolatedValues(c0.getGreyByte(), c1.getGreyByte(), c2.getGreyByte(), c3.getGreyByte(), t);
      return Color.fromGreyByte(g);
    }
    case RGB24: {
      byte r = getInterpolatedValues(c0.getRedByte(),c1.getRedByte(), c2.getRedByte(), c3.getRedByte(), t);
      byte g = getInterpolatedValues(c0.getGreenByte(),c1.getGreenByte(), c2.getGreenByte(), c3.getGreenByte(), t);
      byte b = getInterpolatedValues(c0.getBlueByte(),c1.getBlueByte(), c2.getBlueByte(), c3.getBlueByte(), t);
      return Color.fromRGBBytes(r, g, b);
    }
    case RGBA32: {
      byte r = getInterpolatedValues(c0.getRedByte(),c1.getRedByte(), c2.getRedByte(), c3.getRedByte(), t);
      byte g = getInterpolatedValues(c0.getGreenByte(),c1.getGreenByte(), c2.getGreenByte(), c3.getGreenByte(), t);
      byte b = getInterpolatedValues(c0.getBlueByte(),c1.getBlueByte(), c2.getBlueByte(), c3.getBlueByte(), t);
      byte a = getInterpolatedValues(c0.getAlphaByte(),c1.getAlphaByte(), c2.getAlphaByte(), c3.getAlphaByte(), t);
      return Color.fromRGBABytes(r, g, b, a);
    }
    default:
      throw new IllegalStateException();
    }
    
  }

  private Color getInterpolatedPixel2(int x, int y, float tx, float ty){
    int cX;
    int cY;

    Color[] newColors = new Color[4];    
    Color[] pixles = new Color[4];

    for(int newY = 0; newY<4; newY++){
      cY = this.clampYPos(y+newY-1);
      for(int newX = 0; newX<4; newX++){
        cX = this.clampXPos(x+newX-1);
        pixles[newX] = srcImage.getPixel(cX, cY);
      }
      newColors[newY] = getInterpolatedValues(pixles[0],pixles[1],pixles[2],pixles[3], tx);
    }

    Color last = getInterpolatedValues(newColors[0],newColors[1],newColors[2],newColors[3], ty);
    return last; 
  }
}
