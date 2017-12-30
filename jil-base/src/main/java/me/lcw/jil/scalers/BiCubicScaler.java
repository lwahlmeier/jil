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

  private static byte getInterpolatedValues(byte c0, byte c1, byte c2, byte c3, float t) {
    int p0 = c0 & 0xff;
    int p1 = c1 & 0xff;
    int p2 = c2 & 0xff;
    int p3 = c3 & 0xff;
    int p = (p3 - p2) - (p0 - p1);
    int q = (p0 - p1) - p;
    int r = p2 - p0;
    int s = p1;
    float tSqrd = t*t;

    //double X = (p * (tSqrd * t)) + (q * tSqrd) + (r * t) + s;
    float X = ((p * (tSqrd * t) + 0.5f) + (q * tSqrd + 0.5f) + (r * t + 0.5f) + s);
    //double X = ((p1 + 0.5 * t*(p2 - p0 + t*(2.0*p0 - 5.0*p1 + 4.0*p2 - p3 + t*(3.0*(p1 - p2) + p3 - p0)))) );
    //double X = ((p1 + 0.5 * t*(p2 - p0 + t*(2.0*p0 - 5.0*p1 + 4.0*p2 - p3 + t*(3.0*(p1 - p2) + p3 - p0)))) );
    //double X = ( p0 + p1 + p2 + p3 ) / 4;

    //double X = (-0.5 * p0 + 1.5 * p1 - 1.5 * p2 + 0.5 * p3) * 
    //    Math.pow(t,3)+(p0-2.5f*p1+2.f*p2-0.5f*p3) * 
    //    Math.pow(t,2)+(-0.5f*p0+0.5f*p2)*t+p1;

    //Constrain to a Byte
    byte R;
    if (X < 0) {
      R = 0;
    } else if (X > 255) {
      R = -1;
    } else {
      R = (byte) X;
    }
    return R;
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
