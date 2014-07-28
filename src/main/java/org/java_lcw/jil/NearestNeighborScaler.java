package org.java_lcw.jil;

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
    
    for (int y=0; y<newHeight; y++) {
      py = (int) (y*y_ratio);
      for(int x=0; x<newWidth; x++) {
        px = (int)(x*x_ratio);
        
          int sp = ((srcImage.getWidth()*py)+px)*(size) ;
          int np = ((newImage.getWidth()*y)+x)*(size);
          if(size == 1) {
            newImage.MAP[np] = srcImage.MAP[sp];
          } else if (size >= 3) {
            newImage.MAP[np] = srcImage.MAP[sp];
            newImage.MAP[np+1] = srcImage.MAP[sp+1];
            newImage.MAP[np+2] = srcImage.MAP[sp+2];
            if(size == 4) {
              newImage.MAP[np+3] = srcImage.MAP[sp+3];
            }
          }
      }
    }
    return newImage;
  }
}
