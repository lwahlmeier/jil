package me.lcw.jil.scalers;

import me.lcw.jil.BaseImage;
import me.lcw.jil.JilImage;
import me.lcw.jil.NativeImage;

public class NearestNeighborScaler {
  static {
    System.out.println(System.getProperty("java.library.path"));
    System.setProperty("java.library.path", "/home/lwahlmeier/git-personal/jil/jil-base/build/libs:"+System.getProperty("java.library.path"));
    System.out.println(System.getProperty("java.library.path"));
    System.loadLibrary("nnscaler");
  }
  private NearestNeighborScaler() {
  }

  private static native void scaleJil(byte[] sba, int srcWidth, int srcHeight, byte[] nba, int newWidth, int newHeight, int colors);
  
  public static JilImage scale(JilImage srcImage, int newWidth, int newHeight) {
    JilImage newImage = JilImage.create(srcImage.getMode(), newWidth, newHeight);
    scaleJil(srcImage.getArray(), srcImage.getWidth(), srcImage.getHeight(), newImage.getArray(), newImage.getWidth(), newImage.getHeight(), newImage.getColors());
//    float x_ratio = srcImage.getWidth()/(float)newWidth;
//    float y_ratio = srcImage.getHeight()/(float)newHeight;
//    int px = 0;
//    int py = 0;
//    int size = srcImage.getColors();
//    byte[] newImageArray = newImage.getArray();
//    byte[] srcImageArray = srcImage.getArray();
//
//    for (int y=0; y<newHeight; y++) {
//      py = (int) (y*y_ratio);
//      for(int x=0; x<newWidth; x++) {
//        px = (int)(x*x_ratio);
//
//        int sp = ((srcImage.getWidth()*py)+px)*(size) ;
//        int np = ((newImage.getWidth()*y)+x)*(size);
//        System.arraycopy(srcImageArray, sp, newImageArray, np, size);
//      }
//    }
    return newImage;
  }

  public static void scaleGeneric(BaseImage srcImage, BaseImage newImage) {

    float x_ratio = srcImage.getWidth()/(float)newImage.getWidth();
    float y_ratio = srcImage.getHeight()/(float)newImage.getHeight();
    int px = 0;
    int py = 0;
    for (int y=0; y<newImage.getHeight(); y++) {
      py = (int) (y*y_ratio);
      for(int x=0; x<newImage.getWidth(); x++) {
        px = (int)(x*x_ratio);
        newImage.setPixel(x, y, srcImage.getPixel(px, py));
      }
    }
  }
  
  
  public static void scaleNativeImage(NativeImage srcImage, NativeImage newImage) {

    float x_ratio = srcImage.getWidth()/(float)newImage.getWidth();
    float y_ratio = srcImage.getHeight()/(float)newImage.getHeight();
    int size = srcImage.getColors();
    int px = 0;
    int py = 0;
    for (int y=0; y<newImage.getHeight(); y++) {
      py = (int) (y*y_ratio);
      for(int x=0; x<newImage.getWidth(); x++) {
        px = (int)(x*x_ratio);
        int opos = ((srcImage.getWidth()*py)+px)*(size) ;
        int npos = ((newImage.getWidth()*y)+x)*(size);
        newImage.getUnsafeByteArray().setFromUBA(srcImage.getUnsafeByteArray(), opos, npos, size);
      }
    }

  }
}
