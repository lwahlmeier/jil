package me.lcw.jil.nat;

import java.io.File;
import java.io.IOException;

import me.lcw.jil.BaseImage;
import me.lcw.jil.Color;
import me.lcw.jil.Draw;
import me.lcw.jil.JilImage;
import me.lcw.jil.Utils.JilUtils;

public class NativeImage implements BaseImage {
  
  private static native long createImage(int channels, int width, int height);
  private static native void imageFillColor(long ptr, Color color); 
  private static native void imageSetPixel(long ptr, int x, int y, Color c);
  private static native byte[] imageGetPixel(long ptr, int x, int y);
  private static native void pasteNativeImage(long ptr, long rptr, int x, int y);
  private static native void pasteBaseImage(long ptr, BaseImage img, int x, int y);
  private static native void mergeNativeImage(long ptr, long rptr, int x, int y);
  private static native void mergeBaseImage(long ptr, BaseImage img, int x, int y);
  private static native long copyImage(long ptr);
  private static native long cutImage(long ptr, int x, int y, int width, int height);
  private static native byte[] imageGetByteArray(long ptr);
  private static native long imageChangeMode(long ptr, int channels);
  private static native long imageResize(long ptr, int width, int height, int scaleType);
  private static native long imageDelete(long ptr);
  
  private final long nativePointer;
  private final MODE mode;
  private final int width;
  private final int height;
  
  private NativeImage(long pntr, MODE mode, int width, int height) {
    this.nativePointer = pntr;
    this.mode = mode;
    this.width = width;
    this.height = height;
  }
  
  private NativeImage(MODE mode, int width, int height) {
    nativePointer = createImage(mode.getColors(), width, height);
    this.mode = mode;
    this.width = width;
    this.height = height;
  }
  
  public static NativeImage create(MODE mode, int width, int height, Color c) {
    NativeImage ni = create(mode, width, height);
    ni.fillImageWithColor(c);
    return ni;
  }
  
  public static NativeImage create(MODE mode, int width, int height) {
    NativeImage ni = new NativeImage(mode, width, height);
    return ni;
  }

  @Override
  public void save(String filename) throws IOException {
    
  }
  
  @Override
  public void save(File file) throws IOException {
    
  }

  @Override
  public void save(File file, ImageType type) throws IOException {
    
  }

  @Override
  public void save(String filename, ImageType type) throws IOException {
    
  }

  @Override
  public JilImage toJilImage() {
    return JilImage.fromByteArray(mode, width, height, getArray());
  }

  @Override
  public NativeImage changeMode(MODE nmode) {
    if(nmode == mode) {
      return copy();
    } else {
      return new NativeImage(imageChangeMode(nativePointer, nmode.getColors()), nmode, width, height);
    }
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
  public NativeImage resize(int newWidth, int newHeight, boolean keepAspect, ScaleType scaleType) {
    if(keepAspect) {
      int[] aspect = JilUtils.getAspectSize(this.width, this.height, newWidth, newHeight);
      newWidth = aspect[0];
      newHeight = aspect[1];
    }
    return new NativeImage(imageResize(nativePointer, newWidth, newHeight, scaleType.ordinal()), mode, newWidth, newHeight);
  }

  @Override
  public void fillImageWithColor(Color color) {
    imageFillColor(nativePointer, color);
  }

  @Override
  public void setPixel(int x, int y, Color c) {
    imageSetPixel(this.nativePointer, x, y, c);
  }

  @Override
  public Color getPixel(int x, int y) {
    byte[] color = imageGetPixel(this.nativePointer, x, y);
    if(mode == MODE.RGBA) {
      return Color.fromRGBAValue(color[0],color[1],color[2],color[3]);
    } else if(mode == MODE.RGB) {
      return Color.fromRGBValue(color[0],color[1],color[2]);
    } else {
      return Color.fromGreyValue(color[0]);
    }
  }

  @Override
  public void paste(int x, int y, BaseImage img) {
    if(img instanceof NativeImage) {
      pasteNativeImage(nativePointer, ((NativeImage)img).nativePointer, x, y);
    } else {
      pasteBaseImage(nativePointer, img, x, y);
    }
  }

  @Override
  public void merge(int x, int y, BaseImage img) {
    if(img instanceof NativeImage) {
      mergeNativeImage(nativePointer, ((NativeImage)img).nativePointer, x, y);
    } else {
      mergeBaseImage(nativePointer, img, x, y);
    }
  }

  @Override
  public NativeImage copy() {
    return new NativeImage(copyImage(nativePointer), mode, width, height);
  }

  @Override
  public NativeImage cut(int x, int y, int width, int height) {
    return new NativeImage(cutImage(nativePointer, x, y, width, height), mode, width, height);
  }

  @Override
  public byte[] getArray() {
    return imageGetByteArray(nativePointer);
  }

  @Override
  public MODE getMode() {
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
  public void finalize() throws Throwable {
    imageDelete(nativePointer);
  }

  @Override
  public Draw draw() {
    // TODO Auto-generated method stub
    return null;
  }
  

}
