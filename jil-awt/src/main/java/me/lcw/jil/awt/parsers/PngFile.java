package me.lcw.jil.awt.parsers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import me.lcw.jil.ImageException;
import me.lcw.jil.awt.AWTImage;

public class PngFile {
  
  private PngFile(){
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
  
  
  

}
