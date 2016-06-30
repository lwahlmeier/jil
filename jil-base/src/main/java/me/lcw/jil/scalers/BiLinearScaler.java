package me.lcw.jil.scalers;

import me.lcw.jil.BaseImage;
import me.lcw.jil.Color;
import me.lcw.jil.JilImage;

public class BiLinearScaler {

  public static JilImage scale(final JilImage srcImage, final int newWidth, final int newHeight) {

    final JilImage newImage = JilImage.create(srcImage.getMode(), newWidth, newHeight);
    final float x_ratio = ((float)(srcImage.getWidth()))/newWidth ;
    final float y_ratio = ((float)(srcImage.getHeight()))/newHeight ;
    float x_diff, y_diff;
    int px, py, py1, px1;
    final int[] r = new int[4];
    for (int y=0; y < newHeight; y++){
      py = (int)(y_ratio * y);
      y_diff = (y_ratio * y) - py;
      
      py1 = BiLinearScaler.clampYPos(srcImage.getHeight(), py+1);

      for (int x=0; x < newWidth; x++){
        px = (int)(x_ratio * x);
        x_diff = (x_ratio * x) - px;
        px1 = BiLinearScaler.clampXPos(srcImage.getWidth(), px+1);

        for(byte c = 0; c < srcImage.getColors(); c++) {
          r[0] = srcImage.getByteInChannel(px, py, c) & 0xff;
          r[1] = srcImage.getByteInChannel(px1, py, c) & 0xff;
          r[2] = srcImage.getByteInChannel(px, py1, c) & 0xff;
          r[3] = srcImage.getByteInChannel(px1, py1, c) & 0xff;
//          System.out.println(
//              r[0]*(1-x_diff)*(1-y_diff) + ":"+  
//              r[1]*(x_diff)  *(1-y_diff) +":"+
//              r[2]*(y_diff)  *(1-x_diff) +":"+
//              r[3]*(x_diff)  *(y_diff) );

          newImage.setPixelInChannel(x, y, c, (byte) (
              r[0]*(1-x_diff)*(1-y_diff) +  
              r[1]*(x_diff)  *(1-y_diff) +
              r[2]*(y_diff)  *(1-x_diff) +
              r[3]*(x_diff)  *(y_diff) ));
        }
      }
    }
    return newImage;    
  }

  public static void scaleGeneric(final BaseImage srcImage, final BaseImage newImage) {

    final float x_ratio = ((float)(srcImage.getWidth()))/newImage.getWidth() ;
    final float y_ratio = ((float)(srcImage.getHeight()))/newImage.getHeight() ;
    float x_diff, y_diff;
    int px, py;
    for (int y=0; y < newImage.getHeight(); y++){
      py = (int)(y_ratio * y);
      y_diff = (y_ratio * y) - py;
      for (int x=0; x < newImage.getWidth(); x++){
        px = (int)(x_ratio * x);
        x_diff = (x_ratio * x) - px;
        newImage.setPixel(x, y, getScaleColor(srcImage, px, py, x_diff, y_diff, x, y));
      }
    }
   
  }
  private static Color getScaleColor(final BaseImage bi, int x, int y, float x_diff, float y_diff, int sx,int sy) {
    Color[] ca = new Color[4];
    int cx = BiLinearScaler.clampXPos(bi.getWidth(), x+1);
    int cy = BiLinearScaler.clampYPos(bi.getHeight(), y+1);
    ca[0] = bi.getPixel(x, y);
    ca[1] = bi.getPixel(cx, y);
    ca[2] = bi.getPixel(x, cy);
    ca[3] = bi.getPixel(cx, cy);
    Color c = null;
    if(bi.getMode() == BaseImage.MODE.RGBA) {
      c= new Color( 
          (byte)(
              (ca[0].getRed()&0xff) *(1-x_diff) *(1-y_diff) +
              (ca[1].getRed()&0xff) *(x_diff)   *(1-y_diff) +
              (ca[2].getRed()&0xff) *(y_diff)*(1-x_diff)  + 
              (ca[3].getRed()&0xff) *(x_diff)   *(y_diff)),
          (byte)((ca[0].getGreen()&0xff)*(1-x_diff)*(1-y_diff) +
              (ca[1].getGreen()&0xff)*(x_diff)*(1-y_diff) + 
              (ca[2].getGreen()&0xff)*(y_diff)*(1-x_diff) + 
              (ca[3].getGreen()&0xff)*(x_diff)*(y_diff)),
          (byte)((ca[0].getBlue()&0xff)*(1-x_diff)*(1-y_diff) +
              (ca[1].getBlue()&0xff)*(x_diff)*(1-y_diff) + 
              (ca[2].getBlue()&0xff)*(y_diff)*(1-x_diff) + 
              (ca[3].getBlue()&0xff)*(x_diff)*(y_diff)),
          (byte)((ca[0].getAlpha()&0xff)*(1-x_diff)*(1-y_diff) +
              (ca[1].getAlpha()&0xff)*(x_diff)*(1-y_diff) + 
              (ca[2].getAlpha()&0xff)*(y_diff)*(1-x_diff) + 
              (ca[3].getAlpha()&0xff)*(x_diff)*(y_diff))
          );
    } else if (bi.getMode() == BaseImage.MODE.RGB){
      c= new Color( 
          (byte)((ca[0].getRed()&0xff)*(1-x_diff)*(1-y_diff) +
              (ca[1].getRed()&0xff)*(x_diff)*(1-y_diff) + 
              (ca[2].getRed()&0xff)*(y_diff)*(1-x_diff) + 
              (ca[3].getRed()&0xff)*(x_diff)*(y_diff)),
          (byte)((ca[0].getGreen()&0xff)*(1-x_diff)*(1-y_diff) +
              (ca[1].getGreen()&0xff)*(x_diff)*(1-y_diff) + 
              (ca[2].getGreen()&0xff)*(y_diff)*(1-x_diff) + 
              (ca[3].getGreen()&0xff)*(x_diff)*(y_diff)),
          (byte)((ca[0].getBlue()&0xff)*(1-x_diff)*(1-y_diff) +
              (ca[1].getBlue()&0xff)*(x_diff)*(1-y_diff) + 
              (ca[2].getBlue()&0xff)*(y_diff)*(1-x_diff) + 
              (ca[3].getBlue()&0xff)*(x_diff)*(y_diff))
          );
    } else if (bi.getMode() == BaseImage.MODE.GREY){
      c= new Color( 
          (byte)((ca[0].getGrey()&0xff)*(1-x_diff)*(1-y_diff) +
              (ca[1].getGrey()&0xff)*(x_diff)*(1-y_diff) + 
              (ca[2].getGrey()&0xff)*(y_diff)*(1-x_diff) + 
              (ca[3].getGrey()&0xff)*(x_diff)*(y_diff))
          );
    }
    return c;
    
  }

  private static int clampXPos(int width, int x) {
    if(x < 0) {
      return 0;
    } else if (x > (width-1)) {
      return (width-1);
    }
    return x;
  }

  private static int clampYPos(int height, int y) {
    if(y < 0) {
      return 0;
    } else if (y > (height-1)) {
      return (height-1);
    }
    return y;
  }
}
