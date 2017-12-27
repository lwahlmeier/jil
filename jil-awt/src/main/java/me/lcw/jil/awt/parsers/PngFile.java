package me.lcw.jil.awt.parsers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import me.lcw.jil.BaseImage;
import me.lcw.jil.ImageException;
import me.lcw.jil.awt.AWTImage;

public class PngFile {
  
  private PngFile(){
  }
  
  public static AWTImage open(byte[] ba) throws IOException, ImageException {
    return open(new ByteArrayInputStream(ba));
  }
  
  public static AWTImage open(InputStream is) throws IOException, ImageException {
    return AWTImage.fromBufferedImage(ImageIO.read(is));
  }
  
  public static AWTImage open(String filename) throws IOException, ImageException {
    return AWTImage.fromBufferedImage(ImageIO.read(new File(filename)));    
  }
  
  public static void save(String saveTo, AWTImage image) throws IOException, ImageException {
    File file = new File(saveTo);
    ImageIO.write(image.getBufferedImage(), "png", file);
  }
  
  public static byte[] saveToByteArray(AWTImage image) throws IOException, ImageException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    BufferedImage tmp;
    if(image.getMode() == BaseImage.ImageMode.RGBA32) {
      tmp = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
      tmp.getGraphics().drawImage(image.getBufferedImage(), 0, 0, null);
    } else {
      tmp = image.getBufferedImage();
    }
    ImageIO.write(tmp, "png", baos);
    return baos.toByteArray();
  }
  
  
  

}
