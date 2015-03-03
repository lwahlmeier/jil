package org.java_lcw.jil.scalers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

import org.java_lcw.jil.Color;
import org.java_lcw.jil.JavaImage;
import org.threadly.concurrent.NoThreadScheduler;
import org.threadly.concurrent.PriorityScheduler;
import org.threadly.concurrent.future.FutureUtils;
import org.threadly.concurrent.future.ListenableFuture;
import org.threadly.util.ExceptionUtils;

public class ThreadedBiCubicScaler {
  //private static final PriorityScheduler PS = new PriorityScheduler(10, 10, Integer.MAX_VALUE);
  private LinkedList<Thread> threads = new LinkedList<>();
  private LinkedList<NoThreadScheduler> ntss = new LinkedList<>();
  private JavaImage srcImage;
  private byte[] srcImageArray;
    
  private ThreadedBiCubicScaler() {
    
  }
  
  public static JavaImage scale(JavaImage srcImage, int newWidth, int newHeight) {
    ThreadedBiCubicScaler tmp = new ThreadedBiCubicScaler();
    tmp.srcImage = srcImage;
    tmp.srcImageArray = srcImage.getArray();
    return tmp.doScale(newWidth, newHeight);
  }
  
  private JavaImage doScale(final int newWidth, final int newHeight){
    final JavaImage newImage = JavaImage.create(srcImage.getBPP(), newWidth, newHeight);
    final float x_ratio = ((float)(srcImage.getWidth()))/newWidth ;
    final float y_ratio = ((float)(srcImage.getHeight()))/newHeight ;
    ArrayList<ListenableFuture<?>> flist = new ArrayList<>();
    for(int i =0; i<10; i++) {
      final NoThreadScheduler nts = new NoThreadScheduler(false);
      Runnable r = new Runnable() {
        @Override
        public void run() {
          try {
            nts.tick(null);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }};
      ntss.add(nts);
      Thread t = new Thread(r);
      threads.add(t);
    }
    
    for (int y = 0; y < newHeight; y++){
      final int py = (int)(y_ratio * y);
      final float y_diff = (y_ratio * y) - py;
      final int fy = y;
      Runnable r = new Runnable() {
        @Override
        public void run() {
          for (int x = 0; x < newWidth; x++){
            int px = (int)(x_ratio * x);
            float x_diff = (x_ratio * x) - px;
            Color c = getInterpolatedPixel(px, py, x_diff, y_diff);
            newImage.setPixel(x, fy, c);
          }
        };
      };
      int tmp = y%10;
      ntss.get(tmp).execute(r);
    }
    for(Thread t: threads) {
      t.start();
    }
    for(Thread t: threads) {
      try {
        t.join();
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    System.out.println(threads.size());
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
  
  private byte getInterpolatedValues(byte c0, byte c1, byte c2, byte c3, float t) {
    int p0 = c0 & 0xff;
    int p1 = c1 & 0xff;
    int p2 = c2 & 0xff;
    int p3 = c3 & 0xff;
    int p = (p3 - p2) - (p0 - p1);
    int q = (p0 - p1) - p;
    int r = p2 - p0;
    int s = p1;
    float tSqrd = t*t;
    
    //double X = (p * (tSqrd * t)) + (q * tSqrd) + (r * t) + s;
    float X = ((p * (tSqrd * t) + 0.5f) + (q * tSqrd + 0.5f) + (r * t + 0.5f) + s);
    //double X = ((p1 + 0.5 * t*(p2 - p0 + t*(2.0*p0 - 5.0*p1 + 4.0*p2 - p3 + t*(3.0*(p1 - p2) + p3 - p0)))) );
    //double X = ((p1 + 0.5 * t*(p2 - p0 + t*(2.0*p0 - 5.0*p1 + 4.0*p2 - p3 + t*(3.0*(p1 - p2) + p3 - p0)))) );
    //double X = ( p0 + p1 + p2 + p3 ) / 4;
    
    //double X = (-0.5 * p0 + 1.5 * p1 - 1.5 * p2 + 0.5 * p3) * 
    //    Math.pow(t,3)+(p0-2.5f*p1+2.f*p2-0.5f*p3) * 
    //    Math.pow(t,2)+(-0.5f*p0+0.5f*p2)*t+p1;

    //Constrain to a Byte
    byte R;
    if (X < 0) {
      R = 0;
    } else if (X > 255) {
      R = -1;
    } else {
      R = (byte) X;
    }
    return R;
  }
  
  private Color getInterpolatedPixel(int x, int y, float tx, float ty){
    int cX;
    int cY;
    int cSize = srcImage.getColors();
    byte[][] newColors = new byte[4][];    
    byte[][] pixles = new byte[4][];
    for(int i=0; i<pixles.length; i++){
      pixles[i] = new byte[cSize];
      newColors[i] = new byte[cSize];
    }
    
    for(int newY = 0; newY<4; newY++){
      cY = this.clampYPos(y+newY-1);
      for(int newX = 0; newX<4; newX++){
        cX = this.clampXPos(x+newX-1);
        int cp = ((cY*srcImage.getWidth())+cX)*cSize;
        for(int i=0; i<cSize; i++){ 
          pixles[newX][i] = srcImageArray[cp+i];
        }
      }
      //Combine Rows into 1 px
      for(int i=0; i<cSize; i++){ 
        newColors[newY][i] = getInterpolatedValues(pixles[0][i],pixles[1][i],pixles[2][i],pixles[3][i], tx);
      }
    }
    Color last = null;
    if(cSize == 1) { 
      last = new Color (getInterpolatedValues(newColors[0][0],newColors[1][0],newColors[2][0],newColors[3][0], ty));
    } else if(cSize == 3) {
      last = new Color (
          getInterpolatedValues(newColors[0][0],newColors[1][0],newColors[2][0],newColors[3][0], ty),
          getInterpolatedValues(newColors[0][1],newColors[1][1],newColors[2][1],newColors[3][1], ty),
          getInterpolatedValues(newColors[0][2],newColors[1][2],newColors[2][2],newColors[3][2], ty)
          );
    } else if (cSize == 4) {
      last = new Color (
          getInterpolatedValues(newColors[0][0],newColors[1][0],newColors[2][0],newColors[3][0], ty),
          getInterpolatedValues(newColors[0][1],newColors[1][1],newColors[2][1],newColors[3][1], ty),
          getInterpolatedValues(newColors[0][2],newColors[1][2],newColors[2][2],newColors[3][2], ty),
          getInterpolatedValues(newColors[0][3],newColors[1][3],newColors[2][3],newColors[3][3], ty)
          );
    }
    return last; 
  }
}
