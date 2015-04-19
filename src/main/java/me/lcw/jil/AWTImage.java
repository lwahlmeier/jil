package me.lcw.jil;

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
import java.util.ArrayDeque;

import javax.imageio.ImageIO;

public class AWTImage implements Image{

  private final int width;
  private final int height;
  private final byte bpp;
  private final byte colors;
  private final BufferedImage bi;


  private AWTImage(byte mode, int width, int height) {
    colors = (byte) (mode/8);
    this.width = width;
    this.height = height;
    this.bpp = mode;
    if(mode == Image.MODE_L) {
      bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
    } else if(mode == Image.MODE_RGB) {
      bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
    } else {
      bi = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
    }
  }

  private AWTImage(byte mode, int width, int height, BufferedImage cbi) {
    colors = (byte) (mode/8);
    this.width = width;
    this.height = height;
    this.bpp = mode;
    bi = cbi;
  }

  private AWTImage(BufferedImage cbi, byte mode) {
    this(mode, cbi.getWidth(), cbi.getHeight(), cbi);
  }

  public static AWTImage create(byte mode, int width, int height) {
    return new AWTImage(mode, width, height);
  }

  public static AWTImage fromJavaImage(JilImage ji) {
    return new AWTImage(ji.getBPP(), ji.getWidth(), ji.getHeight(), ji.toBufferedImage());
  }

  public static AWTImage fromBufferedImage(BufferedImage bi) {
    switch(bi.getType()) {
    case BufferedImage.TYPE_BYTE_BINARY:
    case BufferedImage.TYPE_USHORT_GRAY: {
      BufferedImage nbi = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
      Graphics g = nbi.getGraphics();
      try {
        g.drawImage(bi, 0, 0, null);
      } finally {
        g.dispose();
      }
      return new AWTImage(Image.MODE_L, nbi.getWidth(), nbi.getHeight(), nbi);
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
        return new AWTImage(Image.MODE_RGB, nbi.getWidth(), nbi.getHeight(), nbi);
      } finally {
        g.dispose();
      }
    }

    case BufferedImage.TYPE_BYTE_GRAY:{
      return new AWTImage(Image.MODE_L, bi.getWidth(), bi.getHeight(), bi);
    }
    case BufferedImage.TYPE_3BYTE_BGR:{
      return new AWTImage(Image.MODE_RGB, bi.getWidth(), bi.getHeight(), bi);
    }
    case BufferedImage.TYPE_4BYTE_ABGR: {
      return new AWTImage(Image.MODE_RGBA, bi.getWidth(), bi.getHeight(), bi);
    }

    case BufferedImage.TYPE_4BYTE_ABGR_PRE:
    case BufferedImage.TYPE_INT_ARGB:
    case BufferedImage.TYPE_INT_ARGB_PRE:
    default: {
      BufferedImage nbi = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
      Graphics g = nbi.getGraphics();
      try {
        g.drawImage(bi, 0, 0, null);
        return new AWTImage(Image.MODE_RGBA, nbi.getWidth(), nbi.getHeight(), nbi);
      } finally {
        g.dispose();
      }
    }

    }
  }

  public static AWTImage open(String filename) throws ImageException, IOException {
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

  public static AWTImage open(String filename, ImageType type) throws IOException, ImageException {
    switch(type) {
    case TIFF:
      return AWTImage.fromJavaImage(TiffFile.open(filename));
    case PNG:
    case JPEG:
      return AWTImage.fromBufferedImage(ImageIO.read(new File(filename)));
    default:
      throw new ImageException("Could not determen filetype");
    }
  }

  public BufferedImage getBufferedImage() {
    return bi;
  }

  @Override
  public void save(File file) throws IOException, ImageException {
    ImageType t = Utils.getImageType(file);
    save(file, t);
  }

  @Override
  public void save(String file) throws IOException, ImageException {
    ImageType t = Utils.getImageType(file);
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
      TiffFile.save(filename, this);
    } break;
    default:
      break;
    }
  }


  @Override
  public JilImage toJilImage() {
    return JilImage.fromBufferedImage(bi);
  }

  @Override
  public AWTImage changeMode(byte MODE) {
    BufferedImage nbi;
    if(MODE == Image.MODE_L) {
      nbi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
    } else if(MODE == Image.MODE_RGB) {
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
    return new AWTImage(nbi, MODE);
  }

  @Override
  public AWTImage resizeWithBorders(int bWidth, int bHeight, Color borderColor,
      ScaleType st) {
    //TOOD: no reason we cant so this with AWT.
    throw new UnsupportedOperationException();
  }

  @Override
  public AWTImage resize(int newWidth, int newHeight, boolean keepAspect, ScaleType st) {
    if(keepAspect) {
      int[] aspect = Utils.getAspectSize(this.width, this.height, newWidth, newHeight);
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
        nimg = new AWTImage(resizedImage, bpp);
      } break;      
      case CUBIC:{
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawImage(bi, 0, 0, newWidth, newHeight, null);
        nimg = new AWTImage(resizedImage, bpp);
      } break;
      case CUBIC_SMOOTH:
        nimg = (AWTImage)Utils.biCubicSmooth(this, newWidth, newHeight);
      default:{
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g.drawImage(bi, 0, 0, newWidth, newHeight, null);
        nimg = new AWTImage(resizedImage, bpp);
      }break;
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
        graphics.setPaint(Utils.toAWTColor(c));
        graphics.fillRect(0, 0, width, height);
      } finally {
        graphics.dispose();
      }
    }

    @Override
    public void setPixel(int x, int y, Color c) {
      bi.setRGB(x, y, Utils.toAWTColor(c).getRGB());
    }

    @Override
    public Color getPixel(int x, int y) {
      return Utils.toJILColor(new java.awt.Color(bi.getRGB(x, y)));
    }

    @Override
    public void paste(int x, int y, Image img) {
      BufferedImage bi2;
      if(img instanceof AWTImage) {
        bi2 = ((AWTImage)img).bi;
      } else {
        bi2 = AWTImage.fromJavaImage(img.toJilImage()).bi;
      }
      Graphics2D gi = bi.createGraphics();
      try{
        float opacity = 1.0f;
        gi.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, opacity));
        gi.drawImage(bi2, x, y, null);
      } finally {
        gi.dispose();
      }
    }

    @Override
    public void merge(int x, int y, Image img) {
      BufferedImage bi2;
      if(img instanceof AWTImage) {
        bi2 = ((AWTImage)img).bi;
      } else {
        bi2 = AWTImage.fromJavaImage(img.toJilImage()).bi;
      }
      Graphics2D gi = bi.createGraphics();
      try {
        float opacity = 1.0f;
        gi.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        gi.drawImage(bi2, x, y, null);
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
        return new AWTImage(b, this.bpp);
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
        return new AWTImage(bi2, this.bpp);
      } finally {
        gi2.dispose();
      }

    }

    @Override
    public byte[] getArray() {
      //TODO: need to figure out if there are better ways to do this??
      if(bi.getType() == BufferedImage.TYPE_BYTE_GRAY) {
        return ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
      } else if (bi.getType() == BufferedImage.TYPE_3BYTE_BGR){
        byte[] ba = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        byte[] nba = new byte[ba.length];
        for(int i=0; i<ba.length/3; i++) {
          int pos = i*3;
          nba[pos] = ba[pos+2];
          nba[pos+1] = ba[pos+1];
          nba[pos+2] = ba[pos];
        }
        return nba;
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
        return nba;
      }
    }

    @Override
    public byte getBPP() {
      return bpp;
    }

    @Override
    public byte getColors() {
      return colors;
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
    public AWTDraw getImageDrawer() {
      return new AWTDraw(this);
    }

    public static class AWTDraw implements Draw {
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
        Integer[] ce = new Integer[] {x, y};
        ArrayDeque<Integer[]> pl = new ArrayDeque<Integer[]>();
        pl.add(ce);
        if(edge == null) {
          Color OC = ai.getPixel(x, y);
          if(OC.equals(c)) {
            return;
          }
          while(pl.size() > 0) {
            ce = pl.poll();
            Color tmpC = ai.getPixel(ce[0], ce[1]);
            if(tmpC!=null && tmpC.equalsNoAlpha(OC)) {
              Color nc = c;
              if(keepAlpha) {
                nc = c.changeAlpha(tmpC.getAlpha());
              }
              ai.setPixel(ce[0], ce[1], nc);
              if(ce[0]+1 < ai.getWidth()) {
                pl.add(new Integer[]{ce[0]+1, ce[1]});
              }
              if(ce[0]-1 >= 0) {
                pl.add(new Integer[]{ce[0]-1, ce[1]});
              }
              if(ce[1]+1 < ai.getHeight()) {
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
            Color tmpC = ai.getPixel(ce[0], ce[1]);
            Color nc = c;
            if(keepAlpha) {
              nc = c.changeAlpha(tmpC.getAlpha());
            }
            if(!tmpC.equals(edge) && !tmpC.equals(nc)) {
              ai.setPixel(ce[0], ce[1], nc);
              if(ce[0]+1 < ai.getWidth()) {
                pl.add(new Integer[]{ce[0]+1, ce[1]});
              }
              if(ce[0] -1 >= 0) {
                pl.add(new Integer[]{ce[0]-1, ce[1]});
              }
              if(ce[1]+1 < ai.getHeight()) {
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
        floodFill(x, y, c, null, false);
      }

      @Override
      public void drawRect(int x, int y, int w, int h, Color c, int lineWidth, boolean fill) {
        Graphics2D graph = ai.bi.createGraphics();
        try{
          graph.setColor(Utils.toAWTColor(c));
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
      public void drawCircle(int cx, int cy, int size, Color c, int lineWidth, boolean fill) {
        Graphics2D graph = ai.bi.createGraphics();
        try {
          graph.setColor(Utils.toAWTColor(c));
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
      public void drawLine(int startX, int startY, int endX, int endY, Color c, int lineWidth, boolean alphaMerge) {
        Graphics2D graph = ai.bi.createGraphics();
        try {
          java.awt.Color awt_c = Utils.toAWTColor(c);
          if(!alphaMerge) {
            awt_c = new java.awt.Color(c.getRed()&0xff, c.getGreen()&0xff, c.getBlue()&0xff); 
          }
          graph.setColor(awt_c);
          graph.setStroke(new BasicStroke(lineWidth));
          graph.drawLine(startX, startY, endX, endY);
        } finally {
          graph.dispose();
        }
      }

    }
  }
