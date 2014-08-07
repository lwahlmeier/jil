package org.java_lcw.jil;

import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;

public class Utils {

  private static BufferedImage toBufferedForScaling(Image img) {
    if(img.getBPP() == Image.MODE_RGB){
      //NOTE: this ends up in the wrong color space, but since it does not matter for scaling we use it because its faster.
      BufferedImage BB = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
      byte[] test = ((DataBufferByte) BB.getRaster().getDataBuffer()).getData();
      System.arraycopy(img.getArray(), 0, test, 0, test.length);
      return BB;
    } else {
      return img.toBufferedImage();
    }
  }
  private static byte[] fromBufferedForScaling(BufferedImage img) {
    if(img.getType() == BufferedImage.TYPE_3BYTE_BGR){
      //NOTE: this ends up in the wrong color space, but since it does not matter for scaling we use it because its faster.
      byte[] test = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
      return test;
    } else {
      return Image.fromBufferedImage(img).getArray();
    }
  }
  
  public static Image drawSmoothCircle(int diameter, Color color) {
    BufferedImage bufferedImage = new BufferedImage(diameter+2, diameter+2, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = bufferedImage.createGraphics();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
    g2d.setPaint(new java.awt.Color(color.getRed()&0xff, color.getGreen()&0xff,color.getBlue()&0xff, color.getAlpha()&0xff));
    g2d.fillOval(1, 1, diameter, diameter);
    g2d.dispose();
    Image circle = Image.fromBufferedImage(bufferedImage);
    return circle;
    //ImageIO.write(bufferedImage, "png", new File("/tmp/newimage.png"));
  }
  
  @SuppressWarnings("unused")
  private static void getTextImage(String text, String font, byte size) throws IOException {
    Font tmpFont = new Font(font, Font.PLAIN, size);
    Frame f = new Frame(text);
    f.setFont(tmpFont);
    f.setForeground(new java.awt.Color(255, 255, 255));
    //int height = f.getFontMetrics(tmpFont).getHeight();
    //int width = f.getFontMetrics(tmpFont).stringWidth(text);
   
    //BufferedImage tmpBI = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
    //Graphics2D tmpG = tmpBI.createGraphics();
  }
  
  
  public static byte[] awtResizeBiCubic(Image img, int width, int height) {
    BufferedImage orig = toBufferedForScaling(img); 
    BufferedImage resizedImage = new BufferedImage(width, height, orig.getType());
    Graphics2D g = resizedImage.createGraphics();
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
    g.drawImage(orig, 0, 0, width, height, null);
    g.dispose();
    return fromBufferedForScaling(resizedImage);
  }
  
  public static byte[] awtResizeLiner(Image img, int width, int height) {
    BufferedImage orig = toBufferedForScaling(img); 
    BufferedImage resizedImage = new BufferedImage(width, height, orig.getType());
    Graphics2D g = resizedImage.createGraphics();
    g.drawImage(orig, 0, 0, width, height, null);
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    g.dispose();
    return fromBufferedForScaling(resizedImage);
  }
  
  public static byte[] awtResizeNN(Image img, int width, int height) {
    BufferedImage orig = toBufferedForScaling(img); 
    BufferedImage resizedImage = new BufferedImage(width, height, orig.getType());
    Graphics2D g = resizedImage.createGraphics();
    g.drawImage(orig, 0, 0, width, height, null);
    g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    g.dispose();
    return fromBufferedForScaling(resizedImage);
  }

  public static byte[] bufferedImageToByteArray(BufferedImage bufferedImage) {
    if(bufferedImage.getType() == BufferedImage.TYPE_BYTE_GRAY) {
      return ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
    }
    return intsToBytes(bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null , 0, bufferedImage.getWidth()), (byte) 32);
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
  
  public static int[] getAspectSize(int origWidth, int origHeight, int width, int height) {
      int nw = origWidth;
      int nh = origHeight;
      double ratio = nw/(double)nh;
      if (nw != width) {
        nw = width;
        nh = (int)Math.floor(nw/ratio);
      }
      if (nh > height) {
        nh = height;
        nw = (int)Math.floor(nh*ratio);
      }
      width = nw;
      height = nh;
      return new int[]{width, height};
  }
}
