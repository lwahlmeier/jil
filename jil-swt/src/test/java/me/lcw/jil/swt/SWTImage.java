package me.lcw.jil.swt;

import java.io.File;
import java.io.IOException;

import me.lcw.jil.BaseImage;
import me.lcw.jil.Color;
import me.lcw.jil.Draw;
import me.lcw.jil.JilImage;
import me.lcw.jil.BaseImage.MODE;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;

public class SWTImage implements BaseImage {
  
  private final int width;
  private final int height;
  private final MODE mode;
  private final ImageData img;
  
  private SWTImage(MODE mode, int width, int height) {
    this.width = width;
    this.height = height;
    this.mode = mode;
    if(mode == MODE.RGBA) {
      PaletteData palette = new PaletteData(0xFF , 0xFF00 , 0xFF0000);
      img = new ImageData(width, height, 24, palette);
    } else if(mode == MODE.RGB) {
      PaletteData palette = new PaletteData(0xFF , 0xFF00 , 0xFF0000);
      img = new ImageData(width, height, 24, palette);      
    } else {
//      PaletteData palette = new PaletteData(0xFF);
//      img = new ImageData(width, height, 8, );
      img = null;
    }
    
  }

  @Override
  public void save(String filename) throws IOException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void save(File file) throws IOException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void save(File file, ImageType type) throws IOException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void save(String filename, ImageType type) throws IOException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public JilImage toJilImage() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public BaseImage changeMode(MODE mode) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public BaseImage resizeWithBorders(int bWidth, int bHeight,
      Color borderColor, ScaleType scaleType) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public BaseImage resize(int newWidth, int newHeight, boolean keepAspect,
      ScaleType scaleType) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void fillImageWithColor(Color color) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void setPixel(int x, int y, Color c) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public Color getPixel(int x, int y) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void paste(int x, int y, BaseImage img) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void merge(int x, int y, BaseImage img) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public BaseImage copy() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public BaseImage cut(int x, int y, int width, int height) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public byte[] getArray() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MODE getMode() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getColors() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getWidth() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getHeight() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public Draw draw() {
    // TODO Auto-generated method stub
    return null;
  }

}
