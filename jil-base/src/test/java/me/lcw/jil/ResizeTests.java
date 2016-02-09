package me.lcw.jil;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public abstract class ResizeTests {
  public static final boolean saveFiles = false;
  public BaseImage imgRGBA;
  public BaseImage imgRGB;
  public BaseImage imgL;
  public BaseImage subImgRGBA;
  public BaseImage subImgRGB;
  public BaseImage subImgL;
  public BaseImage.ScaleType scaleType;
  
  @Before
  public abstract void start() throws Exception;
  
  @After
  public void end() {
    imgRGBA = null;
    imgRGB = null;
    imgL = null;
    subImgRGBA = null;
    subImgRGB = null;
    subImgL = null;
    System.gc();
  }
  
  @Test
  public void NoAspectScaleUp() throws Exception {
    subImgRGBA = imgRGBA.resize((int)(imgRGBA.getWidth()*2.55), (int)(imgRGBA.getHeight()*4.55), false, scaleType);
    subImgRGB = imgRGB.resize((int)(imgRGB.getWidth()*2.55), (int)(imgRGB.getHeight()*4.55), false, scaleType);
    subImgL = imgL.resize((int)(imgL.getWidth()*2.55), (int)(imgL.getHeight()*4.55), false, scaleType);
    if(saveFiles) {
      class Local {};
      String name = Local.class.getEnclosingMethod().getName();
      subImgRGBA.save(new File("/tmp/"+name+"-RGBA.png"));
      subImgRGB.save(new File("/tmp/"+name+"-RGB.png"));
      subImgL.save(new File("/tmp/"+name+"-L.png"));
    }
  }
  
  @Test
  public void NoAspectScaleDown() throws Exception {
    subImgRGBA = imgRGBA.resize((int)(imgRGBA.getWidth()/2.55), (int)(imgRGBA.getHeight()/4.55), false, scaleType);
    subImgRGB = imgRGB.resize((int)(imgRGB.getWidth()/2.55), (int)(imgRGB.getHeight()/4.55), false, scaleType);
    subImgL = imgL.resize((int)(imgL.getWidth()/2.55), (int)(imgL.getHeight()/4.55), false, scaleType);
    if(saveFiles) {
      class Local {};
      String name = Local.class.getEnclosingMethod().getName();
      subImgRGBA.save(new File("/tmp/"+name+"-RGBA.png"));
      subImgRGB.save(new File("/tmp/"+name+"-RGB.png"));
      subImgL.save(new File("/tmp/"+name+"-L.png"));
    }
  }
  
  @Test
  public void NoAspectUpHeight() throws Exception {
    subImgRGBA = imgRGBA.resize((int)(imgRGBA.getWidth()/2.55), (int)(imgRGBA.getHeight()*4.55), false, scaleType);
    subImgRGB = imgRGB.resize((int)(imgRGB.getWidth()/2.55), (int)(imgRGB.getHeight()*4.55), false, scaleType);
    subImgL = imgL.resize((int)(imgL.getWidth()/2.55), (int)(imgL.getHeight()*4.55), false, scaleType);
    if(saveFiles) {
      class Local {};
      String name = Local.class.getEnclosingMethod().getName();
      subImgRGBA.save(new File("/tmp/"+name+"-RGBA.png"));
      subImgRGB.save(new File("/tmp/"+name+"-RGB.png"));
      subImgL.save(new File("/tmp/"+name+"-L.png"));
    }
  }
  
  @Test
  public void NoAspectUpWidth() throws Exception {
    subImgRGBA = imgRGBA.resize((int)(imgRGBA.getWidth()*2.55), (int)(imgRGBA.getHeight()/4.55), false, scaleType);
    subImgRGB = imgRGB.resize((int)(imgRGB.getWidth()*2.55), (int)(imgRGB.getHeight()/4.55), false, scaleType);
    subImgL = imgL.resize((int)(imgL.getWidth()*2.55), (int)(imgL.getHeight()/4.55), false, scaleType);
    if(saveFiles) {
      class Local {};
      String name = Local.class.getEnclosingMethod().getName();
      subImgRGBA.save(new File("/tmp/"+name+"-RGBA.png"));
      subImgRGB.save(new File("/tmp/"+name+"-RGB.png"));
      subImgL.save(new File("/tmp/"+name+"-L.png"));
    }
  }
  
  @Test
  public void AspectScaleUp() throws Exception {
    subImgRGBA = imgRGBA.resize((int)(imgRGBA.getWidth()*2.55), (int)(imgRGBA.getHeight()*4.55), true, scaleType);
    subImgRGB = imgRGB.resize((int)(imgRGB.getWidth()*2.55), (int)(imgRGB.getHeight()*4.55), true, scaleType);
    subImgL = imgL.resize((int)(imgL.getWidth()*2.55), (int)(imgL.getHeight()*4.55), true, scaleType);
    if(saveFiles) {
      class Local {};
      String name = Local.class.getEnclosingMethod().getName();
      subImgRGBA.save(new File("/tmp/"+name+"-RGBA.png"));
      subImgRGB.save(new File("/tmp/"+name+"-RGB.png"));
      subImgL.save(new File("/tmp/"+name+"-L.png"));
    }
    double aspect = imgRGBA.getWidth()/imgRGBA.getHeight();
    double aspect2 = subImgRGBA.getWidth()/subImgRGBA.getHeight();
    assertTrue(aspect == aspect2);
    aspect = imgRGB.getWidth()/imgRGB.getHeight();
    aspect2 = subImgRGB.getWidth()/subImgRGB.getHeight();
    assertTrue(aspect == aspect2);
    aspect = imgL.getWidth()/imgL.getHeight();
    aspect2 = subImgL.getWidth()/subImgL.getHeight();
    assertTrue(aspect == aspect2);
    
  }
  
  @Test
  public void AspectScaleDown() throws Exception {
    subImgRGBA = imgRGBA.resize((int)(imgRGBA.getWidth()/2.55), (int)(imgRGBA.getHeight()/4.55), true, scaleType);
    subImgRGB = imgRGB.resize((int)(imgRGB.getWidth()/2.55), (int)(imgRGB.getHeight()/4.55), true, scaleType);
    subImgL = imgL.resize((int)(imgL.getWidth()/2.55), (int)(imgL.getHeight()/4.55), true, scaleType);
    if(saveFiles) {
      class Local {};
      String name = Local.class.getEnclosingMethod().getName();
      subImgRGBA.save(new File("/tmp/"+name+"-RGBA.png"));
      subImgRGB.save(new File("/tmp/"+name+"-RGB.png"));
      subImgL.save(new File("/tmp/"+name+"-L.png"));
    }
    double aspect = imgRGBA.getWidth()/imgRGBA.getHeight();
    double aspect2 = subImgRGBA.getWidth()/subImgRGBA.getHeight();
    assertTrue(aspect == aspect2);
    aspect = imgRGB.getWidth()/imgRGB.getHeight();
    aspect2 = subImgRGB.getWidth()/subImgRGB.getHeight();
    assertTrue(aspect == aspect2);
    aspect = imgL.getWidth()/imgL.getHeight();
    aspect2 = subImgL.getWidth()/subImgL.getHeight();
    assertTrue(aspect == aspect2);
  }
  
  @Test
  public void AspectUpHeight() throws Exception {
    subImgRGBA = imgRGBA.resize((int)(imgRGBA.getWidth()/2.55), (int)(imgRGBA.getHeight()*4.55), true, scaleType);
    subImgRGB = imgRGB.resize((int)(imgRGB.getWidth()/2.55), (int)(imgRGB.getHeight()*4.55), true, scaleType);
    subImgL = imgL.resize((int)(imgL.getWidth()/2.55), (int)(imgL.getHeight()*4.55), true, scaleType);
    if(saveFiles) {
      class Local {};
      String name = Local.class.getEnclosingMethod().getName();
      subImgRGBA.save(new File("/tmp/"+name+"-RGBA.png"));
      subImgRGB.save(new File("/tmp/"+name+"-RGB.png"));
      subImgL.save(new File("/tmp/"+name+"-L.png"));
    }
    double aspect = imgRGBA.getWidth()/imgRGBA.getHeight();
    double aspect2 = subImgRGBA.getWidth()/subImgRGBA.getHeight();
    assertTrue(aspect == aspect2);
    aspect = imgRGB.getWidth()/imgRGB.getHeight();
    aspect2 = subImgRGB.getWidth()/subImgRGB.getHeight();
    assertTrue(aspect == aspect2);
    aspect = imgL.getWidth()/imgL.getHeight();
    aspect2 = subImgL.getWidth()/subImgL.getHeight();
    assertTrue(aspect == aspect2);
  }
  
  @Test
  public void AspectUpWidth() throws Exception {
    subImgRGBA = imgRGBA.resize((int)(imgRGBA.getWidth()*2.55), (int)(imgRGBA.getHeight()/4.55), true, scaleType);
    subImgRGB = imgRGB.resize((int)(imgRGB.getWidth()*2.55), (int)(imgRGB.getHeight()/4.55), true, scaleType);
    subImgL = imgL.resize((int)(imgL.getWidth()*2.55), (int)(imgL.getHeight()/4.55), true, scaleType);
    if(saveFiles) {
      class Local {};
      String name = Local.class.getEnclosingMethod().getName();
      subImgRGBA.save(new File("/tmp/"+name+"-RGBA.png"));
      subImgRGB.save(new File("/tmp/"+name+"-RGB.png"));
      subImgL.save(new File("/tmp/"+name+"-L.png"));
    }
    double aspect = imgRGBA.getWidth()/imgRGBA.getHeight();
    double aspect2 = subImgRGBA.getWidth()/subImgRGBA.getHeight();
    assertTrue(aspect == aspect2);
    aspect = imgRGB.getWidth()/imgRGB.getHeight();
    aspect2 = subImgRGB.getWidth()/subImgRGB.getHeight();
    assertTrue(aspect == aspect2);
    aspect = imgL.getWidth()/imgL.getHeight();
    aspect2 = subImgL.getWidth()/subImgL.getHeight();
    assertTrue(aspect == aspect2);
  }
}
