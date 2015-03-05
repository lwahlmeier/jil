package org.java_lcw.jil;

import java.io.File;
import java.io.IOException;

public interface Image {
  
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
  public enum ScaleType {NN, LINER, CUBIC, CUBIC_SMOOTH};
  
  
  /**
   * Save the image to the given file name.  We determine the type based on the file extension (required) 
   * @param filename Path/Name of the file to save
   * @throws IOException This happens if we can not save/open that file
   * @throws ImageException This happens if we can not figure out the type you want use to save as 
   */
  public void save(String file) throws IOException, ImageException;
  
  /**
   * Save the image to the given file name.  We determine the type based on the file extension (required) 
   * @param file Location for the file to be written out to
   * @throws IOException This happens if we can not save/open that file
   * @throws ImageException This happens if we can not figure out the type you want use to save as 
   */
  public void save(File file) throws IOException, ImageException;
  
  /**
   * Save the image for a given location with the provided type.
   * @param file Location for the file to be written out to
   * @param type Type of file to open used Image.ImageType.(TIFF, PNG, JPEG)
   * @throws IOException This happens if we can not save/open that file
   * @throws ImageException This happens if we can not figure out the type you want use to save as 
   */
  public void save(File file, ImageType type) throws IOException, ImageException;
  
  /**
   * Save the image to the given file name.
   * @param filename Path/Name of the file to save
   * @param type Type of file to open used Image.ImageType.(TIFF, PNG, JPEG)
   * @throws IOException This happens if we can not save/open that file
   * @throws ImageException This happens if we can not figure out the type you want use to save as 
   */
  public void save(String filename, ImageType type) throws IOException, ImageException;
  
  public JilImage toJavaImage();
  
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
  public Image changeMode(byte MODE);
  
  /**
   * This resizes the Image keeping its aspect then adds a border to it if it is not the set width/height
   * @param bWidth new Width
   * @param bHeight new Height
   * @param borderColor new Height
   * @return new Image object of the given size
   */
  public Image resizeWithBorders(int bWidth, int bHeight, Color borderColor, ScaleType st);
  
  /**
   * This resizes the Image
   * @param newWidth new Width
   * @param newHeight new Height
   * @param keepAspect boolean, true means keep aspect, false means dont keep the aspect
   * @param st ScaleType to use (see Image.ScaleTypes)
   * @return new Image object of the given size
   */
  public Image resize(int newWidth, int newHeight, boolean keepAspect, ScaleType st);
  
  /**
   * Fill current Image with this color
   * @param c
   */
  public void fillImageWithColor(Color c);
  
  
  /**
   * Set a pixel in this image to a given Color
   * 
   * @param x X position of the pixel
   * @param y Y position of the pixel
   * @param c Color to set the pixel to (see Image.Color)
   */
  public void setPixel(int x, int y, Color c);
  
  /**
   * The a color for a given pixel
   * @param x X position of the pixel
   * @param y Y position of the pixel
   * @return Color object of that pixel
   */
  public Color getPixel(int x, int y);
  
  /**
   * Paste the given Image object onto this Image
   * If the given Image is taller or wider then this Image we only merge the visible bits onto this Image
   *  
   * @param x X position to start the merge
   * @param y Y position to start the merge
   * @param img Image object to merge
   * @throws ImageException
   */
  public void paste(int x, int y, Image img);
  
  /**
   * Merge the given Image object onto this Image
   * If the given Image is taller or wider then this Image we only merge the visible bits onto this Image.
   * If alphaMerge == true and the img has an alpha channel we will use that as a mask on how to merge the images.  
   * @param x X position to start the merge
   * @param y Y position to start the merge
   * @param img Image object to merge
   * @throws ImageException
   */
  public void merge(int x, int y, Image img);
  
  public Image copy();
  
  public Image cut(int x, int y, int width, int height);
  
  /**
   * This gives the backing byte array for the image.  Modifying it will modify the image. 
   * @return byte[] of the raw Image data
   */
  public byte[] getArray();
  
  /**
   * Get the number of bitsPerPixel, this is the same as the Image.MODE_ of the Image
   * @return byte (8, 24, 32)
   */
  public byte getBPP();
  
  /**
   * Returns the number color channels in this Image (BPP/8)
   * @return byte (1, 3, or 4)
   */
  public byte getColors();
  
  
  /**
   * Returns the width of this Image
   * @return Image Width (int)
   */
  public int getWidth();
  
  /**
   * Returns the height of this Image
   * @return Image Height (int)
   */  
  public int getHeight();
  
  public Draw getImageDrawer();
}
