package org.java_lcw.jil;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class Draw {

  public static void rect(Image img, int x, int y, int w, int h, Color c, int lineWidth, boolean fill) {
    int maxW = x+w;
    int maxH = y+h;
    if (img.getWidth() < maxW) {
      maxW = img.getWidth() - x;
    }
    if (img.getHeight() < maxH) {
      maxH = img.getHeight() - y;
    }

    if(fill) {
      for(int H = 0; H<maxH; H++) {
        for(int W = 0;  W< maxW; W++) {
          img.setPixel(W+x, y+H, c);
        }
      }
    } else {
      for(int H = 0; H<maxH; H++) {
        for(int lw=0; lw<lineWidth; lw++) {
          img.setPixel(x+lw, y+H, c);
          img.setPixel(x+maxW-lw, y+H, c);
        }
      }

      for(int W = 0; W<maxW; W++) {
        for(int lw=0; lw<lineWidth; lw++) {
          img.setPixel(x+W, y+lw, c);
          img.setPixel(x+W, y+maxH-lw, c);
        }
      }
    }
  }

  public static void floodFill(Image img, int x, int y, Color c, Color edge) {

    if(x <0 || x>=img.getWidth()) {
      return;
    }
    if(y <0 || y>=img.getWidth()) {
      return;
    }
    Color OC = img.getPixel(x, y);
    if(OC.equals(c)) {
      return;
    }
    Integer[] ce = new Integer[] {x, y};
    ArrayDeque<Integer[]> pl = new ArrayDeque<Integer[]>();
    pl.add(ce);
    if(edge == null) {
      while(pl.size() > 0) {
        ce = pl.poll();
        try {
          Color tmpC = img.getPixel(ce[0], ce[1]);
          if(tmpC!=null && tmpC.equals(OC)) {
            img.setPixel(ce[0], ce[1], c);
            if(ce[0]+1 < img.getWidth()) {
              pl.add(new Integer[]{ce[0]+1, ce[1]});
            }
            if(ce[0] -1 >= 0) {
              pl.add(new Integer[]{ce[0]-1, ce[1]});
            }
            if(ce[0]+1 < img.getHeight()) {
              pl.add(new Integer[]{ce[0], ce[1]+1});
            }
            if(ce[0] -1 >= 0) {
              pl.add(new Integer[]{ce[0], ce[1]-1});
            }
          }
        } catch(ArrayIndexOutOfBoundsException e) { 
        }
      }
    } else {
      while(pl.size() > 0) {
        ce = pl.poll();
        try{
          Color tmpC = img.getPixel(ce[0], ce[1]);
          if(!tmpC.equals(c) && !tmpC.equals(edge)) {
            img.setPixel(ce[0], ce[1], c);
            if(ce[0]+1 < img.getWidth()) {
              pl.add(new Integer[]{ce[0]+1, ce[1]});
            }
            if(ce[0] -1 >= 0) {
              pl.add(new Integer[]{ce[0]-1, ce[1]});
            }
            if(ce[0]+1 < img.getHeight()) {
              pl.add(new Integer[]{ce[0], ce[1]+1});
            }
            if(ce[0] -1 >= 0) {
              pl.add(new Integer[]{ce[0], ce[1]-1});
            }
          }
        } catch(ArrayIndexOutOfBoundsException e) { 
        }

      }
    }
  }

  public static void fillColor(Image img, int x, int y, Color c) {
    floodFill(img,x,y,c, null);
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
  public static void line(Image img, int startX, int startY, int endX, int endY, Color c, int lineWidth, boolean alphaMerge) {
    int xdist = Math.abs(endX-startX);
    int ydist = Math.abs(endY-startY);
    int sx = 1;
    int sy = 1;
    if(startX  >= endX) {
      sx = -1;
    }
    if(startY >= endY) {
      sy = -1;
    }
    int err = xdist-ydist;
    Image origImg = img;
    Image circle = null;
    ArrayList<int[]> pxlist = new ArrayList<int[]>();
    if(alphaMerge && origImg.getBPP() == Image.MODE_RGBA) {
      img = Image.create(Image.MODE_RGBA, img.getWidth(), img.getHeight(), new Color((byte)0,(byte)0,(byte)0,(byte)0));  
    } else if (lineWidth > 1) {
      img = Image.create(Image.MODE_RGBA, img.getWidth(), img.getHeight(), new Color((byte)0,(byte)0,(byte)0,(byte)0));
    }
    
    while(true) {
      pxlist.add(new int[]{startX, startY});
      if(lineWidth > 1) {
        if(circle == null) {
          circle = Image.create(img.getBPP(), lineWidth+1, lineWidth+1, new Color((byte)0,(byte)0,(byte)0,(byte)0));
          circle(circle, (lineWidth/2), (lineWidth/2), lineWidth, new Color(c.getRed(), c.getGreen(), c.getBlue()), 1, true);
        }
        img.paste(startX-(lineWidth/2), startY-(lineWidth/2), circle, true);
      } else {
        img.setPixel(startX, startY, c);
      }

      if(startX == endX && startY == endY) {
        break;
      }
      int er2 = 2*err;
      if (er2 > -ydist) {
        err-=ydist;
        startX+=sx;
      }
      if(er2 < xdist) {
        err+=xdist;
        startY+=sy;
      }
    } 
    
    if(alphaMerge && origImg.getBPP() == Image.MODE_RGBA) {
      while(pxlist.size() > 0) {
        int[] tmp = pxlist.remove(0);
        Color pc = img.getPixel(tmp[0], tmp[1]);
        if(pc != null) {
          floodFill(img, tmp[0], tmp[1], c, null);
          break;
        }
      } 
      origImg.paste(0, 0, img, alphaMerge);
    }else if (lineWidth > 1) {
      origImg.paste(0, 0, img, true);
    }
  }
}

