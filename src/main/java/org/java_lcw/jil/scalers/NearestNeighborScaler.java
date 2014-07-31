package org.java_lcw.jil.scalers;

import org.java_lcw.jil.Image;

public class NearestNeighborScaler {
  private NearestNeighborScaler() {
  }
  
  public static Image scale(Image srcImage, int newWidth, int newHeight) {
    Image newImage = Image.create(srcImage.getBPP(), newWidth, newHeight);
    float x_ratio = srcImage.getWidth()/(float)newWidth;
    float y_ratio = srcImage.getHeight()/(float)newHeight;
    int px = 0;
    int py = 0;
    int size = srcImage.getBPP()/8;
    byte[] newImageArray = newImage.getArray();
    byte[] srcImageArray = srcImage.getArray();
    
    for (int y=0; y<newHeight; y++) {
      py = (int) (y*y_ratio);
      for(int x=0; x<newWidth; x++) {
        px = (int)(x*x_ratio);
        
          int sp = ((srcImage.getWidth()*py)+px)*(size) ;
          int np = ((newImage.getWidth()*y)+x)*(size);
          System.arraycopy(srcImageArray, sp, newImageArray, np, size);
      }
    }
    return newImage;
  }
}
