package me.lcw.jil.scalers;

import me.lcw.jil.Color;
import me.lcw.jil.JilImage;

public class BiCubicScaler {
  private JilImage srcImage;
  private byte[] srcImageArray;
    
  private BiCubicScaler() {
    
  }
  
  public static JilImage scale(JilImage srcImage, int newWidth, int newHeight) {
    BiCubicScaler tmp = new BiCubicScaler();
    tmp.srcImage = srcImage;
    tmp.srcImageArray = srcImage.getArray();
    return tmp.doScale(newWidth, newHeight);
  }
  
  private JilImage doScale(int newWidth, int newHeight){
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
  
  private Color getInterpolatedPixel(int x, int y, float tx, float ty){
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
      cY = this.clampYPos(y+newY-1);
      for(int newX = 0; newX<4; newX++){
        cX = this.clampXPos(x+newX-1);
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
      last = Color.fromGreyValue(getInterpolatedValues(newColors[0][0],newColors[1][0],newColors[2][0],newColors[3][0], ty));
    } else if(cSize == 3) {
      last = Color.fromRGBValue(
          getInterpolatedValues(newColors[0][0],newColors[1][0],newColors[2][0],newColors[3][0], ty),
          getInterpolatedValues(newColors[0][1],newColors[1][1],newColors[2][1],newColors[3][1], ty),
          getInterpolatedValues(newColors[0][2],newColors[1][2],newColors[2][2],newColors[3][2], ty)
          );
    } else if (cSize == 4) {
      last = Color.fromRGBAValue(
          getInterpolatedValues(newColors[0][0],newColors[1][0],newColors[2][0],newColors[3][0], ty),
          getInterpolatedValues(newColors[0][1],newColors[1][1],newColors[2][1],newColors[3][1], ty),
          getInterpolatedValues(newColors[0][2],newColors[1][2],newColors[2][2],newColors[3][2], ty),
          getInterpolatedValues(newColors[0][3],newColors[1][3],newColors[2][3],newColors[3][3], ty)
          );
    }
    return last; 
  }
}
