package org.java_lcw.jil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.java_lcw.jil.Image.ImageException;

public class PngFile {
  
  private PngFile(){
  }
  
  public static Image open(InputStream is) throws IOException, ImageException {
    BufferedImage bi = ImageIO.read(is);
    return Image.fromBufferedImage(bi);
  }
  
  public static Image open(String filename) throws IOException, ImageException {
    File file = new File(filename);
    return Image.fromBufferedImage(ImageIO.read(file));    
  }
  
  public static void save(String saveTo, Image image) throws IOException, ImageException {
    File file = new File(saveTo);
    ImageIO.write(image.toBufferedImage(), "png", file);
  }
  

}
