package me.lcw.jil.Utils;

import me.lcw.jil.BaseImage;
import me.lcw.jil.BaseImage.MODE;
import me.lcw.jil.Color;
import me.lcw.jil.JilImage;

public class ImageCompareUtils {
  public static JilImage diffImages(final BaseImage biOrig, final BaseImage biNew) {
    if(biOrig.getWidth() != biNew.getWidth() || biOrig.getHeight() != biNew.getHeight()) {
      throw new IllegalStateException("Images must be the same width and height!");
    }
    
    JilImage outImage = JilImage.create(MODE.RGBA, biOrig.getWidth(), biOrig.getHeight(), Color.ALPHA);
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
