package me.lcw.jil.awt;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import me.lcw.jil.BaseImage;
import me.lcw.jil.Color;
import me.lcw.jil.Draw;
import me.lcw.jil.ImageException;
import me.lcw.jil.JilImage;
import me.lcw.jil.Utils.ImageFillUtils;
import me.lcw.jil.Utils.JilUtils;
import me.lcw.jil.awt.parsers.JpegFile;
import me.lcw.jil.awt.parsers.PngFile;
import me.lcw.jil.parsers.tiff.TIFFDecoder;
import me.lcw.jil.parsers.tiff.TIFFEncoder;

public class AWTImage implements BaseImage {

  private final AWTDraw draw = new AWTDraw(this);
  private final int width;
  private final int height;
  private final ImageMode mode;
  private final BufferedImage bi;
  private volatile boolean localBACache = false;
  private volatile boolean localBACacheDirty = true;
  private volatile byte[] localBA = null;
  private volatile boolean smoothCircle = false;


  private AWTImage(ImageMode mode, int width, int height) {
    this.mode = mode;
    this.width = width;
    this.height = height;
    if(mode == BaseImage.ImageMode.GREY8) {
      bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
    } else if(mode == BaseImage.ImageMode.RGB24) {
      bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
    } else {
      bi = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
    }
  }

  private AWTImage(ImageMode mode, int width, int height, BufferedImage cbi) {
    this.mode = mode;
    this.width = width;
    this.height = height;
    bi = cbi;
  }

  private AWTImage(BufferedImage cbi, ImageMode mode) {
    this(mode, cbi.getWidth(), cbi.getHeight(), cbi);
  }

  public static AWTImage create(ImageMode mode, int width, int height) {
    return new AWTImage(mode, width, height);
  }

  public static AWTImage create(ImageMode mode, int width, int height, Color c) {
    AWTImage i = new AWTImage(mode, width, height);
    i.fillImageWithColor(c);
    return i;
  }

  public static AWTImage fromBaseImage(BaseImage img) {
    BufferedImage BB;
    if(img.getMode() == ImageMode.GREY8) {
      BB = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
      byte[] test = ((DataBufferByte) BB.getRaster().getDataBuffer()).getData();
      System.arraycopy(img.getByteArray(), 0, test, 0, test.length);
    } else if(img.getMode() == ImageMode.RGB24) {
      BaseImage nbi = img;
      BB = new BufferedImage(nbi.getWidth(), nbi.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
      byte[] array = ((DataBufferByte) BB.getRaster().getDataBuffer()).getData();
      byte[] MAP = nbi.getByteArray();
      for(int i=0; i<array.length/3; i++) {
        int pos = i*3;
        array[pos] = MAP[pos+2];
        array[pos+1] = MAP[pos+1];
        array[pos+2] = MAP[pos];
      }
    } else {
      BB = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
      byte[] array = ((DataBufferByte) BB.getRaster().getDataBuffer()).getData();
      byte[] MAP = img.getByteArray();
      for(int i=0; i<array.length/4; i++) {
        int pos = i*4;
        array[pos] = MAP[pos+3];
        array[pos+1] = MAP[pos+2];
        array[pos+2] = MAP[pos+1];
        array[pos+3] = MAP[pos];
      }
    }
    return new AWTImage(BB, img.getMode());
  }

  public static AWTImage fromByteArray(ImageMode mode, int width, int height, byte[] ba){
    return AWTImage.fromBaseImage(JilImage.fromByteArray(mode, width, height, ba));
  }

  public static AWTImage fromBufferedImage(BufferedImage bi) {
    switch(bi.getType()) {
    case BufferedImage.TYPE_USHORT_GRAY: {
      BufferedImage nbi = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
      Graphics g = nbi.getGraphics();
      try {
        g.drawImage(bi, 0, 0, null);
      } finally {
        g.dispose();
      }
      return new AWTImage(BaseImage.ImageMode.GREY8, nbi.getWidth(), nbi.getHeight(), nbi);
    }
    case BufferedImage.TYPE_BYTE_INDEXED:
    case BufferedImage.TYPE_INT_RGB:
    case BufferedImage.TYPE_INT_BGR:
    case BufferedImage.TYPE_USHORT_555_RGB:
    case BufferedImage.TYPE_USHORT_565_RGB:{
      BufferedImage nbi = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
      Graphics g = nbi.getGraphics();
      try {
        g.drawImage(bi, 0, 0, null);
        return new AWTImage(BaseImage.ImageMode.RGB24, nbi.getWidth(), nbi.getHeight(), nbi);
      } finally {
        g.dispose();
      }
    }

    case BufferedImage.TYPE_BYTE_GRAY:{
      return new AWTImage(BaseImage.ImageMode.GREY8, bi.getWidth(), bi.getHeight(), bi);
    }
    case BufferedImage.TYPE_3BYTE_BGR:{
      return new AWTImage(BaseImage.ImageMode.RGB24, bi.getWidth(), bi.getHeight(), bi);
    }
    case BufferedImage.TYPE_4BYTE_ABGR: {
      return new AWTImage(BaseImage.ImageMode.RGBA32, bi.getWidth(), bi.getHeight(), bi);
    }
    case BufferedImage.TYPE_BYTE_BINARY:
    case BufferedImage.TYPE_4BYTE_ABGR_PRE:
    case BufferedImage.TYPE_INT_ARGB:
    case BufferedImage.TYPE_INT_ARGB_PRE:
    default: {
      BufferedImage nbi = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
      Graphics g = nbi.getGraphics();
      try {
        g.drawImage(bi, 0, 0, null);
        return new AWTImage(BaseImage.ImageMode.RGBA32, nbi.getWidth(), nbi.getHeight(), nbi);
      } finally {
        g.dispose();
      }
    }

    }
  }

  public static AWTImage open(String filename) throws ImageException, IOException {
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

  public static AWTImage open(String filename, ImageType type) throws IOException, ImageException {
    switch(type) {
    case TIFF:
      return AWTImage.fromBaseImage(TIFFDecoder.decodeFromFile(new File(filename)));
    case PNG:
    case JPEG:
      return AWTImage.fromBufferedImage(ImageIO.read(new File(filename)));
    default:
      throw new ImageException("Could not determen filetype");
    }
  }

  public void enableBAcache() {
    localBACache = true;
  }

  public void disableBAcache() {
    localBACache = false;
    localBACacheDirty = true;
    localBA = null;
  }

  public void enableSmoothCircle() {
    this.smoothCircle = true;
  }

  public void disableSmoothCircle() {
    this.smoothCircle = false;
  }

  public BufferedImage getBufferedImage() {
    return bi;
  }

  @Override
  public void save(File file) throws IOException, ImageException {
    ImageType t = JilUtils.getImageType(file);
    save(file, t);
  }

  @Override
  public void save(String file) throws IOException, ImageException {
    ImageType t = JilUtils.getImageType(file);
    save(file, t);
  }

  @Override
  public void save(File file, ImageType type) throws IOException, ImageException {
    save(file.getAbsolutePath(), type);
  }

  @Override
  public void save(String filename, ImageType type) throws IOException, ImageException {
    switch(type) {
    case JPEG: {
      JpegFile.save(filename, this);
    }break;
    case PNG: {
      PngFile.save(filename, this);
    } break;
    case TIFF: {
      TIFFEncoder.encodeToFile(this, new File(filename));
    } break;
    default:
      break;
    }
  }

  @Override
  public JilImage toJilImage() {
    return JilImage.fromByteArray(this.mode, width, height, this.getByteArray());
  }

  @Override
  public AWTImage changeMode(ImageMode nmode) {
    BufferedImage nbi;
    if(nmode == BaseImage.ImageMode.GREY8) {
      nbi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
    } else if(nmode == BaseImage.ImageMode.RGB24) {
      nbi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
    } else {
      nbi = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
    }
    Graphics g = nbi.getGraphics();
    try {
      g.drawImage(bi, 0, 0, null);
    } finally {
      g.dispose();
    }
    AWTImage ai = new AWTImage(nbi, nmode);
    if(localBACache) {
      ai.enableBAcache();
    }
    return ai;
  }

  @Override
  public AWTImage resizeWithBorders(int bWidth, int bHeight, Color borderColor, ScaleType st) {
    AWTImage aio = AWTImage.create(this.getMode(), bWidth, bHeight, borderColor);
    AWTImage aii = resize(bWidth, bHeight, true, st);
    if(aio.getHeight() == aii.getHeight()) {
      int pos = (aio.getWidth()/2) - (aii.getWidth()/2);
      aio.paste(pos, 0, aii);
    } else {
      int pos = (aio.getHeight()/2)  - (aii.getHeight()/2);
      aio.paste(0, pos, aii);
    }
    if(localBACache) {
      aio.enableBAcache();
    }
    return aio;
  }

  @Override
  public AWTImage resize(int newWidth, int newHeight, boolean keepAspect, ScaleType st) {
    if(keepAspect) {
      int[] aspect = JilUtils.getAspectSize(this.width, this.height, newWidth, newHeight);
      newWidth = aspect[0];
      newHeight = aspect[1];
    }
    AWTImage nimg;
    BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, bi.getType());
    Graphics2D g = resizedImage.createGraphics();
    try{
      switch(st) {
      case LINER:{
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g.drawImage(bi, 0, 0, newWidth, newHeight, null);
        nimg = new AWTImage(resizedImage, mode);
      } break;      
      case CUBIC:{
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawImage(bi, 0, 0, newWidth, newHeight, null);
        nimg = new AWTImage(resizedImage, mode);
      } break;
      case CUBIC_SMOOTH:
        nimg = (AWTImage)JilUtils.biCubicSmooth(this, newWidth, newHeight);
      default:{
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g.drawImage(bi, 0, 0, newWidth, newHeight, null);
        nimg = new AWTImage(resizedImage, mode);
      }break;
      }
      if(localBACache) {
        nimg.enableBAcache();
      }
      return nimg;
    } finally {
      g.dispose();

    }
  }

  @Override
  public void fillImageWithColor(Color c) {
    Graphics2D graphics = bi.createGraphics();
    try {
      graphics.setPaint(AWTUtils.toAWTColor(c));
      graphics.fillRect(0, 0, width, height);
      this.localBACacheDirty = true;
    } finally {
      graphics.dispose();
    }
  }

  @Override
  public void setPixel(int x, int y, Color c) {
    bi.setRGB(x, y, AWTUtils.toAWTColor(c).getRGB());
    this.localBACacheDirty = true;
  }

  @Override
  public Color getPixel(int x, int y) {
    if(x >= 0 && x < width && y>=0 && y<height) {
      java.awt.Color ac = new java.awt.Color(bi.getRGB(x, y), true);
      return AWTUtils.toJILColor(ac);
    }
    return null;
  }

  @Override
  public void paste(int x, int y, BaseImage img) {
    BufferedImage bi2;
    if(img instanceof AWTImage) {
      bi2 = ((AWTImage)img).bi;
    } else {
      bi2 = AWTImage.fromBaseImage(img).bi;
    }
    Graphics2D gi = bi.createGraphics();
    try{
      float opacity = 1.0f;
      gi.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, opacity));
      gi.drawImage(bi2, x, y, null);
      this.localBACacheDirty = true;
    } finally {
      gi.dispose();
    }
  }

  @Override
  public void merge(int x, int y, BaseImage img) {
    BufferedImage bi2;
    if(img instanceof AWTImage) {
      bi2 = ((AWTImage)img).bi;
    } else {
      bi2 = AWTImage.fromBaseImage(img).bi;
    }
    Graphics2D gi = bi.createGraphics();
    try {
      float opacity = 1.0f;
      gi.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
      gi.drawImage(bi2, x, y, null);
      this.localBACacheDirty = true;
    } finally {
      gi.dispose();
    }
  }

  @Override
  public AWTImage copy() {
    BufferedImage b = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());
    Graphics g = b.getGraphics();
    try {
      g.drawImage(bi, 0, 0, null);
      AWTImage ai = new AWTImage(b, mode);
      if(localBACache) {
        ai.enableBAcache();
      }
      return ai;
    } finally {
      g.dispose();
    }

  }

  @Override
  public AWTImage cut(int x, int y, int w, int h) {
    BufferedImage bi2 = new BufferedImage(w, h, bi.getType());
    Graphics2D gi2 = bi2.createGraphics();
    try{
      gi2.drawImage(bi.getSubimage(x, y, w, h), 0, 0, null);
      AWTImage ai = new AWTImage(bi2, mode);
      if(localBACache) {
        ai.enableBAcache();
      }
      return ai;
    } finally {
      gi2.dispose();
    }

  }

  @Override
  public byte[] getByteArray() {

    if(this.localBACache && !this.localBACacheDirty) {
      return this.localBA;
    } else {
      byte[] fba;
      if(bi.getType() == BufferedImage.TYPE_BYTE_GRAY) {
        fba = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
      } else if (bi.getType() == BufferedImage.TYPE_3BYTE_BGR){
        byte[] ba = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        byte[] nba = new byte[ba.length];
        for(int i=0; i<ba.length/3; i++) {
          int pos = i*3;
          nba[pos] = ba[pos+2];
          nba[pos+1] = ba[pos+1];
          nba[pos+2] = ba[pos];
        }
        fba = nba;
      } else {
        byte[] ba = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        byte[] nba = new byte[ba.length];
        for(int i=0; i<ba.length/4; i++) {
          int pos = i*4;
          nba[pos] = ba[pos+3];
          nba[pos+1] = ba[pos+2];
          nba[pos+2] = ba[pos+1];
          nba[pos+3] = ba[pos];
        }
        fba = nba;
      }
      if(this.localBACache) {
        localBACacheDirty = false;
        localBA = fba;
        return localBA;
      } else {
        return fba;
      }
    }

  }

  @Override
  public ImageMode getMode() {
    return mode;
  }

  @Override
  public int getColors() {
    return mode.getColors();
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public Draw draw() {
    return draw;
  }

  private static class AWTDraw implements Draw {
    private final AWTImage ai;

    public AWTDraw(AWTImage ai) {
      this.ai = ai;
    }

    @Override
    public void floodFill(int x, int y, Color c, Color edge, boolean keepAlpha) {
      if(x < 0 || x >= ai.getWidth()) {
        return;
      }
      if(y <0 || y>=ai.getWidth()) {
        return;
      }

      if(edge == null) {
        ImageFillUtils.noEdgeLineFill(ai, x, y, c, keepAlpha);
      } else {
        ImageFillUtils.edgeCustomFill(ai, x, y, c, edge, keepAlpha);
      }
    }

    @Override
    public void fillColor(int x, int y, Color c) {
      floodFill(x, y, c, null, false);
    }

    @Override
    public void rect(int x, int y, int w, int h, Color c, int lineWidth, boolean fill) {
      Graphics2D graph = ai.bi.createGraphics();
      ai.localBACacheDirty = true;
      try{
        graph.setColor(AWTUtils.toAWTColor(c));
        graph.setStroke(new BasicStroke(lineWidth));
        int offset = (lineWidth/2);
        if(fill) {
          graph.fill(new Rectangle(x+offset, y+offset, w, h));
        } else {
          graph.drawRect(x+offset, y+offset, w-lineWidth, h-lineWidth);
        }
      } finally {
        graph.dispose();
      }
    }

    @Override
    public void circle(int cx, int cy, int size, Color c, int lineWidth, boolean fill) {
      Graphics2D graph = ai.bi.createGraphics();
      ai.localBACacheDirty = true;
      try {
        if(ai.smoothCircle) {
          graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
          graph.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
          graph.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
          graph.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
          graph.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
          graph.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        }
        graph.setColor(AWTUtils.toAWTColor(c));
        graph.setStroke(new BasicStroke(lineWidth));
        if(fill) {
          graph.fillOval(cx-(size/2), cy-(size/2), size, size);
        } else {
          graph.drawOval(cx-(size/2), cy-(size/2), size, size);
        }
      } finally {
        graph.dispose();
      }
    }

    @Override
    public void line(int startX, int startY, int endX, int endY, Color c, int lineWidth, boolean alphaMerge) {
      Graphics2D graph = ai.bi.createGraphics();
      ai.localBACacheDirty = true;
      try {
        java.awt.Color awt_c = AWTUtils.toAWTColor(c);
        if(!alphaMerge) {
          graph.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));  
        }
        graph.setColor(awt_c);
        graph.setStroke(new BasicStroke(lineWidth));
        graph.drawLine(startX, startY, endX, endY);
      } finally {
        graph.dispose();
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
    return "AWTImage: width:"+width+": height"+height+": mode:"+mode.toString();
  }

}

