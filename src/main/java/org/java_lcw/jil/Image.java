package org.java_lcw.jil;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.java_lcw.jil.scalers.BiCubicScaler;
import org.java_lcw.jil.scalers.BiLinearScaler;
import org.java_lcw.jil.scalers.NearestNeighborScaler;


/**
 *
 * Main Image object used to construct new Image files.  
 * 
 * @author lcw - Luke Wahlmeier
 */
public class Image {
  /**
   * MODE_L is grey scaled image (8 bits per pixel, 1 channel)
   */
  public static final byte MODE_L = 8;
  /**
   * MODE_RGB is an RGB (Red Green Blue) image Each color is its own Channel (24 bits per pixel, 3 channels)
   */
  public static final byte MODE_RGB = 24;
  /**
   * MODE_RGBA is an RGBA (Red Green Blue Alpha) image Each color and alpha has its own Channel (32 bits per pixel, 4 channels)
   */
  public static final byte MODE_RGBA = 32;
  
  private final int width;
  private final int height;
  private final byte bpp;
  private final byte colors;
  protected byte[] MAP;
  
  /**
   * Image Types for Image Object to use (open/save)
   * 
   * @author lcw - Luke Wahlmeier
   *
   */
  public enum ImageType {TIFF, JPEG, PNG};
  
  /**
   * ScaleTypes that can be used when resizing an Image.
   * NN      - Nearest Neighbor - Very fast but kind somewhat noticeable scaler (Default).
   * LINER   - This is BiLiner, its very fast and descent quality.
   * CUBIC   - This is BiCubic, its looks very good in most situations but is a little slower.
   * LANCZOS - This one has the highest Quality but is pretty slow. 
   * @author lcw - Luke Wahlmeier
   *
   */
  public enum ScaleType {NN, LINER, CUBIC, AWT_NN, AWT_LINER, AWT_CUBIC};
  
  private Image(byte mode, int width, int height) {
    colors = (byte) (mode/8);
    int size = colors*width*height;
    MAP = new byte[size];
    this.width = width;
    this.height = height;
    this.bpp = mode;
  }
  
  private Image(byte mode, int width, int height, byte[] map) {
    colors = (byte) (mode/8);
    MAP = map;
    this.width = width;
    this.height = height;
    this.bpp = mode;
  }

  /**
   * Main Method for creating a new Image
   * 
   * @param mode Image mode, uses the static bytes Image.MODE_(L, RGB, RGBA)
   * @param width How wide the image should be in pixels
   * @param height How high the Image should be in pixels
   * @return Returns an Image object
   */
  public static Image create(byte mode, int width, int height) {
    return new Image((byte)(mode), width, height);
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
  public static Image create(byte mode, int width, int height, Color color) {
    Image i = new Image((byte)(mode), width, height);
    i.fillColor(color);
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
   * @throws ImageException This happens if the data provided is to large or to small for the (mode/8)*width*height
   */
  public static Image fromByteArray(byte mode, int width, int height, byte[] data) {
    
    byte cBytes = (byte)(mode/8);
    if(data.length != (width*height*cBytes)){
      throw new RuntimeException("Incorrect number of bytes to make an image of that type");
    }
    Image image = new Image(mode, width, height, data);
    return image;
  }
  
  /**
   * Static Method that allows you to open a file, just pass in the path/filename. 
   * @param filename  Filename to attempt to open.
   * @return Returns an Image object from the provided file.
   * @throws ImageException This can happen if we do not know the type of file we where asked to open.
   * @throws IOException This happens when we can not access the file.
   */
  public static Image open(String filename) throws ImageException, IOException {
    try {
      return open(filename, getImageType(filename));
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
  public static Image open(String filename, ImageType type) throws IOException, ImageException {
    Image image;
    switch(type) {
    case TIFF:
      image = TiffFile.open(filename);
      break;
    case PNG:
      image = PngFile.open(filename);
      break;
    case JPEG:
      image = JpegFile.open(filename);
      break;
    default:
      throw new ImageException("Could not determen filetype");
    }
    return image;
  }
  
  /**
   * Save the image to the given file name.  We determine the type based on the file extension (required) 
   * @param file Location for the file to be written out to
   * @throws IOException This happens if we can not save/open that file
   * @throws ImageException This happens if we can not figure out the type you want use to save as 
   */
  public void save(File file) throws IOException, ImageException {
    save(file.getAbsolutePath());
  }

  /**
   * Save the image for a given location with the provided type.
   * @param file Location for the file to be written out to
   * @param type Type of file to open used Image.ImageType.(TIFF, PNG, JPEG)
   * @throws IOException This happens if we can not save/open that file
   * @throws ImageException This happens if we can not figure out the type you want use to save as 
   */
  public void save(File file, ImageType type) throws IOException, ImageException {
    save(file.getAbsolutePath(), type);
  }
  
  /**
   * Save the image to the given file name.  We determine the type based on the file extension (required) 
   * @param filename Path/Name of the file to save
   * @throws IOException This happens if we can not save/open that file
   * @throws ImageException This happens if we can not figure out the type you want use to save as 
   */
  public void save(String filename) throws IOException, ImageException {
    ImageType type = getImageType(filename);
    save(filename, type);
  }
  
  /**
   * Save the image to the given file name.
   * @param filename Path/Name of the file to save
   * @param type Type of file to open used Image.ImageType.(TIFF, PNG, JPEG)
   * @throws IOException This happens if we can not save/open that file
   * @throws ImageException This happens if we can not figure out the type you want use to save as 
   */
  public void save(String filename, ImageType type) throws IOException, ImageException{
    switch(type) {
    case TIFF:
      TiffFile.save(filename, this);
      break;
    case PNG:
      PngFile.save(filename, this);
      break;
    case JPEG:
      JpegFile.save(filename, this);
      break;
    default:
      throw new ImageException("Could not determen file type");
    }
  }

  /**
   * Create an Image from a BufferedImage from AWT - The new Image will always be RGBA type
   * @param BI BufferedImage to use to make the Image object
   * @return returns an Image object based from the BufferedImage
   * @throws ImageException This happens if there is something wrong with the BufferedImage
   */
  public static Image fromBufferedImage(BufferedImage BI) {
    Image img;
    if (BI.getType() == BufferedImage.TYPE_BYTE_GRAY) {
      img = Image.fromByteArray(MODE_L, BI.getWidth(), BI.getHeight(), Utils.bufferedImageToByteArray(BI));
    } else if(BI.getType() == BufferedImage.TYPE_4BYTE_ABGR) {
      img = Image.fromByteArray(MODE_RGBA, BI.getWidth(), BI.getHeight(), Utils.bufferedImageToByteArray(BI));
    } else if(BI.getType() == BufferedImage.TYPE_3BYTE_BGR) {
      img = Image.fromByteArray(MODE_RGBA, BI.getWidth(), BI.getHeight(), Utils.bufferedImageToByteArray(BI)).changeMode(MODE_RGB);
    } else {
      img = Image.fromByteArray(MODE_RGBA, BI.getWidth(), BI.getHeight(), Utils.bufferedImageToByteArray(BI));
    }
    return img;
  }

  /**
   * Take the current Image object and make a BufferedImage out of it.  This is always of TYPE_INT_ARGB. 
   * @return BufferedImage
   * @throws ImageException
   */
  public BufferedImage toBufferedImage() {
    if(this.bpp == 8) {
      BufferedImage BB = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
      byte[] test = ((DataBufferByte) BB.getRaster().getDataBuffer()).getData();
      System.arraycopy(MAP, 0, test, 0, test.length);
      return BB;
    } else {
      BufferedImage BB = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
      int[] array = Utils.bytesToInts(Image.MODE_RGBA, this.changeMode(Image.MODE_RGBA).getArray());
      BB.setRGB(0, 0, this.getWidth(), this.getHeight(), array, 0, this.getWidth());
      return BB;
    }
  }

  
  /**
   * This takes ImageData from SWT and makes an Image Object.
   * @param data ImageData object to create an Image object from.
   * @return An Image Object based from the ImageData provided.
   * @throws ImageException
   */
  public static Image fromImageData(ImageData data) throws ImageException {
    PaletteData palette = data.palette;
    Image newImg;
    if (palette.isDirect) {
      //TODO: need to stop dropping the alpha channel on the floor here!!
      byte bpp = (byte)(data.depth);
      int width = data.width*data.height;
      int[] newInt = new int[width];
      data.getPixels(0, 0, width, newInt, 0);
      byte[] newBytes = Utils.intsToBytes(newInt, bpp);
      newImg = Image.fromByteArray(bpp, data.width, data.height, newBytes);
    } else {
      byte bpp = (byte)(32);
      newImg = Image.create(bpp, data.width, data.height);
      RGB[] rgbs = palette.getRGBs();
      byte[] red = new byte[rgbs.length];
      byte[] green = new byte[rgbs.length];
      byte[] blue = new byte[rgbs.length];
      for (int i=0; i<rgbs.length; i++) {
        red[i] = (byte)rgbs[i].red;
        green[i] = (byte)rgbs[i].green;
        blue[i] = (byte)rgbs[i].blue;
      }
      for(int y=0; y<data.height; y++) {
        for( int x = 0; x<data.width; x++) {
          int px = data.getPixel(x, y);
          int alpha = data.getAlpha(x, y);
          Color c = new Color(red[px], green[px], blue[px], (byte)alpha);
          newImg.setPixel(x, y, c);
        }
      }
    }
    return newImg;
  }
  /**
   * Create an SWT ImageData object based from the current Image object
   * @return ImageData object
   */
  public ImageData toImageData() {
    PaletteData palette = new PaletteData(0xFF0000, 0x00FF00, 0x0000FF);
    ImageData ID = new ImageData(this.getWidth(), this.getHeight(), 24, palette);
    Color c;
    int[] newInt;
    byte[] cBytes = new byte[3];
    for(int y=0; y<this.getHeight(); y++) {
      for(int x=0; x<this.getWidth(); x++) {
        c = this.getPixel(x, y);
        if( this.getColors() == 4) {
          cBytes[0] = c.getRed();
          cBytes[1] = c.getGreen();
          cBytes[2] = c.getBlue();
          newInt = Utils.bytesToInts(Image.MODE_RGB, cBytes);
          ID.setPixel(x, y, newInt[0]);
          ID.setAlpha(x, y, c.getAlpha() &0xff );
        } else if (this.getColors() == 3){
          cBytes[0] = c.getRed();
          cBytes[1] = c.getGreen();
          cBytes[2] = c.getBlue();
          newInt = Utils.bytesToInts(Image.MODE_RGB, cBytes);
          ID.setPixel(x, y, newInt[0]);
        } else {
          cBytes[0] = c.getGrey();
          cBytes[1] = c.getGrey();
          cBytes[2] = c.getGrey();
          newInt = Utils.bytesToInts(Image.MODE_RGB, cBytes);
          ID.setPixel(x, y, newInt[0]);
        }
      }
    }
    return ID;
  }

  /**
   * Change the MODE of the current Image. Use the static MODE_ types
   * MODE_RGBA = 4 byte Image with an alpha channel
   * MODE_RGB = 3 byte image
   * MODE_L = 1 byte Image (black and white)
   * 
   * @param MODE Sets the Image.MODE_ to change to 
   * @return Returns a new Image Object in that mode (Caution current Image object should be discarded
   * as changes to it could effect the new Image Object 
   * @throws ImageException
   */
  public Image changeMode(byte MODE) {
    if (MODE == this.bpp) {
      return this;
    } 
    Image image = Image.create(MODE, width, height);      
    if (MODE == 8) {
      int avg;
      for (int x = 0; x < image.MAP.length; x++){
        int pos = x*this.colors; 
        avg = ((MAP[pos]&0xff) + (MAP[pos+1]&0xff) + (MAP[pos+2]&0xff))/3;
        image.MAP[x] = (byte) avg;
      }
      return image;
    }
    
    if (MODE == 24 && this.bpp == 32) {
      for(int i=0; i< image.MAP.length/3; i++) {
        int npos = i*3;
        int opos = i*4;
        image.MAP[npos] = MAP[opos];
        image.MAP[npos+1] = MAP[opos+1];
        image.MAP[npos+2] = MAP[opos+2];
      }
    } else if (MODE == 24 && this.bpp == 8) {
      for(int i=0; i<MAP.length; i++) {
        int pos = i*3;
        image.MAP[pos] = MAP[i];
        image.MAP[pos+1] = MAP[i];
        image.MAP[pos+2] = MAP[i];
      }
    } else if (MODE == 32 && this.bpp == 8) {
      for(int i=0; i<MAP.length; i++) {
        int pos = i*4;
        image.MAP[pos] = MAP[i];
        image.MAP[pos+1] = MAP[i];
        image.MAP[pos+2] = MAP[i];
        image.MAP[pos+3] = (byte)255;
      }
    } else if (MODE == 32 && this.bpp == 24) {
      for(int i=0; i<(MAP.length/3); i++) {
        int npos = i*4;
        int opos = i*3;
        image.MAP[npos] = MAP[opos];
        image.MAP[npos+1] = MAP[opos+1];
        image.MAP[npos+2] = MAP[opos+2];
        image.MAP[npos+3] = (byte)255;
      }
    }
    return image;
  }
  
  /**
   * This resizes the Image keeping its aspect then adds a border to it if it is not the set width/height
   * @param bWidth new Width
   * @param bHeight new Height
   * @param borderColor new Height
   * @return new Image object of the given size
   */
  public Image resizeWithBorders(int bWidth, int bHeight, Color borderColor, ScaleType st) {
    Image ib = Image.create(this.bpp, bWidth, bHeight, borderColor);
    
    Image newI = resize(bWidth, bHeight, true, st);
    
    if(newI.getHeight() == ib.getHeight()) {
      int pos = (ib.getWidth()/2) - (newI.getWidth()/2);
      ib.paste(pos, 0, newI);
    } else {
      int pos = (ib.getHeight()/2)  - (newI.getHeight()/2);
      ib.paste(0, pos, newI);
    }
    
    return ib;
  }
  
  /**
   * This resizes the Image, uses the Nearest Neighbor scaler, and keeps aspect ratio
   * @param width new Width
   * @param height new Height
   * @return new Image object of the given size
   */
  public Image resize(int width, int height) {
    return resize(width, height, true, ScaleType.NN);
  }
  
  /**
   * This resizes the Image, uses the Nearest Neighbor scaler, and keeps aspect ratio
   * @param width new Width
   * @param height new Height
   * @param keepAspect boolean, true means keep aspect, false means dont keep the aspect
   * @return new Image object of the given size
   */
  public Image resize(int width, int height, boolean keepAspect) {
    return resize(width, height, keepAspect, ScaleType.NN);
  }
  
  /**
   * This resizes the Image
   * @param newWidth new Width
   * @param newHeight new Height
   * @param keepAspect boolean, true means keep aspect, false means dont keep the aspect
   * @param st ScaleType to use (see Image.ScaleTypes)
   * @return new Image object of the given size
   */
  public Image resize(int newWidth, int newHeight, boolean keepAspect, ScaleType st) {
    if(keepAspect) {
      int[] aspect = Utils.getAspectSize(this.width, this.height, newWidth, newHeight);
      newWidth = aspect[0];
      newHeight = aspect[1];
    }
    Image tmp;
    switch(st) {
    case LINER:
      tmp = BiLinearScaler.scale(this, newWidth, newHeight);
      break;
    case CUBIC:
      tmp = BiCubicScaler.scale(this, newWidth, newHeight);
      break;
    case AWT_NN:
      tmp = Image.fromByteArray(this.bpp, newWidth, newHeight, Utils.awtResizeNN(this, newWidth, newHeight));
      break;
    case AWT_LINER:
      tmp = Image.fromByteArray(this.bpp, newWidth, newHeight,Utils.awtResizeLiner(this, newWidth, newHeight));
      break;
    case AWT_CUBIC:
      tmp = Image.fromByteArray(this.bpp, newWidth, newHeight,Utils.awtResizeBiCubic(this, newWidth, newHeight));
      break;
    default:
      tmp = NearestNeighborScaler.scale(this, newWidth, newHeight);
    }
    return tmp;
  }
  
  /**
   * Fill current Image with this color
   * @param c
   */
  public void fillColor(Color c) {
    if (this.bpp == 8){
      Arrays.fill(MAP, c.getGrey());
    } else if (this.bpp >= 24){
      for(int i=0; i<MAP.length/this.colors; i++) {
        int pos = i*this.colors;
        MAP[pos] = c.getRed();
        MAP[pos+1] = c.getGreen();
        MAP[pos+2] = c.getBlue();
        if (this.colors == 4){
          MAP[pos+3] = c.getAlpha();
        }
      }


    } 
  }
  
  /**
   * Set a pixel in this image to a given Color
   * 
   * @param x X position of the pixel
   * @param y Y position of the pixel
   * @param c Color to set the pixel to (see Image.Color)
   */
  public void setPixel(int x, int y, Color c) {
    if(x<0 || x>=this.width) {
      return;
    }
    if(y<0 || y>=this.height) {
      return;
    }
    int pos = ((y*this.width)+x)*(this.colors); 
    if( this.bpp == 8) {
      MAP[pos] = c.getGrey();
    } else if (this.bpp >= 24) {
      MAP[pos] = c.getRed();
      MAP[pos+1] = c.getGreen();
      MAP[pos+2] = c.getBlue();
      if(this.bpp == 32) {
        MAP[pos+3] = c.getAlpha();
      }
    }
  }
  
  public void mergePixel(int x, int y, Color c) {
    Color cc = this.getPixel(x, y);
    cc.merge(c);
    setPixel(x, y, cc);
  }

  
  public void setPixelInChannel(int x, int y, byte c, byte p) {
    int POS = ((y*this.getWidth())+x)*(this.colors)+c;
    MAP[POS] = p;
  }
  
  /**
   * The a color for a given pixel
   * @param x X position of the pixel
   * @param y Y position of the pixel
   * @return Color object of that pixel
   */
  public Color getPixel(int x, int y) {
    if(x < 0 || x >= width || y < 0 || y >= height) {
      return null;
    }
    int POS = ((y*this.getWidth())+x)*(this.colors);
    if (this.getBPP() == 32) {
      return new Color(MAP[POS], MAP[POS+1], MAP[POS+2], MAP[POS+3]);
    } else if (this.getBPP() == 24) {
      return new Color(MAP[POS], MAP[POS+1], MAP[POS+2]);
    } else {
      return new Color(MAP[POS]);
    }
  }
  
  public byte getByteInChannel(int x, int y, byte c) {
    int POS = ((y*this.getWidth())+x)*(this.colors)+c;
    return MAP[POS];
  }
  
  /**
   * Paste the given Image object onto this Image
   * If the given Image is taller or wider then this Image we only merge the visible bits onto this Image
   *  
   * @param x X position to start the merge
   * @param y Y position to start the merge
   * @param img Image object to merge
   * @throws ImageException
   */
  public void paste(int x, int y, Image img) {
    paste(x, y, img, false);
  }
  
  /**
   * Paste the given Image object onto this Image
   * If the given Image is taller or wider then this Image we only merge the visible bits onto this Image.
   * If alphaMerge == true and the img has an alpha channel we will use that as a mask on how to merge the images.  
   * @param x X position to start the merge
   * @param y Y position to start the merge
   * @param img Image object to merge
   * @param alphaMerge should we do a mask type merge on any alpha channel?
   * @throws ImageException
   */
  public void paste(int x, int y, Image img, boolean alphaMerge){
    if (img.height+y < 0 || y >= this.height) {
      return;
    }
    
    if (img.width+x < 0 || x >= this.width) {
      return;
    }
    if (! alphaMerge && img.getBPP() != this.getBPP()) {
      img = img.changeMode(this.getBPP());
    }

    int imgXOff = 0;
    int imgYOff = 0;
    if(x < 0) {
      imgXOff = Math.abs(x);
      x=0;
    }
    if(y < 0) {
      imgYOff = Math.abs(y);
      y=0;
    }
    
    int thisLineWidth = this.width * this.colors;
    int imgLineWidth = img.width * img.colors;
    int imgXOffBytes = imgXOff * img.colors;
    int XBytes = x*this.colors;
    for(int h=y; h < this.height; h++) {
      if(h-y+imgYOff >= img.height) {
        break;
      }
      int thisStart = thisLineWidth*h;
      int imgStart = imgLineWidth*(h-y+imgYOff);
      if(! alphaMerge) {
        System.arraycopy(img.MAP, imgStart+(imgXOffBytes), this.MAP, thisStart+(XBytes), Math.min(imgLineWidth-(imgXOffBytes), thisLineWidth-(XBytes)));
      } else {
        for(int w = 0; w < Math.min(imgLineWidth-(imgXOffBytes), thisLineWidth-(XBytes)); w+=4) {
          if(img.MAP[imgStart+w+imgXOffBytes+3] == 0) {
            continue;
          } else if (this.colors == 4 && this.MAP[thisStart+w+3] == 0) {
            System.arraycopy(img.MAP, imgStart+w+imgXOffBytes, this.MAP, thisStart+w+(XBytes), this.colors);
          } else if (img.MAP[imgStart+imgXOffBytes+3] == 255) {
            System.arraycopy(img.MAP, imgStart+w+imgXOffBytes, this.MAP, thisStart+w+(XBytes), this.colors);
          } else {
            Color c = img.getPixel((w+imgXOffBytes)/img.colors, h-y+imgYOff);
            Color c2 = this.getPixel((w/this.colors)+x, h);
            c2.merge(c);
            this.setPixel((w/this.colors)+x, h, c2);
          }
        }
      }
    }
  }

  public Image copy() {
    Image newImage = Image.create(this.bpp, width, height);
    System.arraycopy(MAP, 0, newImage.MAP, 0, MAP.length);
    return newImage;
  }
  
  public Image cut(int x, int y, int width, int height) {
    Image newImage = Image.create(this.bpp, width, height);
    
      for(int yy = 0; yy< height; yy++) {
        int startPos = (((y+yy)*this.width)+x)*(this.colors);
        System.arraycopy(this.MAP, startPos, newImage.MAP, (yy*width*(newImage.colors)), width*(newImage.colors));
      }
    return newImage;
  }
  
  /**
   * Sets this Image to random Data
   */
  public void mkRandom() {
    Random r = new Random();
    r.nextBytes(MAP);
  }
  
  protected void setArray(byte[] array){
    this.MAP= array;
  }
  
  /**
   * This gives the backing byte array for the image.  Modifying it will modify the image. 
   * @return byte[] of the raw Image data
   */
  public byte[] getArray() {
    return MAP;
  }
  
  /**
   * Get the number of bitsPerPixel, this is the same as the Image.MODE_ of the Image
   * @return byte (8, 24, 32)
   */
  public byte getBPP(){
    return this.bpp;
  }  
  
  /**
   * Returns the number color channels in this Image (BPP/8)
   * @return byte (1, 3, or 4)
   */
  public byte getColors(){
    return this.colors;
  }  
  
  /**
   * Returns the width of this Image
   * @return Image Width (int)
   */
      
  public int getWidth(){
    return this.width;
  }
  
  /**
   * Returns the height of this Image
   * @return Image Height (int)
   */  
  public int getHeight(){
    return this.height;
  }

  //TODO: need to find image type by byte inspection!!
  private static ImageType getImageType(String filename) throws ImageException {
    String ext = filename.substring(filename.lastIndexOf('.')+1).toLowerCase();
    if (ext.equals("tiff") || ext.equals("tif")) {
      return ImageType.TIFF;
    } else if (ext.equals("png")) {
      return ImageType.PNG;
    } else if (ext.equals("jpg") || ext.equals("jpeg")){
      return ImageType.JPEG;
    }
    throw new ImageException("Could not determen file type");
  }



  public static class ImageException extends Exception {
    private static final long serialVersionUID = 713250734097347352L;
    public ImageException() {
      super();
    }
    public ImageException(String string) {
      super(string);
    }
  }
  
}
