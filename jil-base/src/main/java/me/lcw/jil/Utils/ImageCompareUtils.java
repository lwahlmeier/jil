package me.lcw.jil.Utils;

import me.lcw.jil.BaseImage;
import me.lcw.jil.BaseImage.ImageMode;
import me.lcw.jil.Color;
import me.lcw.jil.JilImage;

public class ImageCompareUtils {
  
  public static boolean hasImageChanged(final BaseImage biOrig, final BaseImage biNew) {
    if(biOrig.equals(biNew)) {
      return false;
    } 
    if(biOrig.getWidth() == biNew.getWidth() && biOrig.getHeight() == biNew.getHeight()) {
      long size = (biOrig.getWidth()*biOrig.getHeight());
      for(long i=0; i<size; i++) {
        int px = (int) (i%biOrig.getWidth());
        int py = (int) (i/biOrig.getWidth());
        if(!biOrig.getPixel(px, py).equals(biNew.getPixel(px, py))) {
          return true;
        }
      }
      return false;
    }
    return true;
  }
  
  public static JilImage diffImages(final BaseImage biOrig, final BaseImage biNew) {
    if(biOrig.getWidth() != biNew.getWidth() || biOrig.getHeight() != biNew.getHeight()) {
      throw new IllegalStateException("Images must be the same width and height!");
    }
    
    JilImage outImage = JilImage.create(ImageMode.RGBA32, biOrig.getWidth(), biOrig.getHeight(), Color.ALPHA);
    for(int y=0; y<biOrig.getHeight(); y++) {
      for(int x=0; x<biOrig.getWidth(); x++) {
        Color origColor = biOrig.getPixel(x, y);
        Color newColor = biNew.getPixel(x, y);
        if(!origColor.equals(newColor)) {
          outImage.setPixel(x, y, newColor);
        }
      }
    }
    return outImage;
  }
}
