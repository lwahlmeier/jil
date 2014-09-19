package org.java_lcw.jil;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import org.java_lcw.jil.Image.ImageException;

public class Draw {
  private static final int sigmaD = 2;
  private static final int sigmaR = 2;
  private static final double sigmaMax = Math.max(sigmaD, sigmaR);
  private static final int kernelRadius = (int)Math.ceil(2 * sigmaMax);
  private static final double twoSigmaRSquared = 2 * sigmaR * sigmaR;
  private static final int kernelSize = kernelRadius * 2 + 1;
  private static final int center = (kernelSize - 1) / 2;
  private static final double[][] kernelD = new double[kernelSize][kernelSize];
  private static final double gaussSimilarity[] = new double[256];
  static {

    for (int x = -center; x < -center + kernelSize; x++) {
      for (int y = -center; y < -center + kernelSize; y++) {
        kernelD[x + center][y + center] = Math.exp(-((x * x + y * y) / (2 * sigmaD * sigmaD)));
      }
    }


    for (int i = 0; i < 256; i++) {
      gaussSimilarity[i] = Math.exp(-((i) / twoSigmaRSquared));
    }
  }



  public static void rect(Image img, int x, int y, int w, int h, Color c, int lineWidth, boolean fill) {
    int maxW = x+w;
    int maxH = y+h;
    if (img.getWidth() < maxW) {
      maxW = img.getWidth() - x;
    }
    if (img.getHeight() < maxH) {
      maxH = img.getHeight() - y;
    }
    if(x < 0) {
      x=0;
    }
    if(y < 0) {
      y=0;
    }
    for(int H = y; H<maxH; H++) {
      for(int W = x;  W< maxW; W++) {
        if(W <= (y+lineWidth)-1 || H <= (y+lineWidth)-1 || H >= maxH-lineWidth || W >= maxW-lineWidth) {
          img.setPixel(W, H, c);
        } 
      }
    }
    if(fill) {
      int tx = x+ w/2;
      int ty = y+ h/2;
      floodFill(img, tx, ty, c, c, false);
    }
  }
  
  public static void floodFill(Image img, int x, int y, Color c) {
    floodFill(img, x, y, c, null, false);
  }
  
  public static void floodFill(Image img, int x, int y, Color c, Color edge) {
    floodFill(img, x, y, c, edge, false);
  }

  public static void floodFill(Image img, int x, int y, Color c, Color edge, boolean keepAlpha) {
    if(x < 0 || x >= img.getWidth()) {
      return;
    }
    if(y <0 || y>=img.getWidth()) {
      return;
    }
    Integer[] ce = new Integer[] {x, y};
    ArrayDeque<Integer[]> pl = new ArrayDeque<Integer[]>();
    pl.add(ce);
    if(edge == null) {
      Color OC = img.getPixel(x, y);
      if(OC.equals(c)) {
        return;
      }
      while(pl.size() > 0) {
        ce = pl.poll();
        Color tmpC = img.getPixel(ce[0], ce[1]);
        if(tmpC!=null && tmpC.equalsNoAlpha(OC)) {
          Color nc = c.copy();
          if(keepAlpha) {
            nc.setAlpha(tmpC.getAlpha());
          }
          img.setPixel(ce[0], ce[1], nc);
          if(ce[0]+1 < img.getWidth()) {
            pl.add(new Integer[]{ce[0]+1, ce[1]});
          }
          if(ce[0]-1 >= 0) {
            pl.add(new Integer[]{ce[0]-1, ce[1]});
          }
          if(ce[1]+1 < img.getHeight()) {
            pl.add(new Integer[]{ce[0], ce[1]+1});
          }
          if(ce[1]-1 >= 0) {
            pl.add(new Integer[]{ce[0], ce[1]-1});
          }
        }
      }
    } else {
      while(pl.size() > 0) {
        ce = pl.poll();
        Color tmpC = img.getPixel(ce[0], ce[1]);
        Color nc = c.copy();
        if(keepAlpha) {
          nc.setAlpha(tmpC.getAlpha());
        }
        if(!tmpC.equals(edge) && !tmpC.equals(nc)) {
          
          img.setPixel(ce[0], ce[1], nc);
          if(ce[0]+1 < img.getWidth()) {
            pl.add(new Integer[]{ce[0]+1, ce[1]});
          }
          if(ce[0] -1 >= 0) {
            pl.add(new Integer[]{ce[0]-1, ce[1]});
          }
          if(ce[1]+1 < img.getHeight()) {
            pl.add(new Integer[]{ce[0], ce[1]+1});
          }
          if(ce[1] -1 >= 0) {
            pl.add(new Integer[]{ce[0], ce[1]-1});
          }
        }
      }
    }
  }

  public static void fillColor(Image img, int x, int y, Color c) {
    floodFill(img,x,y,c, null, false);
  }

  public static void circle(Image img, int cx, int cy, int size, Color c, int lineWidth, boolean fill){
    int r = size/2;
    int points = Math.max(r/16, 5);
    for(int i=0; i<360*points; i++) {
      int px = (int)Math.round(cx+Math.cos(i*Math.PI/180.0/points)*r);
      int py = (int)Math.round(cy+Math.sin(i*Math.PI/180.0/points)*r);
      if(px >= 0 && py >= 0 && py < img.getHeight() && px < img.getWidth()) {
        img.setPixel(px, py, c);
      }
    }

    if(fill) {
      lineWidth = size;
    }

    for(int l = 1; l<lineWidth; l++){
      size -=1;
      Draw.circle(img, cx, cy, size, c, 1, false);
    }
  }
  public static void line(Image img, int startX, int startY, int endX, int endY, Color c, int lineWidth) {
    line(img, startX, startY, endX, endY, c, lineWidth, false);

  }

  public static List<int[]> lineToList(int x, int y, int x2, int y2) {
    List<int[]> list = new ArrayList<int[]>();
    int w = x2 - x ;
    int h = y2 - y ;
    int dx1 = 0;
    int dy1 = 0;
    int dx2 = 0;
    int dy2 = 0 ;
    if (w<0) { 
      dx1 = -1;
    } else if (w>0) {
      dx1 = 1 ;
    }
    if (h<0) {
      dy1 = -1;
    } else if (h>0) { 
      dy1 = 1 ;
    }
    if (w<0) {
      dx2 = -1;
    } else if (w>0) {
      dx2 = 1 ;
    }
    int longest = Math.abs(w) ;
    int shortest = Math.abs(h) ;
    if (!(longest>shortest)) {
      longest = Math.abs(h) ;
      shortest = Math.abs(w) ;
      if (h<0) {
        dy2 = -1 ;
      } else if (h>0) {
        dy2 = 1 ;
      }
      dx2 = 0 ;            
    }
    int numerator = longest >> 1;
      for (int i=0;i<=longest;i++) {
        list.add(new int[]{x, y});
        numerator += shortest ;
        if (!(numerator<longest)) {
          numerator -= longest ;
          x += dx1 ;
          y += dy1 ;
        } else {
          x += dx2 ;
          y += dy2 ;
        }
      }
      return list;
  }


  public static void line(Image img, int x, int y, int x2, int y2, Color c, int lineWidth, boolean alphaMerge) {
    List<int[]> pxlist = lineToList(x, y, x2, y2);
    Image origImg = img;
    Image circle = null;
    if(lineWidth > 1) {
      img = Image.create(Image.MODE_RGBA, img.getWidth(), img.getHeight());  
      if(circle == null) {
        circle = Image.create(img.getBPP(), lineWidth+1, lineWidth+1);
        circle(circle, (lineWidth/2), (lineWidth/2), lineWidth, new Color(c.getRed(), c.getGreen(), c.getBlue()), 1, true);
      }
    }
    for(int[] ai: pxlist) {
      if(lineWidth > 1) {
        img.paste(ai[0]-(lineWidth/2), ai[1]-(lineWidth/2), circle, true);
      } else {
        img.setPixel(ai[0], ai[1], c);
      }
    }

    if(lineWidth > 1) {
      while(pxlist.size() > 0) {
        int[] tmp = pxlist.remove(0);
        Color pc = img.getPixel(tmp[0], tmp[1]);
        if(pc != null) {
          floodFill(img, tmp[0], tmp[1], c, Color.ALPHA, false);
          break;
        }
      }
      origImg.paste(0, 0, img, true);
    }
  }
}




