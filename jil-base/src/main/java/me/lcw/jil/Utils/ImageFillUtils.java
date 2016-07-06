package me.lcw.jil.Utils;

import java.util.ArrayDeque;
import java.util.HashSet;

import me.lcw.jil.BaseImage;
import me.lcw.jil.Color;

public class ImageFillUtils {
  public static void edgeFill(BaseImage ai, int x, int y, Color c, Color edge, boolean keepAlpha) {
    PixelPair startPair = new PixelPair(x, y);

    ArrayDeque<PixelPair> toCheck = new ArrayDeque<PixelPair>();
    HashSet<PixelPair> checked = new HashSet<PixelPair>();

    toCheck.add(startPair);
    Color OC = ai.getPixel(x, y);
    Color NC = c;
    if(keepAlpha) {
      NC = c.changeAlpha(OC.getAlpha());
    }
    if(OC.equals(c)) {
      return;
    }
    boolean spanAbove = false;
    boolean spanBelow = false;
    while(!toCheck.isEmpty()) {
      PixelPair cpp = toCheck.removeFirst();
      checked.add(cpp);
      int x1 = cpp.x;    
      while(x1>=0 && !ai.getPixel(x1, cpp.y).equals(edge)) {
        x1--;
      }
      x1++;
      spanAbove = false;
      spanBelow = false;

      while(x1 < ai.getWidth() && !ai.getPixel(x1, cpp.y).equals(edge)) {
        ai.setPixel(x1, cpp.y, NC);
        if(!spanAbove && cpp.y > 0 && !ai.getPixel(x1, cpp.y-1).equals(edge) ) {
          PixelPair npp = new PixelPair(x1, cpp.y-1);
          if(!toCheck.contains(npp) && !checked.contains(npp)) {
            toCheck.add(npp);
            spanAbove = true;
          }
        } else if(spanAbove && cpp.y > 0 && ai.getPixel(x1, cpp.y-1).equals(edge)) {
          spanAbove = false;
        }
        if(!spanBelow && cpp.y<ai.getHeight()-1 && !ai.getPixel(x1, cpp.y+1).equals(edge) ) {
          PixelPair npp = new PixelPair(x1, cpp.y+1);
          if(!toCheck.contains(npp) && !checked.contains(npp)) {
            toCheck.add(npp);
            spanBelow = true;
          }

        } else if(spanBelow && cpp.y<ai.getHeight()-1 && ai.getPixel(x1, cpp.y+1).equals(edge)) {
          spanBelow = false;
        }
        x1++;
      }
    }
  }

  
  public static void noEdgeFill(BaseImage ai, int x, int y, Color c, boolean keepAlpha) {
    PixelPair startPair = new PixelPair(x, y);

    ArrayDeque<PixelPair> toCheck = new ArrayDeque<PixelPair>();

    toCheck.add(startPair);
    Color OC = ai.getPixel(x, y);
    Color NC = c;
    if(keepAlpha) {
      NC = c.changeAlpha(OC.getAlpha());
    }
    if(OC.equals(c)) {
      return;
    }
    boolean spanAbove = false;
    boolean spanBelow = false;
    while(!toCheck.isEmpty()) {
      PixelPair cpp = toCheck.removeFirst();
      int x1 = cpp.x;    
      while(x1>=0 && ai.getPixel(x1, cpp.y).equalsNoAlpha(OC)) {
        x1--;
      }
      x1++;
      spanAbove = false;
      spanBelow = false;

      while(x1 < ai.getWidth() && ai.getPixel(x1, cpp.y).equalsNoAlpha(OC)) {
        ai.setPixel(x1, cpp.y, NC);
        if(!spanAbove && cpp.y > 0 && ai.getPixel(x1, cpp.y-1).equalsNoAlpha(OC)) {
          PixelPair npp = new PixelPair(x1, cpp.y-1);
          if(toCheck.contains(npp)){
            return;
          }
          toCheck.add(npp);
          spanAbove = true;
        } else if(spanAbove && cpp.y > 0 && !ai.getPixel(x1, cpp.y-1).equalsNoAlpha(OC)) {
          spanAbove = false;
        }
        if(!spanBelow && cpp.y<ai.getHeight()-1 && ai.getPixel(x1, cpp.y+1).equalsNoAlpha(OC)) {
          PixelPair npp = new PixelPair(x1, cpp.y+1);
          if(toCheck.contains(npp)) {
            return;
          }
          toCheck.add(npp);
          spanBelow = true;
        } else if(spanBelow && cpp.y<ai.getHeight()-1 && !ai.getPixel(x1, cpp.y+1).equalsNoAlpha(OC)) {
          spanBelow = false;
        }
        x1++;
      }
    }
  }

}
