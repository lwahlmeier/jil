package me.lcw.jil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import me.lcw.jil.ImageException;

public class PngFile {
  
  private PngFile(){
  }
  
  public static JilImage open(InputStream is) throws IOException, ImageException {
    BufferedImage bi = ImageIO.read(is);
    return JilImage.fromBufferedImage(bi);
  }
  
  public static JilImage open(String filename) throws IOException, ImageException {
    File file = new File(filename);
    return JilImage.fromBufferedImage(ImageIO.read(file));    
  }
  
  public static void save(String saveTo, Image image) throws IOException, ImageException {
    File file = new File(saveTo);
    ImageIO.write(image.toJilImage().toBufferedImage(), "png", file);
  }
  
  
  

}
