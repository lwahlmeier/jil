package org.java_image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;


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
  
  private final byte[][] MAP;
  private int width;
  private int height;
  private byte bpp;
  private byte colors;
  
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

  public enum ScaleType {NN, LINER, CUBIC, LANCZOS};
  
  private Image(byte mode, int width, int height) {
    
    colors = (byte) (mode/8);
    MAP = new byte[colors][];
    for(int i = 0; i<colors; i++){
      MAP[i] = new byte[width*height];
      Arrays.fill(MAP[i], (byte)(0));
    }
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
   * Create an Image object from a byte Array.  The byte array must be a single array
   * of bytes representing the mode given (L, RGB, or RGBA)
   * @param mode Image mode, uses the static bytes Image.MODE_(L, RGB, RGBA)
   * @param width How wide the image should be in pixels
   * @param height How high the Image should be in pixels
   * @param data byte[] to use to loading the data
   * @return Returns an Image object with the provided byte[] set in it
   * @throws ImageException This happens if the data provided is to large or to small for the (mode/8)*width*height
   */
  public static Image fromByteArray(byte mode, int width, int height, byte[] data) throws ImageException {
    
    byte cBytes = (byte)(mode/8);
    if(data.length != (width*height*cBytes)){
      throw new ImageException("Incorrect number of bytes to make an image of that type");
    }
    
    byte[][] map = new byte[cBytes][];
    for(int i = 0; i<cBytes; i++){
      map[i] = new byte[width*height];
      for(int q=0; q<map[i].length; q++){
        System.arraycopy(data, ((q*cBytes)+i), map[i], q, 1);
      }
    }
    
    Image image = create(mode, width, height);
    for (byte i = 0; i<cBytes; i++) {
      image.setChannel(i, map[i]);
    }
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
    ImageType type = getImageType(filename);
    return open(filename, type);
  }

  /**
   * Static Method that allows you to open a file, just pass in the path/filename. 
   * @param filename  Filename to attempt to open.
   * @param type Type of file to open used Image.ImageType.(TIFF, PNG, JPEG)
   * @return Returns an Image object from the provided file.
   * @throws ImageException This can happen if we do not know the type of file we where asked to open.
   * @throws IOException This happens when we can not access the file.
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
   * @param filename Path/Name of the file to save
   * @throws IOException This happens if we can not save/open that file
   * @throws ImageException This happens if we can not figure out the type you want use to save as 
   */
  public void save(String filename) throws IOException, ImageException {
    ImageType type = getImageType(filename);
    save(filename, type);
  }
  
  /**
   * Save the image to the given file name.  We determine the type based on the file extension (required) 
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
  public static Image fromBufferedImage(BufferedImage BI) throws ImageException{
    Image img = Image.fromByteArray(MODE_RGBA, BI.getWidth(), BI.getHeight(), 
        intsToBytes(BI.getRGB(0, 0, BI.getWidth(), BI.getHeight(), null , 0, BI.getWidth()), (byte) 32));
    return img;
  }
  //This always up changes to RGBA image
  /**
   * Take the current Image object and make a BufferedImage out of it.  This is always of TYPE_INT_ARGB. 
   * @return BufferedImage
   * @throws ImageException
   */
  public BufferedImage toBufferedImage() throws ImageException {
    BufferedImage BB = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
    int[] array = bytesToInts(Image.MODE_RGBA, this.changeMode(Image.MODE_RGBA).toArray());
    BB.setRGB(0, 0, this.getWidth(), this.getHeight(), array, 0, this.getWidth());
    return BB;
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
      byte[] newBytes = intsToBytes(newInt, bpp);
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
        if( this.getChannels() == 4) {
          cBytes[0] = c.getRed();
          cBytes[1] = c.getGreen();
          cBytes[2] = c.getBlue();
          newInt = bytesToInts(Image.MODE_RGB, cBytes);
          ID.setPixel(x, y, newInt[0]);
          ID.setAlpha(x, y, c.getAlpha() &0xff );
        } else if (this.getChannels() == 3){
          cBytes[0] = c.getRed();
          cBytes[1] = c.getGreen();
          cBytes[2] = c.getBlue();
          newInt = bytesToInts(Image.MODE_RGB, cBytes);
          ID.setPixel(x, y, newInt[0]);
        } else {
          cBytes[0] = c.getGrey();
          cBytes[1] = c.getGrey();
          cBytes[2] = c.getGrey();
          newInt = bytesToInts(Image.MODE_RGB, cBytes);
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
  public Image changeMode(byte MODE) throws ImageException {
    if (MODE == this.getBPP()) {
      return this;
    } 
    Image image = Image.create(MODE, width, height);  
    if (MODE == 24 && this.bpp == 32) {
      /*simple drop alpha channel*/
      image.setChannel((byte)0, this.MAP[0]);
      image.setChannel((byte)1, this.MAP[1]);
      image.setChannel((byte)2, this.MAP[2]);
    } else if (MODE == 8) {
      byte[] data = new byte[this.width*this.height];
      int avg;
      for (int x = 0; x < data.length; x++){
        avg = ((MAP[0][x]&0xff) +(MAP[1][x]&0xff)+(MAP[2][x]&0xff))/3;
        data[x] = (byte) avg;
      }
      image.setChannel((byte) 0, data);
    } else if (MODE == 24 && this.bpp == 8) {
      image.setChannel((byte)0, this.MAP[0]);
      image.setChannel((byte)1, this.MAP[0]);
      image.setChannel((byte)2, this.MAP[0]);

    } else if (MODE == 32 && this.bpp == 8) {
      image.setChannel((byte)0, this.MAP[0]);
      image.setChannel((byte)1, this.MAP[0]);
      image.setChannel((byte)2, this.MAP[0]);
      byte[] alpha = new byte[this.getWidth()*this.getHeight()];
      Arrays.fill(alpha, (byte)255);
      image.setChannel((byte)3, alpha);
      image.setChannel((byte)3, alpha);
    } else if (MODE == 32 && this.bpp == 24) {
      image.setChannel((byte)0, this.MAP[0]);
      image.setChannel((byte)1, this.MAP[1]);
      image.setChannel((byte)2, this.MAP[2]);
      byte[] alpha = new byte[this.getWidth()*this.getHeight()];
      Arrays.fill(alpha, (byte)255);
      image.setChannel((byte)3, alpha);
    } else {
      throw new ImageException("Could not Convert mode!! "+MODE+":"+this.getBPP());
    }
    return image;
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
   * @param width new Width
   * @param height new Height
   * @param keepAspect boolean, true means keep aspect, false means dont keep the aspect
   * @param st ScaleType to use (see Image.ScaleTypes)
   * @return new Image object of the given size
   */
  public Image resize(int width, int height, boolean keepAspect, ScaleType st) {
    if(keepAspect) {
      int nw = this.getWidth();
      int nh = this.getHeight();
      double ratio = nw/(double)nh;
      if (nw != width) {
        nw = width;
        nh = (int)Math.floor(nw/ratio);
      }
      if (nh > height) {
        nh = height;
        nw = (int)Math.floor(nh*ratio);
      }
      width = nw;
      height = nh;
    }
    
    Image tmp = Image.create(this.bpp, width, height);
    switch(st) {
    case LINER:
      tmp = BilinearScaler(width, height);
      break;
    default:
      tmp = nearestNeighborScaler(width, height);
    }
    return tmp;
  }

  
  private Image BilinearScaler(int newWidth, int newHeight){
    Image newImage = Image.create(this.getBPP(), newWidth, newHeight);
    float x_ratio = ((float)(this.getWidth()-1))/newWidth ;
    float y_ratio = ((float)(this.getHeight()-1))/newHeight ;
    float x_diff, y_diff;
    int px, py, idx;
    int[] r = new int[4];
    int[] b = new int[4];
    int[] g = new int[4];
    int[] a = new int[4];

    for (int y=0; y < newHeight; y++){
      for (int x=0; x < newWidth; x++){
        px = (int)(x_ratio * x);
        py = (int)(y_ratio * y);
        x_diff = (x_ratio * x) - px;
        y_diff = (y_ratio * y) - py;
        idx = (py*this.getWidth()) + px;
        for(int c = 0; c < this.getChannels(); c++) {
          r[0] = this.MAP[c][idx] & 0xff;
          r[1] = this.MAP[c][idx+1] & 0xff;
          r[2] = this.MAP[c][idx+this.getWidth()] & 0xff;
          r[3] = this.MAP[c][idx+this.getWidth()+1] & 0xff;
          newImage.MAP[c][(y*newWidth)+x] = (byte) (
            r[0]*(1-x_diff)*(1-y_diff) +  
            r[1]*(x_diff)  *(1-y_diff) +
            r[2]*(y_diff)  *(1-x_diff) +
            r[3]*(x_diff)  *(y_diff) );
        }
      }
    }
    return newImage;
  }
  
  private Image nearestNeighborScaler(int newWidth, int newHeight){
    Image newImage = Image.create(this.getBPP(), newWidth, newHeight);
    double x_ratio = this.getWidth()/(double)newWidth;
    double y_ratio = this.getHeight()/(double)newHeight;
    int px = 0;
    int py = 0;
    for (int y=0; y<newHeight; y++) {
      for(int x=0; x<newWidth; x++) {
        px = (int)Math.floor(x*x_ratio);
        py = (int)Math.floor(y*y_ratio);
        for(int c=0; c<(this.getBPP()/8); c++) {
          newImage.setPixel(x, y, this.getPixel(px, py));
        }
      }
    }
    return newImage;
  }
  
  /**
   * Fill current Image with this color
   * @param c
   */
  public void fillColor(Color c) {
    if (MAP.length == 1){
      Arrays.fill(MAP[0], c.getGrey());
    } else if (MAP.length == 3){
      Arrays.fill(MAP[0], c.getRed());
      Arrays.fill(MAP[1], c.getBlue());
      Arrays.fill(MAP[2], c.getGreen());
    } else if (MAP.length == 4){
      Arrays.fill(MAP[0], c.getRed());
      Arrays.fill(MAP[1], c.getBlue());
      Arrays.fill(MAP[2], c.getGreen());
      Arrays.fill(MAP[3], c.getAlpha());
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
    int p = ((y*this.getWidth())+x);
    if( this.getBPP() == 32) {
      MAP[0][p] = c.getRed();
      MAP[1][p] = c.getGreen();
      MAP[2][p] = c.getBlue();
      MAP[3][p] = c.getAlpha();
    } else if (this.getBPP() == 24) {
      MAP[0][p] = c.getRed();
      MAP[1][p] = c.getGreen();
      MAP[2][p] = c.getBlue();
    } else {
      MAP[0][p] = c.getGrey();
    }
  }
  
  /**
   * The a color for a given pixel
   * @param x X position of the pixel
   * @param y Y position of the pixel
   * @return Color object of that pixel
   */
  public Color getPixel(int x, int y) {
    int POS = ((y*this.getWidth())+x);
    if (this.getBPP() == 32) {
      return new Color(MAP[0][POS], MAP[1][POS], MAP[2][POS], MAP[3][POS]);
    } else if (this.getBPP() == 24) {
      return new Color(MAP[0][POS], MAP[1][POS], MAP[2][POS]);
    } else {
      return new Color(MAP[0][POS]);
    }
  }

  /**
   * Merges the given Image object onto the Image
   * If the given Image is taller or wider then this Image we only merge the visable bits onto this Image 
   * @param x X position to start the merge
   * @param y Y position to start the merge
   * @param img Image object to merge
   * @throws ImageException
   */
  public void mergeImages(int x, int y, Image img) throws ImageException {
    if (img.getBPP() != this.getBPP()) {
      img = img.changeMode(this.getBPP());
    }
    int maxW = img.getWidth();
    int maxH = img.getHeight();
    if (this.getWidth() - x < maxW) {
      maxW = this.getWidth() - x;
    }
    if (this.getHeight() - y < maxH) {
      maxH = this.getHeight() - y;
    }
    for(int h = 0; h<maxH; h++) {
      for(byte c=0; c< this.MAP.length; c++) {
        System.arraycopy(img.getChannel(c), h*img.getWidth(), MAP[c], x+((y+h)*this.getWidth()), maxW);
      }
    }
  }
  
  /**
   * Sets this Image to random Data
   */
  public void mkRandom() {
    Random r = new Random();
    for (int x = 0; x< this.MAP.length; x++){
      r.nextBytes(MAP[x]);
    }
  }
  
  
  private byte[] getChannel(byte channel) {
    return MAP[channel];
  }
  
  
  protected void setChannel(byte channel, byte[] array) throws ImageException {
    if (array.length != this.MAP[channel].length) {
      throw new ImageException("Must give array of correct length to set channel, Need:"+this.MAP[channel].length+" Got:"+array.length);
    }
    this.MAP[channel] = array;
  }
  
  /**
   * Outputs this image to a ByteArray.  This has to be constructed so should not be called unless it is needed 
   * @return byte[] of the raw Image data
   */
  public byte[] toArray() {
    byte[] data = new byte[width*height*(bpp/8)];  
    for (int i = 0; i< MAP[0].length; i++){
      for (int c = 0; c < MAP.length; c++){
        System.arraycopy(MAP[c], i, data, (c+(i*MAP.length)), 1);
      }
    }
    return data;
  }
  
  /**
   * Get the number of bitsPerPixel, this is the same as the Image.MODE_ of the Image
   * @return byte (8, 24, 32)
   */
  public byte getBPP(){
    return this.bpp;
  }  
  
  /**
   * Returns the number channels in this Image (BPP/8)
   * @return byte (1, 3, or 4)
   */
  public byte getChannels(){
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

  private static byte[] intsToBytes(int[] array, byte bpp) {
    byte cp = (byte) (bpp/8);
    byte[] nArray = new byte[array.length*cp];
    for (int i =0; i< array.length; i++) {
      int c = i*cp;
      nArray[c] = (byte) ((array[i] >> 16) & 0xff);
      nArray[c+1] = (byte) ((array[i] >> 8) & 0xff);
      nArray[c+2] = (byte) (array[i] & 0xff);
      if (cp == 4) {
        nArray[c+3] = (byte) ((array[i] >> 24) & 0xff);
      }
    }
    return nArray;
  }
  
  private static int[] bytesToInts(byte mode, byte[] array) {
    int[] nArray = new int[array.length/(mode/8)];
    for (int i =0; i< nArray.length; i++) {
      int c = i*(mode/8);
      if (mode == 32 ) {
      nArray[i] =  ((array[c+3] << 24 ) & 0xff000000) | ((array[c] << 16) & 0xff0000) | 
          ((array[c+1] << 8) & 0xff00) | ((array[c+2]) & 0xff);
      //Dont hit these because we upsample to 32bit before we make BufferedImage
      } else if (mode == 24) {
        nArray[i] =  ((array[c] << 16)&0xff0000) | 
            ((array[c+1] << 8)&0xff00) | ((array[c+2])&0xff);        
      } else if (mode == 8) {
        nArray[i] =  (255<<24) | ((array[c] << 16)) | 
            ((array[c] << 8)) | ((array[c]));
      }
    }
    return nArray;
  }

  /**
   * This class is how colors are passed around to/from the Image Object
   * 
   * @author lcw - Luke Wahlmeier
   *
   */
  public static class Color {
    private byte green = 0;
    private byte red = 0;
    private byte blue = 0;
    private byte alpha = 0;
    
    /**
     * Construct the color with RGB values set
     * @param red
     * @param green
     * @param blue
     */
    public Color(byte red, byte green, byte blue) {
      setRGB((byte) red, (byte) green, (byte) blue);
    }
    /**
     * Construct the color with RGBA values set
     * @param red
     * @param green
     * @param blue
     * @param alpha
     */
    public Color(byte red, byte green, byte blue, byte alpha) {
      setRGBA((byte) red, (byte) green, (byte) blue, (byte)alpha);
    }
    
    /**
     * Construct the color with grey values set
     * @param grey
     */
    public Color(byte grey) {
      setL((byte) grey);
    }
    /**
     * Construct a Color object with no colors set (everything is 0)
     */
    public Color() {
    }
    
    /**
     * Set the blue value on this color object
     * @param b
     */
    public void setBlue(byte b) {
      blue = b;
    }
    public byte getBlue() {
      return blue;
    }
    
    /**
     * Set the Red value on this color object
     * @param r
     */
    public void setRed(byte r) {
      red = r;
    }
    public byte getRed() {
      return red;
    }
    
    /**
     * Set the green value on this color object
     * @param g
     */
    public void setGreen(byte g) {
      green = g;
    }
    public byte getGreen() {
      return green;
    }
    
    /**
     * Set the alpha value on this color object
     * @param a
     */
    public void setAlpha(byte a) {
      alpha = a;
    }
    public byte getAlpha() {
      return alpha;
    }
    /**
     * Set the RGB Value on this Color Object
     * @param r
     * @param g
     * @param b
     */
    public void setRGB(byte r, byte g, byte b) {
      red = r;
      green = g;
      blue = b;
      alpha = (byte)255;
    }
    /**
     * Set the RGBA Value on this Color Object
     * @param r
     * @param g
     * @param b
     * @param a
     */
    public void setRGBA(byte r, byte g, byte b, byte a) {
      red = r;
      green = g;
      blue = b;
      alpha = a;
    }
    
    /**
     * Set the Grey value on this Color Object
     * @param g
     */
    public void setGrey(byte g) {
      setL(g);
    }
    
    public byte getGrey() {
      return (byte) (((red&0xff)+(green&0xff)+(blue&0xff))/3);
    }
    /**
     * Set the grey value on this color object(overrides all rgb values)
     * @param g
     */
    public void setL(byte g) {
      red = g;
      green = g;
      blue = g;
      alpha = (byte)255;
    }
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
