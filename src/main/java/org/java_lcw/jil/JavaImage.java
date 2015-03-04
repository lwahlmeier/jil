package org.java_lcw.jil;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.java_lcw.jil.scalers.BiCubicScaler;
import org.java_lcw.jil.scalers.BiLinearScaler;
import org.java_lcw.jil.scalers.NearestNeighborScaler;


/**
 *
 * Main Image object used to construct new Image files.  
 * 
 * @author lcw - Luke Wahlmeier
 */
public class JavaImage implements Image {
  
  private final int width;
  private final int height;
  private final byte bpp;
  private final byte colors;
  protected byte[] MAP;
  
  private JavaImage(byte mode, int width, int height) {
    colors = (byte) (mode/8);
    int size = colors*width*height;
    MAP = new byte[size];
    this.width = width;
    this.height = height;
    this.bpp = mode;
  }
  
  private JavaImage(byte mode, int width, int height, byte[] map) {
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
  public static JavaImage create(byte mode, int width, int height) {
    return new JavaImage((byte)(mode), width, height);
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
  public static JavaImage create(byte mode, int width, int height, Color color) {
    JavaImage i = new JavaImage((byte)(mode), width, height);
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
   * @throws ImageException This happens if the data provided is to large or to small for the (mode/8)*width*height
   */
  public static JavaImage fromByteArray(byte mode, int width, int height, byte[] data) {
    
    byte cBytes = (byte)(mode/8);
    if(data.length != (width*height*cBytes)){
      throw new RuntimeException("Incorrect number of bytes to make an image of that type");
    }
    JavaImage image = new JavaImage(mode, width, height, data);
    return image;
  }
  
  /**
   * Static Method that allows you to open a file, just pass in the path/filename. 
   * @param filename  Filename to attempt to open.
   * @return Returns an Image object from the provided file.
   * @throws ImageException This can happen if we do not know the type of file we where asked to open.
   * @throws IOException This happens when we can not access the file.
   */
  public static JavaImage open(String filename) throws ImageException, IOException {
    try {
      return open(filename, Utils.getImageType(filename));
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
  public static JavaImage open(String filename, ImageType type) throws IOException, ImageException {
    JavaImage image;
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
   * Create an Image from a BufferedImage from AWT - The new Image will always be RGBA type
   * @param BI BufferedImage to use to make the Image object
   * @return returns an Image object based from the BufferedImage
   * @throws ImageException This happens if there is something wrong with the BufferedImage
   */
  public static JavaImage fromBufferedImage(BufferedImage BI) {
    JavaImage img;
    if (BI.getType() == BufferedImage.TYPE_BYTE_GRAY) {
      img = JavaImage.fromByteArray(MODE_L, BI.getWidth(), BI.getHeight(), Utils.bufferedImageToByteArray(BI));
    } else if(BI.getType() == BufferedImage.TYPE_4BYTE_ABGR) {
      img = JavaImage.fromByteArray(MODE_RGBA, BI.getWidth(), BI.getHeight(), Utils.bufferedImageToByteArray(BI));
    } else if(BI.getType() == BufferedImage.TYPE_3BYTE_BGR) {
      img = JavaImage.fromByteArray(MODE_RGBA, BI.getWidth(), BI.getHeight(), Utils.bufferedImageToByteArray(BI)).changeMode(MODE_RGB);
    } else {
      img = JavaImage.fromByteArray(MODE_RGBA, BI.getWidth(), BI.getHeight(), Utils.bufferedImageToByteArray(BI));
    }
    return img;
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
    ImageType type = Utils.getImageType(filename);
    save(filename, type);
  }
  
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
    } else if(this.bpp == 24) {
      BufferedImage BB = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
      //byte[] array = new byte[this.MAP.length];
      byte[] array = ((DataBufferByte) BB.getRaster().getDataBuffer()).getData();
      for(int i=0; i<array.length/3; i++) {
        int pos = i*3;
        array[pos] = this.MAP[pos+2];
        array[pos+1] = this.MAP[pos+1];
        array[pos+2] = this.MAP[pos];
      }
      return BB; 
    } else {
      BufferedImage BB = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
      //byte[] array = new byte[this.MAP.length];
      byte[] array = ((DataBufferByte) BB.getRaster().getDataBuffer()).getData();
      for(int i=0; i<array.length/4; i++) {
        int pos = i*4;
        array[pos] = this.MAP[pos+3];
        array[pos+1] = this.MAP[pos+2];
        array[pos+2] = this.MAP[pos+1];
        array[pos+3] = this.MAP[pos];
        
      }
      return BB;
    }
  }

  @Override
  public JavaImage changeMode(byte MODE) {
    if (MODE == this.bpp) {
      return this;
    } 
    JavaImage image = JavaImage.create(MODE, width, height);      
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
  
  @Override
  public JavaImage resizeWithBorders(int bWidth, int bHeight, Color borderColor, ScaleType st) {
    JavaImage ib = JavaImage.create(this.bpp, bWidth, bHeight, borderColor);
    
    JavaImage newI = resize(bWidth, bHeight, true, st);
    
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
  public JavaImage resize(int newWidth, int newHeight, boolean keepAspect, ScaleType st) {
    if(keepAspect) {
      int[] aspect = Utils.getAspectSize(this.width, this.height, newWidth, newHeight);
      newWidth = aspect[0];
      newHeight = aspect[1];
    }
    JavaImage tmp;
    switch(st) {
    case LINER:
      tmp = BiLinearScaler.scale(this, newWidth, newHeight);
      break;
    case CUBIC:
      tmp = BiCubicScaler.scale(this, newWidth, newHeight);
      break;
    case CUBIC_SMOOTH:
      tmp = (JavaImage)Utils.biCubicSmooth(this, newWidth, newHeight);
    default:
      tmp = NearestNeighborScaler.scale(this, newWidth, newHeight);
    }
    return tmp;
  }
  
  @Override
  public void fillImageWithColor(Color c) {
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
  
  @Override
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
  
  public void setPixelInChannel(int x, int y, byte c, byte p) {
    int POS = ((y*this.getWidth())+x)*(this.colors)+c;
    MAP[POS] = p;
  }
  
  @Override
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
  
  @Override
  public void paste(int x, int y, Image img) {
    if (img.getHeight()+y < 0 || y >= this.height) {
      return;
    }
    
    if (img.getWidth()+x < 0 || x >= this.width) {
      return;
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
    int imgLineWidth = img.getWidth() * img.getColors();
    int imgXOffBytes = imgXOff * img.getColors();
    int XBytes = x*this.colors;
    for(int h=y; h < this.height; h++) {
      int imgYPos = h-y+imgYOff;
      if( imgYPos >= img.getHeight()) {
        break;
      }
      int thisStart = thisLineWidth*h;
      int imgStart = imgLineWidth*(imgYPos);
      System.arraycopy(img.getArray(), imgStart+(imgXOffBytes), this.MAP, thisStart+(XBytes), Math.min(imgLineWidth-(imgXOffBytes), thisLineWidth-(XBytes)));
    }
  }
  
  @Override
  public void merge(int x, int y, Image img){
    if (img.getHeight()+y < 0 || y >= this.height) {
      return;
    }
    
    if (img.getWidth()+x < 0 || x >= this.width) {
      return;
    }
    if(this.getBPP() < 4 && img.getBPP() < 4) {
      paste(x, y, img);
      return;
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
    int imgLineWidth = img.getWidth() * img.getColors();
    int imgXOffBytes = imgXOff * img.getColors();
    int XBytes = x*this.colors;
    for(int h=y; h < this.height; h++) {
      int imgYPos = h-y+imgYOff;
      if( imgYPos >= img.getHeight()) {
        break;
      }
      int thisStart = thisLineWidth*h;
      int imgStart = imgLineWidth*(imgYPos);
      int maxWidth = Math.min((img.getWidth()-(imgXOff)), (this.width-x));
      for(int w = 0; w < maxWidth; w++) {
        int wImgBytes = w*img.getColors();
        int wThisBytes = w*this.colors;
        int imgXPos = imgStart+wImgBytes+imgXOffBytes;
        int thisXPos = thisStart+XBytes+wThisBytes;
        if(img.getColors() == 4 && img.getArray()[imgXPos+3] == 0) {
          continue;
        } else if (this.colors == 4 && this.MAP[thisXPos+3] == 0) {
          System.arraycopy(img.getArray(), imgXPos, this.MAP, thisXPos, this.colors);
          /*
            try{
              System.arraycopy(img.getArray(), imgXPos, this.MAP, thisXPos, this.colors);
            } catch (Exception e){
              System.out.println(img);
              System.out.println((w+x)+":"+h+":"+img.getWidth()+":"+img.getHeight()+":"+img.getBPP());
              System.out.println(img.getArray().length+":"+imgXPos+":"+this.MAP.length+":"+thisXPos+":"+this.colors);
            }*/

        } else if (img.getColors() == 4 && img.getArray()[imgXPos+3] == 255) {
          System.arraycopy(img.getArray(), imgXPos, this.MAP, thisXPos, this.colors);
        } else {
          Color c = img.getPixel((w+imgXOff), imgYPos);
          Color c2 = this.getPixel(w+x, h);
          c2.merge(c);
          this.setPixel(w+x, h, c2);
        }
      }
    }
  }
  
  
  @Override
  public JavaImage copy() {
    JavaImage newImage = JavaImage.create(this.bpp, width, height);
    System.arraycopy(MAP, 0, newImage.MAP, 0, MAP.length);
    return newImage;
  }
  
  @Override
  public JavaImage cut(int x, int y, int width, int height) {
    JavaImage newImage = JavaImage.create(this.bpp, width, height);
    
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
    this.MAP = array;
  }
  
  @Override
  public byte[] getArray() {
    return MAP;
  }
  
  @Override
  public byte getBPP(){
    return this.bpp;
  }  
  
  @Override
  public byte getColors(){
    return this.colors;
  }  
  
  @Override
  public int getWidth(){
    return this.width;
  }
  
  @Override
  public int getHeight(){
    return this.height;
  }
  
  @Override
  public JavaImage toJavaImage() {
    return this;
  }

  @Override
  public JavaDraw getImageDrawer() {
    return new JavaDraw(this);
  }
  
  public static class JavaDraw implements Draw {
    private static final int sigmaD = 2;
    private static final int sigmaR = 2;
    private static final double sigmaMax = Math.max(sigmaD, sigmaR);
    private static final int kernelRadius = (int)Math.ceil(2 * sigmaMax);
    private static final double twoSigmaRSquared = 2 * sigmaR * sigmaR;
    private static final int kernelSize = kernelRadius * 2 + 1;
    private static final int center = (kernelSize - 1) / 2;
    private static final double[][] kernelD = new double[kernelSize][kernelSize];
    private static final double gaussSimilarity[] = new double[256];
    static {

      for (int x = -center; x < -center + kernelSize; x++) {
        for (int y = -center; y < -center + kernelSize; y++) {
          kernelD[x + center][y + center] = Math.exp(-((x * x + y * y) / (2 * sigmaD * sigmaD)));
        }
      }


      for (int i = 0; i < 256; i++) {
        gaussSimilarity[i] = Math.exp(-((i) / twoSigmaRSquared));
      }
    }
    private final JavaImage ji;
    
    public JavaDraw(JavaImage ji) {
      this.ji = ji;
    }


    @Override
    public void drawRect(int x, int y, int w, int h, Color c, int lineWidth, boolean fill) {
      int maxW = x+w;
      int maxH = y+h;
      if (ji.getWidth() < maxW) {
        maxW = ji.getWidth() - x;
      }
      if (ji.getHeight() < maxH) {
        maxH = ji.getHeight() - y;
      }
      if(x < 0) {
        x=0;
      }
      if(y < 0) {
        y=0;
      }
      for(int H = y; H<maxH; H++) {
        for(int W = x;  W< maxW; W++) {
          if(W <= (y+lineWidth)-1 || H <= (y+lineWidth)-1 || H >= maxH-lineWidth || W >= maxW-lineWidth) {
            ji.setPixel(W, H, c);
          } 
        }
      }
      if(fill) {
        int tx = x+ w/2;
        int ty = y+ h/2;
        floodFill(tx, ty, c, c, false);
      }
    }
    
    public void floodFill(int x, int y, Color c) {
      floodFill( x, y, c, null, false);
    }
    
    public void floodFill(int x, int y, Color c, Color edge) {
      floodFill( x, y, c, edge, false);
    }

    public void floodFill(int x, int y, Color c, Color edge, boolean keepAlpha) {
      if(x < 0 || x >= ji.getWidth()) {
        return;
      }
      if(y <0 || y>=ji.getWidth()) {
        return;
      }
      Integer[] ce = new Integer[] {x, y};
      ArrayDeque<Integer[]> pl = new ArrayDeque<Integer[]>();
      pl.add(ce);
      if(edge == null) {
        Color OC = ji.getPixel(x, y);
        if(OC.equals(c)) {
          return;
        }
        while(pl.size() > 0) {
          ce = pl.poll();
          Color tmpC = ji.getPixel(ce[0], ce[1]);
          if(tmpC!=null && tmpC.equalsNoAlpha(OC)) {
            Color nc = c.copy();
            if(keepAlpha) {
              nc.setAlpha(tmpC.getAlpha());
            }
            ji.setPixel(ce[0], ce[1], nc);
            if(ce[0]+1 < ji.getWidth()) {
              pl.add(new Integer[]{ce[0]+1, ce[1]});
            }
            if(ce[0]-1 >= 0) {
              pl.add(new Integer[]{ce[0]-1, ce[1]});
            }
            if(ce[1]+1 < ji.getHeight()) {
              pl.add(new Integer[]{ce[0], ce[1]+1});
            }
            if(ce[1]-1 >= 0) {
              pl.add(new Integer[]{ce[0], ce[1]-1});
            }
          }
        }
      } else {
        while(pl.size() > 0) {
          ce = pl.poll();
          Color tmpC = ji.getPixel(ce[0], ce[1]);
          Color nc = c.copy();
          if(keepAlpha) {
            nc.setAlpha(tmpC.getAlpha());
          }
          if(!tmpC.equals(edge) && !tmpC.equals(nc)) {
            
            ji.setPixel(ce[0], ce[1], nc);
            if(ce[0]+1 < ji.getWidth()) {
              pl.add(new Integer[]{ce[0]+1, ce[1]});
            }
            if(ce[0] -1 >= 0) {
              pl.add(new Integer[]{ce[0]-1, ce[1]});
            }
            if(ce[1]+1 < ji.getHeight()) {
              pl.add(new Integer[]{ce[0], ce[1]+1});
            }
            if(ce[1] -1 >= 0) {
              pl.add(new Integer[]{ce[0], ce[1]-1});
            }
          }
        }
      }
    }

    public void fillColor(int x, int y, Color c) {
      floodFill(x,y,c, null, false);
    }

    public void drawCircle(int cx, int cy, int size, Color c, int lineWidth, boolean fill) {
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
        drawCircle(cx, cy, size, c, 1, false);
      }
    }
    
    public void drawLine(int startX, int startY, int endX, int endY, Color c, int lineWidth) {
      drawLine(startX, startY, endX, endY, c, lineWidth, false);
    }

    public void drawLine(int x, int y, int x2, int y2, Color c, int lineWidth, boolean alphaMerge) {
      List<int[]> pxlist = Utils.lineToList(x, y, x2, y2);
      JavaImage newImg = ji;
      JavaImage circle = null;
      
      if(lineWidth > 1) {
        newImg = JavaImage.create(JavaImage.MODE_RGBA, ji.getWidth(), ji.getHeight());  
        if(circle == null) {
          circle = JavaImage.create(JavaImage.MODE_RGBA, lineWidth+1, lineWidth+1);
          circle.getImageDrawer().drawCircle((lineWidth/2), (lineWidth/2), lineWidth, new Color(c.getRed(), c.getGreen(), c.getBlue()), 1, true);
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
            newImg.getImageDrawer().floodFill(tmp[0], tmp[1], c, Color.ALPHA, false);
            break;
          }
        }
        ji.merge(0, 0, newImg);
      }
    }
  }
}
