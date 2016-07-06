package me.lcw.jil.Utils;

import me.lcw.jil.BaseImage;
import me.lcw.jil.Color;
import me.lcw.jil.JilImage;

public class ImageCompareUtils {
  public static JilImage diffImages(final BaseImage biOrig, final BaseImage biNew, final float pctOff) {
    if(biOrig.getWidth() != biNew.getWidth() || biOrig.getHeight() != biNew.getHeight()) {
      throw new IllegalStateException("Images must be the same width and height!");
    }
    JilImage outImage = JilImage.create(biOrig.getMode(), biOrig.getWidth(), biOrig.getHeight());
    for(int y=0; y<biOrig.getHeight(); y++) {
      for(int x=0; x<biOrig.getWidth(); x++) {
        Color origColor = biOrig.getPixel(x, y);
        Color newColor = biNew.getPixel(x, y);
        float redOff = Math.abs((float)(origColor.getRedPct() - newColor.getRedPct()));
        float greenOff = Math.abs((float)(origColor.getGreenPct() - newColor.getGreenPct()));
        float blueOff = Math.abs((float)(origColor.getBluePct() - newColor.getBluePct()));
        if(redOff < pctOff && greenOff < pctOff && blueOff < pctOff ) {
          outImage.setPixel(x, y, Color.ALPHA);
        } else {
          outImage.setPixel(x, y, newColor);
        }
      }
    }
    return outImage;
  }
}
