package me.lcw.jil;

import java.io.IOException;
import java.io.RandomAccessFile;


//import ar.com.hjg.pngj.ImageLineByte;
//import ar.com.hjg.pngj.PngReaderByte;

public class Try {
//
//  //@Test
//  public void tmp2() {
//    Color bc = Color.BLACK.changeAlpha((byte)100);
//    Color c = Color.RED.changeAlpha((byte) 200);
//    System.out.println(bc);
//    System.out.println(c);
//    Color nc1 = Utils.mergeColors(c, bc);
//    System.out.println(nc1);
//  }
//  
//  //@Test
//  public void test1() throws ImageException, IOException {
//    AWTImage img = AWTImage.create(BaseImage.MODE.RGBA, 1920, 1080);
//    img.fillImageWithColor(Color.ALPHA);
//    Color c = Color.RED.changeAlpha((byte) 50);
//    img.getImageDrawer().drawRect(0, 0, 160, 1080, c, 1, true);
//    c = Color.RED.changeAlpha((byte) 100);
//    img.getImageDrawer().drawRect(160, 0, 160, 1080, c, 1, true);
//    c = Color.RED.changeAlpha((byte) 200);
//    img.getImageDrawer().drawRect(160*2, 0, 160, 1080, c, 1, true);
//    img.getImageDrawer().drawRect(160*3, 0, 160, 1080, Color.RED, 1, true);
//    
//    c = Color.GREEN.changeAlpha((byte) 50);
//    img.getImageDrawer().drawRect(160*4, 0, 160, 1080, c, 1, true);
//    c = Color.GREEN.changeAlpha((byte) 100);
//    img.getImageDrawer().drawRect(160*5, 0, 160, 1080, c, 1, true);
//    c = Color.GREEN.changeAlpha((byte) 200);
//    img.getImageDrawer().drawRect(160*6, 0, 160, 1080, c, 1, true);
//    img.getImageDrawer().drawRect(160*7, 0, 160, 1080, Color.GREEN, 1, true);
//    
//    c = Color.BLUE.changeAlpha((byte) 50);
//    img.getImageDrawer().drawRect(160*8, 0, 160, 1080, c, 1, true);
//    c = Color.BLUE.changeAlpha((byte) 100);
//    img.getImageDrawer().drawRect(160*9, 0, 160, 1080, c, 1, true);
//    c = Color.BLUE.changeAlpha((byte) 200);
//    img.getImageDrawer().drawRect(160*10, 0, 160, 1080, c, 1, true);
//    img.getImageDrawer().drawRect(160*11, 0, 160, 1080, Color.BLUE, 1, true);
//    RandomAccessFile raf = new RandomAccessFile("/tmp/testImage.raw", "rw");
//    raf.write(img.getArray());
//    raf.close();
//    img.save("/tmp/test.png");
//  }
//  
//  //@Test
//  public void RGBImageGenerator() throws ImageException, IOException {
//    AWTImage img = AWTImage.create(BaseImage.MODE.RGB, 1920, 1080);
//    for(int i=0; i<1080; i++) {
//      double pct = i/1080.0;
//      byte r = (byte)(0xff * (pct*2));
//      byte g = (byte)(0xff* (pct*1));
//      byte b = (byte)(0xff -(0xff* (pct*2)));
//      Color c = new Color(r,g,b);
//      img.getImageDrawer().drawLine(0, i, 1920, i, c, 0, false);
//    }
//    img.save("/tmp/test.png");
//  }
//  
//  //@Test
//  public void GreyImageGenerator() throws ImageException, IOException {
//    AWTImage img = AWTImage.create(BaseImage.MODE.GREY, 1920, 1080);
//    for(int i=0; i<1080; i++) {
//      double pct = i/1080.0;
//      byte tmp = (byte)(256*pct);
//      img.getImageDrawer().drawLine(0, i, 1920, i, new Color(tmp), 0, false);
//    }
//    img.save("/tmp/test.png");
//  }
//  
//  //@Test
//  public void RGBAImageGenerator() throws ImageException, IOException {
//    JilImage img = JilImage.create(BaseImage.MODE.RGBA, 1920, 1080);
//    img.fillImageWithColor(Color.ALPHA);
//    Color c = Color.RED.changeAlpha((byte) 50);
//    img.getImageDrawer().drawRect(0, 0, 1920, 90, c, 1, true);
//    c = Color.RED.changeAlpha((byte) 100);
//    img.getImageDrawer().drawRect(0, 90, 1920, 90, c, 1, true);
//    c = Color.RED.changeAlpha((byte) 200);
//    img.getImageDrawer().drawRect(0, 90*2, 1920, 90, c, 1, true);
//    img.getImageDrawer().drawRect(0, 90*3, 1920, 90, Color.RED, 1, true);
//    
//    c = Color.GREEN.changeAlpha((byte) 50);
//    img.getImageDrawer().drawRect(0, 90*4, 1920, 90, c, 1, true);
//    c = Color.GREEN.changeAlpha((byte) 100);
//    img.getImageDrawer().drawRect(0, 90*5, 1920, 90, c, 1, true);
//    c = Color.GREEN.changeAlpha((byte) 200);
//    img.getImageDrawer().drawRect(0, 90*6, 1920, 90, c, 1, true);
//    img.getImageDrawer().drawRect(0, 90*7, 1920, 90, Color.GREEN, 1, true);
//    
//    c = Color.BLUE.changeAlpha((byte) 50);
//    img.getImageDrawer().drawRect(0, 90*8, 1920, 90, c, 1, true);
//    c = Color.BLUE.changeAlpha((byte) 100);
//    img.getImageDrawer().drawRect(0, 90*9, 1920, 90, c, 1, true);
//    c = Color.BLUE.changeAlpha((byte) 200);
//    img.getImageDrawer().drawRect(0, 90*10, 1920, 90, c, 1, true);
//    img.getImageDrawer().drawRect(0, 90*11, 1920, 90, Color.BLUE, 1, true);
//    AWTImage.fromBaseImage(img).save("/tmp/test.png");
//  }
}
