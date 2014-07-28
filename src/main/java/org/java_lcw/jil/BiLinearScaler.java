package org.java_lcw.jil;

public class BiLinearScaler {
  private Image srcImage;
  private BiLinearScaler() {
  }
  
  public static Image scale(Image srcImage, int newWidth, int newHeight) {
    final BiLinearScaler tmp = new BiLinearScaler();
    tmp.srcImage = srcImage;
    
    Image newImage = Image.create(srcImage.getBPP(), newWidth, newHeight);
    final float x_ratio = ((float)(srcImage.getWidth()))/newWidth ;
    final float y_ratio = ((float)(srcImage.getHeight()))/newHeight ;
    float x_diff, y_diff;
    int px, py, py1, px1;
    final int[] r = new int[4];
    for (int y=0; y < newHeight; y++){
      py = (int)(y_ratio * y);
      y_diff = (y_ratio * y) - py;

      py1 = tmp.clampYPos(py+1);
      
      for (int x=0; x < newWidth; x++){
        px = (int)(x_ratio * x);
        x_diff = (x_ratio * x) - px;
        px1 = tmp.clampXPos(px+1);

        for(byte c = 0; c < tmp.srcImage.getChannels(); c++) {
          r[0] = tmp.srcImage.getByteInChannel(px, py, c) & 0xff;
          r[1] = tmp.srcImage.getByteInChannel(px1, py, c) & 0xff;
          r[2] = tmp.srcImage.getByteInChannel(px, py1, c) & 0xff;
          r[3] = tmp.srcImage.getByteInChannel(px1, py1, c) & 0xff;
          newImage.setPixelInChannel(x, y, c, (byte) (
              r[0]*(1-x_diff)*(1-y_diff) +  
              r[1]*(x_diff)  *(1-y_diff) +
              r[2]*(y_diff)  *(1-x_diff) +
              r[3]*(x_diff)  *(y_diff) ));
        }
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
}
