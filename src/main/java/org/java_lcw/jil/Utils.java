package org.java_lcw.jil;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class Utils {
  
  
  public static Image awtResizeBiCubic(Image img, int width, int height) {
    BufferedImage orig = img.toBufferedImage(); 
    BufferedImage resizedImage = new BufferedImage(width, height, orig.getType());
    Graphics2D g = resizedImage.createGraphics();
    g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g.drawImage(orig, 0, 0, width, height, null);
    return Image.fromBufferedImage(resizedImage);
  }
  
  public static Image awtResizeNN(Image img, int width, int height) {
    BufferedImage orig = img.toBufferedImage(); 
    BufferedImage resizedImage = new BufferedImage(width, height, orig.getType());
    Graphics2D g = resizedImage.createGraphics();
    g.drawImage(orig, 0, 0, width, height, null);
    g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    return Image.fromBufferedImage(resizedImage);
  }

  public static byte[] bufferedImageToByteArray(BufferedImage BI) {
    return intsToBytes(BI.getRGB(0, 0, BI.getWidth(), BI.getHeight(), null , 0, BI.getWidth()), (byte) 32);
  }
  
  public static byte[] intsToBytes(int[] array, byte bpp) {
    byte cp = (byte) (bpp/8);
    byte[] nArray = new byte[array.length*cp];
    for (int i =0; i< array.length; i++) {
      int c = i*cp;
      nArray[c] = (byte) ((array[i] >> 16) & 0xff);
      nArray[c+1] = (byte) ((array[i] >> 8) & 0xff);
      nArray[c+2] = (byte) (array[i] & 0xff);
      if (cp == 4) {
        nArray[c+3] = (byte) ((array[i] >> 24) & 0xff);
      }
    }
    return nArray;
  }
  
  public static int[] bytesToInts(byte mode, byte[] array) {
    int[] nArray = new int[array.length/(mode/8)];
    for (int i =0; i< nArray.length; i++) {
      int c = i*(mode/8);
      if (mode == 32 ) {
      nArray[i] =  ((array[c+3] << 24 ) & 0xff000000) | ((array[c] << 16) & 0xff0000) | 
          ((array[c+1] << 8) & 0xff00) | ((array[c+2]) & 0xff);
      //Dont hit these because we upsample to 32bit before we make BufferedImage
      } else if (mode == 24) {
        nArray[i] =  ((array[c] << 16)&0xff0000) | 
            ((array[c+1] << 8)&0xff00) | ((array[c+2])&0xff);        
      } else if (mode == 8) {
        nArray[i] =  (255<<24) | ((array[c] << 16)) | 
            ((array[c] << 8)) | ((array[c]));
      }
    }
    return nArray;
  }
}
