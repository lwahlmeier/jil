package org.java_lcw.jil;

import java.io.IOException;

import org.java_lcw.jil.Image.ImageException;

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
  
  public static void fillColor(Image img, int x, int y, Color c) throws IOException, ImageException {

    if (x >= img.getWidth() || y >= img.getHeight() || y < 0 || x < 0) {
      return;
    }
    Color oc = img.getPixel(x, y);
    if(oc.equals(c)) {
      return;
    }

    int cx = x;
    int cy = y;
    Color nc;
    
    while(cy >= 0) {
      while(cx >= 0) {
        nc = img.getPixel(cx, cy);
        if(nc.equals(oc)){
          img.setPixel(cx, cy, c);
        } else {
          break;
        }
        cx-=1;
      }
      cx = x+1;
      while(cx < img.getWidth()) {
        nc = img.getPixel(cx, cy);
        if(nc.equals(oc)){
          img.setPixel(cx, cy, c);
        } else {
          break;
        }
        cx+=1;
      }
      cx = x;
      nc = img.getPixel(cx, cy);
      if(!nc.equals(c)){
        break;
      }
      cy -= 1;

    }
    
    cy = y+1;
    cx = x;
    while(cy < img.getHeight()) {
      while(cx >= 0) {
        nc = img.getPixel(cx, cy);
        if(nc.equals(oc)){
          img.setPixel(cx, cy, c);
        } else {
          break;
        }
        cx-=1;
      }
      cx = x+1;
      while(cx < img.getWidth()) {
        nc = img.getPixel(cx, cy);
        if(nc.equals(oc)){
          img.setPixel(cx, cy, c);
        } 
        else {
          break;
        }
        cx+=1;
      }
      
      cx = x;
      nc = img.getPixel(cx, cy);
      if(!nc.equals(c)){
        break;
      }
      cy+=1;
    }
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
    System.out.println(xdist);
    System.out.println(ydist);

    while(true) {
      for(int l = 0; l < lineWidth; l++) {
        for(int nx = x; nx < l+x; nx++) {
          for(int ny = y; ny < l+y; ny++) {
            if(nx >= 0 && ny >= 0 && nx < img.getWidth() && ny < img.getHeight()) {
              img.setPixel(nx, ny, c);
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
                img.setPixel(nx, ny, c);
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
