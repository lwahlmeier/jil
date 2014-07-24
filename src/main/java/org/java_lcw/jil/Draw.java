package org.java_lcw.jil;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayDeque;
import java.util.Random;

import org.java_lcw.jil.Image.ImageException;

public class Draw {

  public static void rect(Image img, int x, int y, int w, int h, Color c, int lineWidth, boolean fill) {
    synchronized(img) {
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
  }

  public static void floodFill(Image img, int x, int y, Color c, Color edge) throws IOException, ImageException {
    synchronized(img) {
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
            if(tmpC.equals(OC)) {
              img.setPixel(ce[0], ce[1], c);
              pl.add(new Integer[]{ce[0]+1, ce[1]});
              pl.add(new Integer[]{ce[0]-1, ce[1]});
              pl.add(new Integer[]{ce[0], ce[1]+1});
              pl.add(new Integer[]{ce[0], ce[1]-1});
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
              pl.add(new Integer[]{ce[0]+1, ce[1]});
              pl.add(new Integer[]{ce[0]-1, ce[1]});
              pl.add(new Integer[]{ce[0], ce[1]+1});
              pl.add(new Integer[]{ce[0], ce[1]-1});
            }
          } catch(ArrayIndexOutOfBoundsException e) { 
          }

        }
      }
    }
  }

  public static void fillColor(Image img, int x, int y, Color c) throws IOException, ImageException {
    floodFill(img,x,y,c, null);
  }

  public static void circle(Image img, int cx, int cy, int size, Color c, int lineWidth, boolean fill){
    synchronized(img) {
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
  }

  public static void line(Image img, int startX, int startY, int endX, int endY, Color c, int lineWidth) {
    synchronized(img) {
      int xdist = Math.abs(endX-startX);
      int ydist = Math.abs(endY-startY);
      int err = xdist - ydist;

      int sx = 1;
      int sy = 1;
      if(startX > endX) {
        sx = -1;
      }
      if(startY > endY) {
        sy = -1;
      }

      int x = startX;
      int y = startY;
      int e2 = 0;

      while(true) {
        for(int l = 0; l < lineWidth; l++) {
          for(int nx = x; nx < l+x; nx++) {
            for(int ny = y; ny < l+y; ny++) {
              if(nx >= 0 && ny >= 0 && nx < img.getWidth() && ny < img.getHeight()) {
                img.setPixel(nx, ny, c, true);
              }
            }
          }
        }

        if(x == endX && y == endY){
          break;
        }

        e2 = 4*err;
        if(e2 > -ydist) {
          err = err-ydist;
          x = x + sx;
        }

        if(x == endX && y == endY){
          for(int l = 0; l < lineWidth; l++) {
            for(int nx = x; nx < l+x; nx++) {
              for(int ny = y; ny < l+y; ny++) {
                if(nx >= 0 && ny >= 0 && nx < img.getWidth() && ny < img.getHeight()) {
                  img.setPixel(nx, ny, c, true);
                }
              }
            }
          }
          break;
        }
        if (e2 < xdist) {
          err = err +xdist;
          y = y + sy;
        }
      }
    }
  }
}

