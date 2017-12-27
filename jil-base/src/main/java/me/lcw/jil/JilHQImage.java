package me.lcw.jil;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import me.lcw.jil.Utils.ColorUtils;
import me.lcw.jil.Utils.ImageConvertUtils;
import me.lcw.jil.Utils.ImageFillUtils;
import me.lcw.jil.Utils.JilUtils;


/**
 *
 * Main Image object used to construct new Image files.  
 * 
 * @author lcw - Luke Wahlmeier
 */
public class JilHQImage extends JilImage {

  private final JilByteDraw draw = new JilByteDraw(this);
  private final double[] MAP;

  public JilHQImage(ImageMode mode, int width, int height) {
    super(mode, width, height);
    if(mode != ImageMode.GREY8 && mode != ImageMode.RGB24 && mode != ImageMode.RGBA32) {
      throw new IllegalArgumentException(this.getClass().getSimpleName()+" cant not be created with mode "+mode);
    }
    int size = this.getColors()*(getWidth()+getHeight());
    MAP = new double[size];
  }

  protected JilHQImage(ImageMode mode, int width, int height, double[] map) {
    super(mode, width, height);
    MAP = map;
  }

  /**
   * Main Method for creating a new Image
   * 
   * @param mode Image mode, uses the static bytes Image.MODE_(L, RGB, RGBA)
   * @param width How wide the image should be in pixels
   * @param height How high the Image should be in pixels
   * @return Returns an Image object
   */
  public static JilHQImage create(ImageMode mode, int width, int height) {
    return new JilHQImage(mode, width, height);
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
  public static JilHQImage create(ImageMode mode, int width, int height, Color color) {
    JilHQImage i = new JilHQImage(mode, width, height);
    i.fillImageWithColor(color);
    return i;
  }

  @Override
  public JilImage changeMode(ImageMode nmode) {
    return ImageConvertUtils.convertMode(this, nmode);
  }



  @Override
  public void setPixel(int x, int y, Color c) {
    if(x<0 || x>=this.getWidth()) {
      return;
    }
    if(y<0 || y>=this.getHeight()) {
      return;
    }
    int pos = ((y*this.getWidth())+x)*(getColors()); 
    if(getMode() == ImageMode.GREY8) {
      MAP[pos] = c.getGreyByte();
    } else {
      MAP[pos] = c.getRedByte();
      MAP[pos+1] = c.getGreenByte();
      MAP[pos+2] = c.getBlueByte();
      if(getMode() == ImageMode.RGBA32) {
        MAP[pos+3] = c.getAlphaByte();
      }
    }
  }

//  public void setPixelInChannel(int x, int y, byte c, byte p) {
//    int POS = ((y*this.getWidth())+x)*(getColors())+c;
//    MAP[POS] = p;
//  }
//  public byte getByteInChannel(int x, int y, byte c) {
//    int POS = ((y*this.getWidth())+x)*(getColors())+c;
//    return MAP[POS];
//  }

  @Override
  public Color getPixel(int x, int y) {
    if(x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) {
      return null;
    }
    int POS = ((y*this.getWidth())+x)*(getColors());
    if (this.getMode() == ImageMode.RGBA32) {
      return Color.fromRGBAPCT(MAP[POS], MAP[POS+1], MAP[POS+2], MAP[POS+3]);
    } else if (this.getMode() == ImageMode.RGB24) {
      return Color.fromRGBPCT(MAP[POS], MAP[POS+1], MAP[POS+2]);
    } else {
      return Color.fromGreyPCT(MAP[POS]);
    }
  }

  @Override
  public void paste(int x, int y, BaseImage img) {
    if(img.getMode() != getMode()) {
      img = img.changeMode(getMode());
    }
    if (img.getHeight()+y < 0 || y >= this.getHeight()) {
      return;
    }

    if (img.getWidth()+x < 0 || x >= this.getWidth()) {
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

    int thisLineWidth = getWidth() * getColors();
    int imgLineWidth = img.getWidth() * img.getColors();
    int imgXOffBytes = imgXOff * img.getColors();
    int XBytes = x * getColors();

    for(int h=y; h < getHeight(); h++) {
      int imgYPos = h-y+imgYOff;
      if( imgYPos >= img.getHeight()) {
        break;
      }
      int thisStart = thisLineWidth*h;
      int imgStart = imgLineWidth*(imgYPos);
      System.arraycopy(img.getByteArray(), imgStart+(imgXOffBytes), this.MAP, thisStart+(XBytes), Math.min(imgLineWidth-(imgXOffBytes), thisLineWidth-(XBytes)));
    }
  }

  @Override
  public void merge(int x, int y, BaseImage img) {
    if (img.getHeight()+y < 0 || y >= this.getHeight()) {
      return;
    }

    if (img.getWidth()+x < 0 || x >= this.getWidth()) {
      return;
    }
    if(this.getMode() != ImageMode.RGBA32 && img.getMode() != ImageMode.RGBA32) {
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

    int thisLineWidth = this.getWidth() * getColors();
    int imgLineWidth = img.getWidth() * img.getColors();
    int imgXOffBytes = imgXOff * img.getColors();
    int XBytes = x*getColors();
    for(int h=y; h < this.getHeight(); h++) {
      int imgYPos = h-y+imgYOff;
      if( imgYPos >= img.getHeight()) {
        break;
      }
      int thisStart = thisLineWidth*h;
      int imgStart = imgLineWidth*(imgYPos);
      int maxWidth = Math.min((img.getWidth()-(imgXOff)), (this.getWidth()-x));
      for(int w = 0; w < maxWidth; w++) {
        int wImgBytes = w*img.getColors();
        int wThisBytes = w*getColors();
        int imgXPos = imgStart+wImgBytes+imgXOffBytes;
        int thisXPos = thisStart+XBytes+wThisBytes;
        if(img.getMode() == ImageMode.RGBA32 && img.getByteArray()[imgXPos+3] == 0) {
          continue;
        } else if (getMode() == ImageMode.RGBA32 && this.MAP[thisXPos+3] == 0) {
          System.arraycopy(img.getByteArray(), imgXPos, this.MAP, thisXPos, getColors());
        } else if (img.getMode() == ImageMode.RGBA32 && img.getByteArray()[imgXPos+3] == 255) {
          System.arraycopy(img.getByteArray(), imgXPos, this.MAP, thisXPos, getColors());
        } else {
          Color c = img.getPixel((w+imgXOff), imgYPos);
          Color c2 = this.getPixel(w+x, h);
          Color ncolor = ColorUtils.mergeColors(c2, c);
          this.setPixel(w+x, h, ncolor);
        }
      }
    }
  }

  @Override
  public JilHQImage copy() {
    JilHQImage newImage = JilHQImage.create(getMode(), getWidth(), getHeight());
    System.arraycopy(MAP, 0, newImage.MAP, 0, MAP.length);
    return newImage;
  }

  @Override
  public JilHQImage cut(int x, int y, int width, int height) {
    JilHQImage newImage = JilHQImage.create(getMode(), width, height);

    for(int yy = 0; yy< height; yy++) {
      int startPos = (((y+yy)*this.getWidth())+x)*(getColors());
      System.arraycopy(this.MAP, startPos, newImage.MAP, (yy*width*(newImage.getColors())), width*(newImage.getColors()));
    }
    return newImage;
  }

  /**
   * Sets this Image to random Data
   */
  public void mkRandom() {
    for(int i=0; i<MAP.length; i++) {
      MAP[i] = ThreadLocalRandom.current().nextDouble(0,1.0);
    }
  }

  @Override
  public byte[] getByteArray() {
    byte[] ba = new byte[MAP.length];
    for(int i=0; i<MAP.length; i++) {
      ba[i] = (byte)(MAP[i]*255);
    }
    return ba;
  }

  @Override
  public JilImage toJilImage() {
    return this.copy();
  }

  @Override
  public Draw draw() {
    return draw;
  }

  private static class JilByteDraw implements Draw {

    private final JilHQImage ji;

    public JilByteDraw (JilHQImage ji) {
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
      JilHQImage newImg = ji;
      JilHQImage circle = null;

      if(lineWidth > 1) {
        newImg = JilHQImage.create(BaseImage.ImageMode.RGBA32, ji.getWidth(), ji.getHeight());  
        if(circle == null) {
          circle = JilHQImage.create(BaseImage.ImageMode.RGBA32, lineWidth+1, lineWidth+1);
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
  public String toString() {
    return "JilByteImage: width:"+getWidth()+": height"+getHeight()+": mode:"+getMode().toString();
  }

  
}
