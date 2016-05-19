package me.lcw.jil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import me.lcw.jil.Utils.ImageConvertUtils;
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
public class JilImage implements BaseImage {

  private final int width;
  private final int height;
  private final MODE mode;
  protected byte[] MAP;

  private JilImage(MODE mode, int width, int height) {
    this.mode = mode;
    int size = mode.getColors()*width*height;
    MAP = new byte[size];
    this.width = width;
    this.height = height;
  }

  private JilImage(MODE mode, int width, int height, byte[] map) {
    this.mode = mode;
    MAP = map;
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
  public static JilImage create(MODE mode, int width, int height) {
    return new JilImage(mode, width, height);
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
  public static JilImage create(MODE mode, int width, int height, Color color) {
    JilImage i = new JilImage(mode, width, height);
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
  public static JilImage fromByteArray(MODE mode, int width, int height, byte[] data) {
    if(data.length != (width*height*mode.getColors())){
      throw new RuntimeException("Incorrect number of bytes to make an image of that type");
    }
    JilImage image = new JilImage(mode, width, height, data);
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
  public JilImage changeMode(MODE nmode) {
    return ImageConvertUtils.convertMode(this, nmode);
  }

  @Override
  public JilImage resizeWithBorders(int bWidth, int bHeight, Color borderColor, ScaleType st) {
    JilImage ib = JilImage.create(mode, bWidth, bHeight, borderColor);

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
  public JilImage resize(int newWidth, int newHeight, boolean keepAspect, ScaleType st) {
    if(keepAspect) {
      int[] aspect = JilUtils.getAspectSize(this.width, this.height, newWidth, newHeight);
      newWidth = aspect[0];
      newHeight = aspect[1];
    }
    JilImage tmp;
    switch(st) {
    case LINER:
      tmp = BiLinearScaler.scale(this, newWidth, newHeight);
      break;
    case CUBIC:
      tmp = BiCubicScaler.scale(this, newWidth, newHeight);
      break;
    case CUBIC_SMOOTH:
      tmp = (JilImage)JilUtils.biCubicSmooth(this, newWidth, newHeight);
    default:
      tmp = NearestNeighborScaler.scale(this, newWidth, newHeight);
    }
    return tmp;
  }

  @Override
  public void fillImageWithColor(Color c) {
    if (mode == MODE.GREY){
      Arrays.fill(MAP, c.getGrey());
    } else {
      for(int i=0; i<MAP.length/getColors(); i++) {
        int pos = i*getColors();
        MAP[pos] = c.getRed();
        MAP[pos+1] = c.getGreen();
        MAP[pos+2] = c.getBlue();
        if (mode == MODE.RGBA){
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
    int pos = ((y*this.width)+x)*(getColors()); 
    if(mode == MODE.GREY) {
      MAP[pos] = c.getGrey();
    } else {
      MAP[pos] = c.getRed();
      MAP[pos+1] = c.getGreen();
      MAP[pos+2] = c.getBlue();
      if(mode == MODE.RGBA) {
        MAP[pos+3] = c.getAlpha();
      }
    }
  }

  public void setPixelInChannel(int x, int y, byte c, byte p) {
    int POS = ((y*this.getWidth())+x)*(getColors())+c;
    MAP[POS] = p;
  }

  @Override
  public Color getPixel(int x, int y) {
    if(x < 0 || x >= width || y < 0 || y >= height) {
      return null;
    }
    int POS = ((y*this.getWidth())+x)*(getColors());
    if (this.getMode() == MODE.RGBA) {
      return new Color(MAP[POS], MAP[POS+1], MAP[POS+2], MAP[POS+3]);
    } else if (this.getMode() == MODE.RGB) {
      return new Color(MAP[POS], MAP[POS+1], MAP[POS+2]);
    } else {
      return new Color(MAP[POS]);
    }
  }

  public byte getByteInChannel(int x, int y, byte c) {
    int POS = ((y*this.getWidth())+x)*(getColors())+c;
    return MAP[POS];
  }

  @Override
  public void paste(int x, int y, BaseImage img) {
    if(img.getMode() != mode) {
      img = img.changeMode(mode);
    }
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

    int thisLineWidth = width * getColors();
    int imgLineWidth = img.getWidth() * img.getColors();
    int imgXOffBytes = imgXOff * img.getColors();
    int XBytes = x * getColors();

    for(int h=y; h < height; h++) {
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
  public void merge(int x, int y, BaseImage img){
    if (img.getHeight()+y < 0 || y >= this.height) {
      return;
    }

    if (img.getWidth()+x < 0 || x >= this.width) {
      return;
    }
    if(this.getMode() != MODE.RGBA && img.getMode() != MODE.RGBA) {
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

    int thisLineWidth = this.width * getColors();
    int imgLineWidth = img.getWidth() * img.getColors();
    int imgXOffBytes = imgXOff * img.getColors();
    int XBytes = x*getColors();
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
        int wThisBytes = w*getColors();
        int imgXPos = imgStart+wImgBytes+imgXOffBytes;
        int thisXPos = thisStart+XBytes+wThisBytes;
        if(img.getMode() == MODE.RGBA && img.getArray()[imgXPos+3] == 0) {
          continue;
        } else if (mode == MODE.RGBA && this.MAP[thisXPos+3] == 0) {
          System.arraycopy(img.getArray(), imgXPos, this.MAP, thisXPos, getColors());
        } else if (img.getMode() == MODE.RGBA && img.getArray()[imgXPos+3] == 255) {
          System.arraycopy(img.getArray(), imgXPos, this.MAP, thisXPos, getColors());
        } else {
          Color c = img.getPixel((w+imgXOff), imgYPos);
          Color c2 = this.getPixel(w+x, h);
          Color ncolor = Color.mergeColors(c2, c);
          this.setPixel(w+x, h, ncolor);
        }
      }
    }
  }


  @Override
  public JilImage copy() {
    JilImage newImage = JilImage.create(mode, width, height);
    System.arraycopy(MAP, 0, newImage.MAP, 0, MAP.length);
    return newImage;
  }

  @Override
  public JilImage cut(int x, int y, int width, int height) {
    JilImage newImage = JilImage.create(mode, width, height);

    for(int yy = 0; yy< height; yy++) {
      int startPos = (((y+yy)*this.width)+x)*(getColors());
      System.arraycopy(this.MAP, startPos, newImage.MAP, (yy*width*(newImage.getColors())), width*(newImage.getColors()));
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
  public MODE getMode(){
    return this.mode;
  }  

  @Override
  public int getColors(){
    return this.mode.getColors();
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
  public JilImage toJilImage() {
    return this;
  }

  @Override
  public JavaDraw draw() {
    return new JavaDraw(this);
  }

  public static class JavaDraw implements Draw {

    private final JilImage ji;

    public JavaDraw(JilImage ji) {
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
            Color nc = c;
            if(keepAlpha) {
              nc = c.changeAlpha(tmpC.getAlpha());
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
          Color nc = c;
          if(keepAlpha) {
            nc = c.changeAlpha(tmpC.getAlpha());
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
        newImg = JilImage.create(BaseImage.MODE.RGBA, ji.getWidth(), ji.getHeight());  
        if(circle == null) {
          circle = JilImage.create(BaseImage.MODE.RGBA, lineWidth+1, lineWidth+1);
          circle.draw().circle((lineWidth/2), (lineWidth/2), lineWidth, new Color(c.getRed(), c.getGreen(), c.getBlue()), 1, true);
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
  public String toString() {
    return "JilImage: width:"+width+": height"+height+": mode:"+mode.toString();
  }
}
