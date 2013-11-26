package org.java_lcw.jil;

import java.util.HashMap;


public class BiCubicScaler {
  private Image srcImage;
    
  private BiCubicScaler() {
    
  }
  
  public static Image scale(Image srcImage, int newWidth, int newHeight) {
    BiCubicScaler tmp = new BiCubicScaler();
    tmp.srcImage = srcImage;
    return tmp.doScale(newWidth, newHeight);
  }
  
  private Image doScale(int newWidth, int newHeight){
    Image newImage = Image.create(srcImage.getBPP(), newWidth, newHeight);
    float x_ratio = ((float)(srcImage.getWidth()))/newWidth ;
    float y_ratio = ((float)(srcImage.getHeight()))/newHeight ;
    float x_diff, y_diff;
    int px, py, idx;
    for (int y = 0; y < newHeight; y++){
      py = (int)(y_ratio * y);
      y_diff = (y_ratio * y) - py;
      for (int x = 0; x < newWidth; x++){
        px = (int)(x_ratio * x);
        x_diff = (x_ratio * x) - px;
        newImage.setPixel(x, y, getInterpolatedPixel(px, py, x_diff, y_diff));
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
  
  private Color getInterpolatedPixel(int x, int y, float tx, float ty){
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
      //Combine Rows into 1 px
      newColors[newY] = new Color(
          (byte) getInterpolatedValues(pixles[0].getRed(),pixles[1].getRed(),pixles[2].getRed(),pixles[3].getRed(), tx),
          (byte) getInterpolatedValues(pixles[0].getGreen(),pixles[1].getGreen(),pixles[2].getGreen(),pixles[3].getGreen(), tx),
          (byte) getInterpolatedValues(pixles[0].getBlue(),pixles[1].getBlue(),pixles[2].getBlue(),pixles[3].getBlue(), tx),
          (byte) getInterpolatedValues(pixles[0].getAlpha(),pixles[1].getAlpha(),pixles[2].getAlpha(),pixles[3].getAlpha(), tx)
          );
    }
    //Combine Column of combined rows into 1px
    byte r = (byte) getInterpolatedValues(newColors[0].getRed(),newColors[1].getRed(),newColors[2].getRed(),newColors[3].getRed(), ty);
    byte g = (byte) getInterpolatedValues(newColors[0].getGreen(),newColors[1].getGreen(),newColors[2].getGreen(),newColors[3].getGreen(), ty);
    byte b = (byte) getInterpolatedValues(newColors[0].getBlue(),newColors[1].getBlue(),newColors[2].getBlue(),newColors[3].getBlue(), ty);
    byte a = (byte) getInterpolatedValues(newColors[0].getAlpha(),newColors[1].getAlpha(),newColors[2].getAlpha(),newColors[3].getAlpha(), ty);
    Color last = new Color(r, g, b, a);
    return last; 
  }
}
