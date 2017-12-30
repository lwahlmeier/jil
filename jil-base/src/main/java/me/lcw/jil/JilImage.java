package me.lcw.jil;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import me.lcw.jil.Utils.ImageFillUtils;
import me.lcw.jil.Utils.JilUtils;
import me.lcw.jil.parsers.png.PNGEncoder;
import me.lcw.jil.parsers.tiff.TIFFDecoder;
import me.lcw.jil.parsers.tiff.TIFFEncoder;
import me.lcw.jil.scalers.BiCubicScaler;
import me.lcw.jil.scalers.BiLinearScaler;
import me.lcw.jil.scalers.NearestNeighborScaler;


/**
 *
 * Main Image object used to construct new Image files.  
 * 
 * @author lcw - Luke Wahlmeier
 */
public abstract class JilImage implements BaseImage {

  private final int width;
  private final int height;
  private final ImageMode mode;
  
  protected JilImage(ImageMode mode, int width, int height) {
    this.mode = mode;
    this.width = width;
    this.height = height;
  }

  /**
   * Main Method for creating a new Image
   * 
   * @param mode Image mode, uses the static bytes Image.MODE_(L, RGB, RGBA)
   * @param width How wide the image should be in pixels
   * @param height How high the Image should be in pixels
   * @return Returns an Image object
   */
  public static JilImage create(ImageMode mode, int width, int height) {
    return new JilByteImage(mode, width, height);
  }

  /**
   * Creating a new Image with a default color
   * 
   * @param mode Image mode, uses the static bytes Image.MODE_(L, RGB, RGBA)
   * @param width How wide the image should be in pixels
   * @param height How high the Image should be in pixels
   * @param color default color to set for the image
   * @return Returns an Image object
   */
  public static JilImage create(ImageMode mode, int width, int height, Color color) {
    JilImage i = new JilByteImage(mode, width, height);
    i.fillImageWithColor(color);
    return i;
  }


  /**
   * Create an Image object from a byte Array.  The byte array must be a single array
   * of bytes representing the mode given (L, RGB, or RGBA)
   * @param mode Image mode, uses the static bytes Image.MODE_(L, RGB, RGBA)
   * @param width How wide the image should be in pixels
   * @param height How high the Image should be in pixels
   * @param data byte[] to use to loading the data
   * @return Returns an Image object with the provided byte[] set in it
   */
  public static JilImage fromByteArray(ImageMode mode, int width, int height, byte[] data) {
    if(data.length != (width*height*mode.getColors())){
      throw new RuntimeException("Incorrect number of bytes to make an image of that type");
    }
    JilImage image = new JilByteImage(mode, width, height, data);
    return image;
  }

  /**
   * Static Method that allows you to open a file, just pass in the path/filename. 
   * @param filename  Filename to attempt to open.
   * @return Returns an Image object from the provided file.
   * @throws ImageException This can happen if we do not know the type of file we where asked to open.
   * @throws IOException This happens when we can not access the file.
   */
  public static JilImage open(String filename) throws ImageException, IOException {
    try {
      return open(filename, JilUtils.getImageType(filename));
    } catch(ImageException e) {
      for(ImageType t: ImageType.values()) {
        try {
          return open(filename, t);
        } catch(ImageException e1) {
        }
      }
      throw new ImageException("Could not figure out image type!");
    }
  }

  /**
   * Static Method that allows you to open a file, just pass in the path/filename. 
   * @param filename  Filename to attempt to open.
   * @param type Type of file to open used Image.ImageType.(TIFF, PNG, JPEG)
   * @return Returns an Image object from the provided file.
   * @throws ImageException This can happen if we do not know the type of file we where asked to open.
   * @throws IOException This happens when we can not access the file.
   * 
   */
  public static JilImage open(String filename, ImageType type) throws IOException, ImageException {
    JilImage image;
    switch(type) {
    case TIFF:
      image = TIFFDecoder.decodeFromFile(new File(filename));
      break;
    case PNG:
    case JPEG:
    default:
      throw new ImageException("Could not determen filetype");
    }
    return image;
  }

  @Override
  public void save(File file) throws IOException, ImageException {
    save(file.getAbsolutePath());
  }

  @Override
  public void save(File file, ImageType type) throws IOException, ImageException {
    save(file.getAbsolutePath(), type);
  }

  @Override
  public void save(String filename) throws IOException, ImageException {
    ImageType type = JilUtils.getImageType(filename);
    save(filename, type);
  }

  public void save(String filename, ImageType type) throws IOException, ImageException{
    switch(type) {
    case TIFF:
      TIFFEncoder.encodeToFile(this, new File(filename));
      break;
    case PNG:
      PNGEncoder.encodeToFile(this, new File(filename));
      break;
    default:
      throw new ImageException("Could not determen file type");
    }
  }

  @Override
  public final ImageMode getMode(){
    return this.mode;
  }  

  @Override
  public final int getColors(){
    return this.mode.getColors();
  }  

  @Override
  public final int getWidth(){
    return this.width;
  }

  @Override
  public final int getHeight(){
    return this.height;
  }
  
  @Override
  public final JilImage resizeWithBorders(int bWidth, int bHeight, Color borderColor, ScaleType st) {
    JilImage ib = JilImage.create(getMode(), bWidth, bHeight, borderColor);

    JilImage newI = resize(bWidth, bHeight, true, st);

    if(newI.getHeight() == ib.getHeight()) {
      int pos = (ib.getWidth()/2) - (newI.getWidth()/2);
      ib.paste(pos, 0, newI);
    } else {
      int pos = (ib.getHeight()/2)  - (newI.getHeight()/2);
      ib.paste(0, pos, newI);
    }

    return ib;
  }

  @Override
  public final JilImage resize(int newWidth, int newHeight, boolean keepAspect, ScaleType st) {
    if(keepAspect) {
      int[] aspect = JilUtils.getAspectSize(getWidth(), getHeight(), newWidth, newHeight);
      newWidth = aspect[0];
      newHeight = aspect[1];
    }

    switch(st) {
    case LINER:
      return BiLinearScaler.scale(this, newWidth, newHeight);
    case CUBIC:
      return BiCubicScaler.scale(this, newWidth, newHeight);
    case CUBIC_SMOOTH:
      return (JilImage)JilUtils.biCubicSmooth(this, newWidth, newHeight);
    default:
      return NearestNeighborScaler.scale(this, newWidth, newHeight);
    }
  }
  
  @Override
  public final void fillImageWithColor(Color c) {
    for(int y=0; y<getHeight(); y++) {
      for(int x=0; x<getWidth(); x++) {
        setPixel(x,y,c);
      }
    }
  }
  
  public abstract JilImage changeMode(ImageMode nmode);
  public abstract JilImage copy();
  public abstract JilImage cut(int x, int y, int width, int height);
  public abstract JilImage toJilImage();
  public abstract Draw draw();
  public abstract byte[] getByteArray();
  public abstract Color getPixel(int x, int y);
  public abstract void setPixel(int x, int y, Color c);
  public abstract void mkRandom();
  public abstract void paste(int x, int y, BaseImage img);
  public abstract void merge(int x, int y, BaseImage img);
  
  

  private static class JilDraw implements Draw {

    private final JilImage ji;

    public JilDraw (JilImage ji) {
      this.ji = ji;
    }


    @Override
    public void rect(int x, int y, int w, int h, Color c, int lineWidth, boolean fill) {
      int maxW = x+w;
      int maxH = y+h;
      if (ji.getWidth() < maxW) {
        maxW = ji.getWidth() ;
      }
      if (ji.getHeight() < maxH) {
        maxH = ji.getHeight();
      }
      if(x < 0) {
        x=0;
      }
      if(y < 0) {
        y=0;
      }
      for(int H = y; H<maxH; H++) {
        for(int W = x;  W<maxW; W++) {
          if(W <= (x+lineWidth)-1 || H <= (y+lineWidth)-1 || H >= maxH-lineWidth || W >= maxW-lineWidth) {
            ji.setPixel(W, H, c);
          } 
        }
      }
      if(fill) {
        int tx = x+ lineWidth+1;
        int ty = y+ lineWidth+1;
        floodFill(tx, ty, c, c, false);
      }
    }

    public void floodFill(int x, int y, Color c) {
      floodFill( x, y, c, null);
    }

    public void floodFill(int x, int y, Color c, Color edge) {
      floodFill( x, y, c, edge, false);
    }

    @Override
    public void floodFill(int x, int y, Color c, Color edge, boolean keepAlpha) {
      if(x < 0 || x >= ji.getWidth()) {
        return;
      }
      if(y <0 || y>=ji.getWidth()) {
        return;
      }
      if(edge == null) {
        ImageFillUtils.noEdgeLineFill(ji, x, y, c, keepAlpha);
      } else {
        ImageFillUtils.edgeCustomFill(ji, x, y, c, edge, keepAlpha);
      }
    }

    @Override
    public void fillColor(int x, int y, Color c) {
      floodFill(x,y,c);
    }

    @Override
    public void circle(int cx, int cy, int size, Color c, int lineWidth, boolean fill) {
      int r = size/2;
      int points = Math.max(r/16, 5);
      for(int i=0; i<360*points; i++) {
        int px = (int)Math.round(cx+Math.cos(i*Math.PI/180.0/points)*r);
        int py = (int)Math.round(cy+Math.sin(i*Math.PI/180.0/points)*r);
        if(px >= 0 && py >= 0 && py < ji.getHeight() && px < ji.getWidth()) {
          ji.setPixel(px, py, c);
        }
      }

      if(fill) {
        lineWidth = size;
      }

      for(int l = 1; l<lineWidth; l++){
        size -=1;
        circle(cx, cy, size, c, 1, false);
      }
    }

    @Override
    public void line(int x, int y, int x2, int y2, Color c, int lineWidth, boolean alphaMerge) {
      List<int[]> pxlist = JilUtils.lineToList(x, y, x2, y2);
      JilImage newImg = ji;
      JilImage circle = null;

      if(lineWidth > 1) {
        newImg = JilImage.create(BaseImage.ImageMode.RGBA32, ji.getWidth(), ji.getHeight());  
        if(circle == null) {
          circle = JilImage.create(BaseImage.ImageMode.RGBA32, lineWidth+1, lineWidth+1);
          circle.draw().circle((lineWidth/2), (lineWidth/2), lineWidth, Color.fromRGBBytes(c.getRedByte(), c.getGreenByte(), c.getBlueByte()), 1, true);
        }
      }

      for(int[] ai: pxlist) {
        if(lineWidth > 1) {
          newImg.merge(ai[0]-(lineWidth/2), ai[1]-(lineWidth/2), circle);
        } else {
          newImg.setPixel(ai[0], ai[1], c);
        }
      }

      if(lineWidth > 1) {
        while(pxlist.size() > 0) {
          int[] tmp = pxlist.remove(0);
          Color pc = newImg.getPixel(tmp[0], tmp[1]);
          if(pc != null) {
            newImg.draw().floodFill(tmp[0], tmp[1], c, Color.ALPHA, false);
            break;
          }
        }
        ji.merge(0, 0, newImg);
      }
    }
  }
  
  @Override
  public boolean equals(Object o) {
    if(o instanceof BaseImage) {
      BaseImage bi = (BaseImage)o;
      if(bi.getWidth() == width && bi.getHeight() == height && bi.getMode() == mode) {
        return Arrays.equals(this.getByteArray(), bi.getByteArray());
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return "JilImage: width:"+width+": height"+height+": mode:"+mode.toString();
  }
}
