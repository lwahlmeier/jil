package me.lcw.jil.scalers;

import me.lcw.jil.BaseImage;
import me.lcw.jil.Color;
import me.lcw.jil.JilImage;

public class BiCubicScaler {
  
  public static JilImage scale(JilImage srcImage, int newWidth, int newHeight){
    JilImage newImage = JilImage.create(srcImage.getMode(), newWidth, newHeight);
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
        newImage.setPixel(x, y, getInterpolatedPixel(srcImage, srcImage.getArray(), px, py, x_diff, y_diff));
      }
    }
    return newImage;
  }
  
  public static void scaleGeneric(final BaseImage srcImage, final BaseImage newImage) {
    float x_ratio = ((float)(srcImage.getWidth()))/newImage.getWidth();
    float y_ratio = ((float)(srcImage.getHeight()))/newImage.getHeight() ;
    float x_diff, y_diff;
    int px, py;
    byte[] srcImageArray = srcImage.getArray();
    for (int y = 0; y < newImage.getHeight(); y++){
      py = (int)(y_ratio * y);
      y_diff = (y_ratio * y) - py;
      for (int x = 0; x < newImage.getWidth(); x++){
        px = (int)(x_ratio * x);
        x_diff = (x_ratio * x) - px;
        newImage.setPixel(x, y, getInterpolatedPixel(srcImage, srcImageArray, px, py, x_diff, y_diff));
      }
    }
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
  
  private static Color getInterpolatedPixel(final BaseImage srcImage, final byte[] srcImageArray, final int x, final int y, final float tx, final float ty){
    int cX;
    int cY;
    int cSize = srcImage.getColors();
    byte[][] newColors = new byte[4][];    
    byte[][] pixles = new byte[4][];
    for(int i=0; i<pixles.length; i++){
      pixles[i] = new byte[cSize];
      newColors[i] = new byte[cSize];
    }
    
    for(int newY = 0; newY<4; newY++){
      cY = clampYPos(srcImage, y+newY-1);
      for(int newX = 0; newX<4; newX++){
        cX = clampXPos(srcImage, x+newX-1);
        int cp = ((cY*srcImage.getWidth())+cX)*cSize;
        for(int i=0; i<cSize; i++){ 
          pixles[newX][i] = srcImageArray[cp+i];
        }
      }
      //Combine Rows into 1 px
      for(int i=0; i<cSize; i++){ 
        newColors[newY][i] = getInterpolatedValues(pixles[0][i],pixles[1][i],pixles[2][i],pixles[3][i], tx);
      }
    }
    Color last = null;
    if(cSize == 1) { 
      last = new Color (getInterpolatedValues(newColors[0][0],newColors[1][0],newColors[2][0],newColors[3][0], ty));
    } else if(cSize == 3) {
      last = new Color (
          getInterpolatedValues(newColors[0][0],newColors[1][0],newColors[2][0],newColors[3][0], ty),
          getInterpolatedValues(newColors[0][1],newColors[1][1],newColors[2][1],newColors[3][1], ty),
          getInterpolatedValues(newColors[0][2],newColors[1][2],newColors[2][2],newColors[3][2], ty)
          );
    } else if (cSize == 4) {
      last = new Color (
          getInterpolatedValues(newColors[0][0],newColors[1][0],newColors[2][0],newColors[3][0], ty),
          getInterpolatedValues(newColors[0][1],newColors[1][1],newColors[2][1],newColors[3][1], ty),
          getInterpolatedValues(newColors[0][2],newColors[1][2],newColors[2][2],newColors[3][2], ty),
          getInterpolatedValues(newColors[0][3],newColors[1][3],newColors[2][3],newColors[3][3], ty)
          );
    }
    return last; 
  }
  
  
  private static int clampXPos(final BaseImage srcImage, final int x) {
    if(x < 0) {
      return 0;
    } else if (x > (srcImage.getWidth()-1)) {
      return (srcImage.getWidth()-1);
    }
    return x;
  }
  
  private static int clampYPos(final BaseImage srcImage, final int y) {
    if(y < 0) {
      return 0;
    } else if (y > (srcImage.getHeight()-1)) {
      return (srcImage.getHeight()-1);
    }
    return y;
  }
}
