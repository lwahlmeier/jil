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
    for (int y=0; y<newHeight; y++) {
      py = (int) (y*y_ratio);
      for(int x=0; x<newWidth; x++) {
        px = (int)(x*x_ratio);
        for(int c=0; c<(srcImage.getBPP()/8); c++) {
          newImage.setPixel(x, y, srcImage.getPixel(px, py));
        }
      }
    }
    return newImage;
  }
}
