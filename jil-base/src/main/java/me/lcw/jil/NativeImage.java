package me.lcw.jil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

import me.lcw.jil.Utils.ImageConvertUtils;
import me.lcw.jil.Utils.ImageFillUtils;
import me.lcw.jil.Utils.JilUtils;
import me.lcw.jil.parsers.png.PNGEncoder;
import me.lcw.jil.parsers.tiff.TIFFDecoder;
import me.lcw.jil.parsers.tiff.TIFFEncoder;
import me.lcw.jil.scalers.BiCubicScaler;
import me.lcw.jil.scalers.BiLinearScaler;
import me.lcw.jil.scalers.NearestNeighborScaler;
import sun.misc.Unsafe;


/**
 *
 * Main Image object used to construct new Image files.  
 * 
 * @author lcw - Luke Wahlmeier
 */
public class NativeImage implements BaseImage {

  private final JilDraw draw = new JilDraw(this);
  private final int width;
  private final int height;
  private final MODE mode;
  private final UnsafeByteArray data;

  private NativeImage(MODE mode, int width, int height) {
    this.mode = mode;
    int size = mode.getColors()*width*height;
    data = new UnsafeByteArray(size);
    this.width = width;
    this.height = height;
  }

  private NativeImage(MODE mode, int width, int height, byte[] map) {
    this.mode = mode;
    this.width = width;
    this.height = height;

    data = new UnsafeByteArray(mode.getColors()*width*height);
    data.setArrayData(map, 0, 0, map.length);
  }
  
  private NativeImage(MODE mode, int width, int height, UnsafeByteArray uba) {
    this.mode = mode;
    this.width = width;
    this.height = height;
    data = uba;
  }

  /**
   * Main Method for creating a new Image
   * 
   * @param mode Image mode, uses the static bytes Image.MODE_(L, RGB, RGBA)
   * @param width How wide the image should be in pixels
   * @param height How high the Image should be in pixels
   * @return Returns an Image object
   */
  public static NativeImage create(MODE mode, int width, int height) {
    return new NativeImage(mode, width, height);
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
  public static NativeImage create(MODE mode, int width, int height, Color color) {
    NativeImage i = new NativeImage(mode, width, height);
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
  public static NativeImage fromByteArray(MODE mode, int width, int height, byte[] data) {
    if(data.length != (width*height*mode.getColors())){
      throw new RuntimeException("Incorrect number of bytes to make an image of that type");
    }
    NativeImage image = new NativeImage(mode, width, height, data);
    return image;
  }

  /**
   * Static Method that allows you to open a file, just pass in the path/filename. 
   * @param filename  Filename to attempt to open.
   * @return Returns an Image object from the provided file.
   * @throws ImageException This can happen if we do not know the type of file we where asked to open.
   * @throws IOException This happens when we can not access the file.
   */
  public static NativeImage open(String filename) throws ImageException, IOException {
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

  public static NativeImage fromJilImage(JilImage ji) {
    return new NativeImage(ji.getMode(), ji.getWidth(), ji.getHeight(), ji.getArray());
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
  public static NativeImage open(String filename, ImageType type) throws IOException, ImageException {
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
    return fromJilImage(image);
  }
  
  public UnsafeByteArray getUnsafeByteArray() {
    return data;
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
  public NativeImage changeMode(MODE nmode) {
    NativeImage nji = create(nmode, getWidth(), getHeight());
    ImageConvertUtils.simpleModeConvert(this, nji);
    return nji;
  }

  @Override
  public NativeImage resizeWithBorders(int bWidth, int bHeight, Color borderColor, ScaleType st) {
    NativeImage ib = NativeImage.create(mode, bWidth, bHeight, borderColor);

    NativeImage newI = resize(bWidth, bHeight, true, st);

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
  public NativeImage resize(int newWidth, int newHeight, boolean keepAspect, ScaleType st) {
    if(keepAspect) {
      int[] aspect = JilUtils.getAspectSize(this.width, this.height, newWidth, newHeight);
      newWidth = aspect[0];
      newHeight = aspect[1];
    }
    switch(st) {
    case LINER: {
      final NativeImage tmp = NativeImage.create(mode, newWidth, newHeight);
      BiLinearScaler.scaleGeneric(this, tmp);
      return tmp;
    }
    case CUBIC: {
      final NativeImage tmp = NativeImage.create(mode, newWidth, newHeight);
      BiCubicScaler.scaleGeneric(this, tmp);
      return tmp;
    }
    case CUBIC_SMOOTH:
      return (NativeImage)JilUtils.biCubicSmooth(this, newWidth, newHeight);
    default: {
      final NativeImage tmp = NativeImage.create(mode, newWidth, newHeight);
      NearestNeighborScaler.scaleNativeImage(this, tmp);
      return tmp;
    }
    }

  }

  @Override
  public void fillImageWithColor(Color c) {
    if (mode == MODE.GREY){
      this.data.setAll(c.getGrey());
    } else {
      if (mode == MODE.RGBA){
        for(int i=0; i<width*height; i++) {
          int pos = i*getColors();
          data.setInt(pos, c.getRGBA());
        }
      } else {
        for(int i=0; i<(width*height); i++) {
          int pos = i*getColors();
          data.setByte(pos, c.getRed());
          data.setByte(pos+1, c.getGreen());
          data.setByte(pos+2, c.getBlue());
        } 
      }
    } 
  }

  @Override
  public void setPixel(int x, int y, Color c) {
    int pos = (y*getWidth() + x);
    if(mode == MODE.GREY) {
      data.setByte(pos, c.getGrey());
    } else if (mode == MODE.RGBA) {
      pos*=4;
      data.setInt(pos, c.getRGBA());
    } else {
      pos*=3;
      data.setByte(pos, c.getRed());
      data.setByte(pos+1, c.getGreen());
      data.setByte(pos+2, c.getBlue());
    }
  }

  @Override
  public Color getPixel(int x, int y) {
    int pos = (y*getWidth() + x);
    if(mode == MODE.GREY) {
      return new Color(data.getByte(pos));
    } else if (mode == MODE.RGBA) {
      pos*=4;
      return Color.fromRGBA(data.getInt(pos));
    } else {
      pos*=3;
      return new Color(data.getByte(pos),
          data.getByte(pos+1),
          data.getByte(pos+2));
    }
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

    byte[] iba = img.getArray();
    
    for(int h=y; h < height; h++) {
      int imgYPos = h-y+imgYOff;
      if( imgYPos >= img.getHeight()) {
        break;
      }
      int thisStart = thisLineWidth*h;
      int imgStart = imgLineWidth*(imgYPos);
      data.setArrayData(iba, imgStart+(imgXOffBytes), thisStart+(XBytes), Math.min(imgLineWidth-(imgXOffBytes), thisLineWidth-(XBytes)));
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

    for(int h=y; h < this.height; h++) {
      int imgYPos = h-y+imgYOff;
      if( imgYPos >= img.getHeight()) {
        break;
      }
      int maxWidth = Math.min((img.getWidth()-(imgXOff)), (this.width-x));
      for(int w = 0; w < maxWidth; w++) {

        Color c = img.getPixel((w+imgXOff), imgYPos);
        Color c2 = this.getPixel(w+x, h);
        if(img.getMode() == MODE.RGBA && (c.getAlpha()) == 0) {
          continue;
        } else if (mode == MODE.RGBA && (c2.getAlpha()) == 0) {
          this.setPixel(w+x, h, c);
        } else if (img.getMode() == MODE.RGBA && c.getAlpha() == -1) {
          this.setPixel(w+x, h, c);
        } else {
          System.out.println(c);
          Color ncolor = Color.mergeColors(c2, c);
          this.setPixel(w+x, h, ncolor);
        }
      }
    }
  }


  @Override
  public NativeImage copy() {
    return new NativeImage(mode, width, height, data.copy());
  }

  @Override
  public NativeImage cut(int x, int y, int width, int height) {
    //TODO:
    return create(mode, width-x, height-y);
  }

  /**
   * Sets this Image to random Data
   */
  public void mkRandom() {
    Random r = new Random();
    for(int i=0; i<((width*height*mode.getColors()))/4; i++){
      data.setInt(i*4, r.nextInt());
    }
  }

  @Override
  public byte[] getArray() {
    byte[] ba = new byte[width*height*mode.getColors()];
    data.getArrayData(ba, 0, 0, ba.length);
    return ba;
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
    return this.toJilImage();
  }

  @Override
  public Draw draw() {
    return draw;
  }

  private static class JilDraw implements Draw {

    private final NativeImage ni;

    public JilDraw (NativeImage ni) {
      this.ni = ni;
    }


    @Override
    public void rect(int x, int y, int w, int h, Color c, int lineWidth, boolean fill) {
      int maxW = x+w;
      int maxH = y+h;
      if (ni.getWidth() < maxW) {
        maxW = ni.getWidth() ;
      }
      if (ni.getHeight() < maxH) {
        maxH = ni.getHeight();
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
            ni.setPixel(W, H, c);
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
      if(x < 0 || x >= ni.getWidth()) {
        return;
      }
      if(y <0 || y>=ni.getWidth()) {
        return;
      }
      if(edge == null) {
        ImageFillUtils.noEdgeFill(ni, x, y, c, keepAlpha);
      } else {
        ImageFillUtils.edgeFill(ni, x, y, c, edge, keepAlpha);
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
        if(px >= 0 && py >= 0 && py < ni.getHeight() && px < ni.getWidth()) {
          ni.setPixel(px, py, c);
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
      NativeImage newImg = ni;
      NativeImage circle = null;

      if(lineWidth > 1) {
        newImg = NativeImage.create(BaseImage.MODE.RGBA, ni.getWidth(), ni.getHeight(), Color.ALPHA);  
        if(circle == null) {
          circle = NativeImage.create(BaseImage.MODE.RGBA, lineWidth+1, lineWidth+1, Color.ALPHA);
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
        
        ni.merge(0, 0, newImg);
      }
    }
  }

  @Override
  public String toString() {
    return "UnsafeJilImage: width:"+width+": height"+height+": mode:"+mode.toString();
  }

  
  public static class UnsafeByteArray {
    final long address;
    final int size;
    
    public UnsafeByteArray(int size) {
      this.size = size;
      address = getUnsafe().allocateMemory(size);
    }
    
    public void setArrayData(byte[] srcArray, int srcOff, int destOff, int length) {
      getUnsafe().copyMemory(srcArray, Unsafe.ARRAY_BYTE_BASE_OFFSET+srcOff, null, address+destOff, length);
    }
    
    public void getArrayData(byte[] dstArray, int dstOff, int srcOff, int length) {
      getUnsafe().copyMemory(null, address+srcOff, dstArray, Unsafe.ARRAY_BYTE_BASE_OFFSET+srcOff, length);
    }
    
    public void setFromUBA(UnsafeByteArray uba, int srcOff, int dstOff, int length) {
      getUnsafe().copyMemory(uba.address+srcOff, address+dstOff, length);
    }
    
    public void setByte(int offset, byte b) {
      getUnsafe().putByte(address+offset, b);
    }
    
    public byte getByte(int offset) {
      return getUnsafe().getByte(address+offset);
    }
    
    public int getInt(int offset) {
      return little2big(getUnsafe().getInt(address+offset));
    }
    
    public void setInt(int offset, int data) {
      getUnsafe().putInt(address+offset, little2big(data));
    }
    
    public int getSize() {
      return size;
    }
    
    public UnsafeByteArray copy() {
      UnsafeByteArray uba = new UnsafeByteArray(size);
      getUnsafe().copyMemory(address, uba.address, size);
      return uba;
    }
    
    public void setAll(byte b) {
      getUnsafe().setMemory(address, size, b);
    }

    @Override
    protected void finalize() {
      getUnsafe().freeMemory(address);
    }
  }
  
  private static volatile Unsafe unsafe;
  
  public static Unsafe getUnsafe() {
    if(unsafe == null) {
      try {
        Field singleoneInstanceField = Unsafe.class.getDeclaredField("theUnsafe");
        singleoneInstanceField.setAccessible(true);
        unsafe = (Unsafe) singleoneInstanceField.get(null);
      } catch (Exception e) {
        throw new RuntimeException(e);
      } 
    }
    return unsafe;

  }

  public static long getAddressOfObject(Object obj) {
    Object helperArray[]    = new Object[1];
    helperArray[0]          = obj;
    long baseOffset         = getUnsafe().arrayBaseOffset(Object[].class);
    long addressOfObject    = getUnsafe().getLong(helperArray, baseOffset);      
    return addressOfObject;
  }
  
  public static void main(String[] args) throws InterruptedException {
    NativeImage ni = NativeImage.fromJilImage(RGBImageGenerator());
    ni.fillImageWithColor(Color.BLACK);
    Thread.sleep(1000);
  }
  
  public static JilImage RGBImageGenerator() {
      JilImage img = JilImage.create(BaseImage.MODE.RGB, 480, 270);
      for(int i=0; i<img.getHeight(); i++) {
        double pct = i/(double)img.getHeight();
        byte r = (byte)(0xff * (pct*2));
        byte g = (byte)(0xff* (pct*1));
        byte b = (byte)(0xff -(0xff* (pct*2)));
        Color c = new Color(r,g,b);
        img.draw().line(0, i, img.getWidth(), i, c, 0, false);
      }
    return img;
  }
  
  private static int little2big(int i) {
    return (i&0xff)<<24 | (i&0xff00)<<8 | (i&0xff0000)>>8 | (i>>24)&0xff;
}
}

