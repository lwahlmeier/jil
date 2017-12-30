package me.lcw.jil;

import java.io.File;
import java.io.IOException;


/**
 * The generic image interface.
 *   
 * NOTE: implementers *MUST* implements the fromJilImage static method.
 */
public interface BaseImage {
  
  public static enum ImageMode {
    GREY8(8, 1), RGB24(24, 3), RGBA32(32, 4), GREYHQ(64, 1), RGBHQ(64*3, 3), RGBAHQ(64*4, 4);
  
    private final int bitsPerPixel;
    private final int colors;
        
    ImageMode(int bpp, int colors) {
      this.bitsPerPixel = bpp;
      this.colors = colors;
    }
    
    public int getBPP() {
      return this.bitsPerPixel;
    }
    
    public int getColors() {
      return this.colors;
    }
  };
  
  /**
   * Image Types for Image Object to use (open/save)
   * 
   */
  public static enum ImageType {TIFF, JPEG, PNG};
  /**
   * ScaleTypes that can be used when resizing an Image.
   * NN      - Nearest Neighbor - Very fast but kind somewhat noticeable scaler (Default).
   * LINER   - This is BiLiner, its very fast and descent quality.
   * CUBIC   - This is BiCubic, its looks very good in most situations but is a little slower.
   * CUBIC_SMOOTH - This is only for downscaling it has the highest Quality but is pretty slow. 
   *
   */
  public static enum ScaleType {NN, LINER, CUBIC, CUBIC_SMOOTH};
  
  
  /**
   * Save the image to the given file name.  We determine the type based on the file extension (required).
   * 
   * @param filename Path/Name of the file to save
   * @throws IOException This happens if we can not save/open that file
   * @throws ImageException This happens if we can not figure out the type you want use to save as 
   */
  public void save(String filename) throws IOException;
  
  /**
   * Save the image to the given file name.  We determine the type based on the file extension (required).
   *  
   * @param file Location for the file to be written out to
   * @throws IOException This happens if we can not save/open that file
   * @throws ImageException This happens if we can not figure out the type you want use to save as 
   */
  public void save(File file) throws IOException;
  
  /**
   * Save the image for a given location with the provided type.
   * 
   * @param file Location for the file to be written out to
   * @param type Type of file to open used Image.ImageType.(TIFF, PNG, JPEG)
   * @throws IOException This happens if we can not save/open that file
   * @throws ImageException This happens if we can not figure out the type you want use to save as 
   */
  public void save(File file, ImageType type) throws IOException;
  
  /**
   * Save the image to the given file name.
   * 
   * @param filename Path/Name of the file to save
   * @param type Type of file to open used Image.ImageType.(TIFF, PNG, JPEG)
   * @throws IOException This happens if we can not save/open that file
   * @throws ImageException This happens if we can not figure out the type you want use to save as 
   */
  public void save(String filename, ImageType type) throws IOException;
  
  /**
   * Converts the current Image type into a JilImage. 
   * 
   * @return a JilImage of this current image.
   */
  public JilImage toJilImage();
//TODO:  public JilHQImage toJilHQImage();
  
  /**
   * Change the MODE of the current Image. Use the static MODE_ types.
   * 
   * MODE_RGBA = 4 byte Image with an alpha channel
   * MODE_RGB = 3 byte image
   * MODE_L = 1 byte Image (black and white)
   * 
   * @param mode Sets the Image.MODE_ to change to 
   * @return Returns a new Image Object in that mode (Caution current Image object should be discarded
   * as changes to it could effect the new Image Object 
   */
  public BaseImage changeMode(ImageMode mode);
  
  /**
   * This resizes the Image keeping its aspect then adds a border to it if it is not the set width/height.
   * 
   * @param bWidth new Width.
   * @param bHeight new Height.
   * @param borderColor new Height.
   * @param scaleType the type of scaling to do.
   * @return new Image object of the given size
   */
  public BaseImage resizeWithBorders(int bWidth, int bHeight, Color borderColor, ScaleType scaleType);
  
  /**
   * This resizes the Image.
   * 
   * @param newWidth new Width
   * @param newHeight new Height
   * @param keepAspect boolean, true means keep aspect, false means dont keep the aspect
   * @param scaleType ScaleType to use (see Image.ScaleTypes)
   * @return new Image object of the given size
   */
  public BaseImage resize(int newWidth, int newHeight, boolean keepAspect, ScaleType scaleType);
  
  /**
   * Fill current Image with this color
   * @param color color to set the image to.
   */
  public void fillImageWithColor(Color color);
  
  
  /**
   * Set a pixel in this image to a given Color
   * 
   * @param x X position of the pixel
   * @param y Y position of the pixel
   * @param c Color to set the pixel to (see Image.Color)
   */
  public void setPixel(int x, int y, Color c);
  
  /**
   * The a color for a given pixel.
   * 
   * @param x X position of the pixel
   * @param y Y position of the pixel
   * @return Color object of that pixel
   */
  public Color getPixel(int x, int y);
  
  /**
   * Paste the given Image object onto this Image.
   * 
   * If the given Image is taller or wider then this Image we only merge the visible bits onto this Image.
   *  
   * @param x X position to start the merge
   * @param y Y position to start the merge
   * @param img Image object to merge
   */
  public void paste(int x, int y, BaseImage img);
  
  /**
   * Merge the given Image object onto this Image.
   * 
   * If the given Image is taller or wider then this Image we only merge the visible bits onto this Image.
   * 
   * @param x X position to start the merge
   * @param y Y position to start the merge
   * @param img Image object to merge
   */
  public void merge(int x, int y, BaseImage img);
  
  /**
   * Copies the image.  This image object will be unique from the original.
   * 
   * @return the new Image object.
   */
  public BaseImage copy();
  
  /**
   * Cuts a section of the image to a new image.  Modifications to the new image will not apply to the old one.
   * 
   * @param x the x position to start at
   * @param y the y position to start at
   * @param width the width of the cut
   * @param height the height of the cut
   * @return the new Image
   */
  public BaseImage cut(int x, int y, int width, int height);
  
  /**
   * This gives the backing byte array for the image.  Modifying it will modify the image.
   *  
   * @return byte[] of the raw Image data
   */
  public byte[] getByteArray();
  
  /**
   * Get the number of bitsPerPixel, this is the same as the Image.MODE_ of the Image.
   * 
   * @return byte (8, 24, 32)
   */
  public ImageMode getMode();
  
  /**
   * Returns the number color channels in this Image (BPP/8).
   * 
   * @return byte (1, 3, or 4).
   */
  public int getColors();
  
  
  /**
   * Returns the width of this Image.
   * 
   * @return The image width.
   */
  public int getWidth();
  
  /**
   * Returns the height of this Image.
   * 
   * @return The image height.
   */  
  public int getHeight();
  
  /**
   * Gets the Draw Object for this image.
   * 
   * @return new Draw object for this image.
   */
  public Draw draw();
  
}
