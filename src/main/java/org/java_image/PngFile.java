package org.java_image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.IOException;
import javax.imageio.ImageIO;

import org.java_image.Image.ImageException;

import java.awt.image.BufferedImage;

public class PngFile {
  
  private PngFile(){
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
