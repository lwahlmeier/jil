package org.java_lcw.jil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.java_lcw.jil.Image.ImageException;

public class JpegFile {
  private JpegFile(){
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
    //Have to convert Image to an RGB only
    BufferedImage tmp = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
    tmp.getGraphics().drawImage(image.toBufferedImage(), 0, 0, null);
    ImageIO.write(tmp, "jpeg", file);
  }
  

}
