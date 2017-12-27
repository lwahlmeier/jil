package me.lcw.jil.awt.parsers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import me.lcw.jil.BaseImage;
import me.lcw.jil.ImageException;
import me.lcw.jil.awt.AWTImage;

public class JpegFile {
  public static final float DEFAULT_JPEG_COMPRESSION = 0.8f;
  
  private JpegFile(){
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
  
  public static byte[] saveToByteArray(BaseImage image) throws IOException, ImageException {
    return saveToByteArray(image, DEFAULT_JPEG_COMPRESSION);
  }
  
  public static byte[] saveToByteArray(BaseImage image, float quality) throws IOException, ImageException {
    if(image instanceof AWTImage) {
      return saveToByteArray((AWTImage) image, quality);
    } else {
      return saveToByteArray(AWTImage.fromBaseImage(image), quality);
    }
  }
  
  public static byte[] saveToByteArray(AWTImage image, float quality) throws IOException, ImageException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
    ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
    jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
    jpgWriteParam.setCompressionQuality(quality);
    ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
    jpgWriter.setOutput(ios);
    BufferedImage tmp;
    if(image.getMode() == BaseImage.ImageMode.RGBA32) {
      tmp = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
      tmp.getGraphics().drawImage(image.getBufferedImage(), 0, 0, null);
    } else {
      tmp = image.getBufferedImage();
    }
    jpgWriter.write(null, new IIOImage(tmp, null, null), jpgWriteParam);

    ios.close();
    jpgWriter.dispose();
    return baos.toByteArray();
  }
  
  public static void save(String saveTo, BaseImage image) throws IOException, ImageException {
    save(saveTo, (AWTImage) image, DEFAULT_JPEG_COMPRESSION);
  }
  
  public static void save(String saveTo, BaseImage image, float quality) throws IOException, ImageException {
    if(image instanceof AWTImage) {
      save(saveTo, (AWTImage) image, quality);
    } else {
      save(saveTo, AWTImage.fromBaseImage(image), quality);
    }
  }
  
  public static void save(String saveTo, AWTImage image, float quality) throws IOException, ImageException {
    File file = new File(saveTo);
    ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
    ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
    jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
    jpgWriteParam.setCompressionQuality(quality);
    OutputStream os = new FileOutputStream(file);
    ImageOutputStream ios = ImageIO.createImageOutputStream(os);
    jpgWriter.setOutput(ios);
    
    //Have to convert Image to an RGB only
    BufferedImage tmp;
    if(image.getMode() == BaseImage.ImageMode.RGBA32) {
      tmp = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
      tmp.getGraphics().drawImage(image.getBufferedImage(), 0, 0, null);
    } else {
      tmp = image.getBufferedImage();
    }
    jpgWriter.write(null, new IIOImage(tmp, null, null), jpgWriteParam);

    os.close();
    ios.close();
    jpgWriter.dispose();
  }
}
