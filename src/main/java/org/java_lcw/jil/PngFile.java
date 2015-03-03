package org.java_lcw.jil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.java_lcw.jil.ImageException;

public class PngFile {
  
  private PngFile(){
  }
  
  public static JavaImage open(InputStream is) throws IOException, ImageException {
    BufferedImage bi = ImageIO.read(is);
    return JavaImage.fromBufferedImage(bi);
  }
  
  public static JavaImage open(String filename) throws IOException, ImageException {
    File file = new File(filename);
    return JavaImage.fromBufferedImage(ImageIO.read(file));    
  }
  
  public static void save(String saveTo, Image image) throws IOException, ImageException {
    File file = new File(saveTo);
    ImageIO.write(image.toJavaImage().toBufferedImage(), "png", file);
  }
  
  
  

}
