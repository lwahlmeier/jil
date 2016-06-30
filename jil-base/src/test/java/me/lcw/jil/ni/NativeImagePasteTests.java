package me.lcw.jil.ni;

import java.io.IOException;

import org.junit.Before;

import me.lcw.jil.BaseImage;
import me.lcw.jil.Color;
import me.lcw.jil.ImageException;
import me.lcw.jil.JilImage;
import me.lcw.jil.NativeImage;
import me.lcw.jil.PasteTests;
import me.lcw.jil.TestUtils;

public class NativeImagePasteTests extends PasteTests {

  @Override
  @Before
  public void start() throws ImageException, IOException{
    img = NativeImage.fromJilImage(TestUtils.RGBImageGenerator());
    JilImage ji = TestUtils.RGBAImageGenerator().resize(300, 300, true, BaseImage.ScaleType.NN);
    subImg = NativeImage.fromJilImage(ji);
  }
  
  @Override
  public void biggerPasteImage() throws Exception {
    subImg2 = NativeImage.create(BaseImage.MODE.RGBA, 554, 371);
    subImg2.fillImageWithColor(Color.BLACK);
    super.biggerPasteImage();
  }
  
  @Override
  public void normal() throws Exception {
    //We add alpha value to make sure its not merged, but instead just over written
    addAlphaToImage((byte)100, subImg);
    System.out.println(subImg.getPixel(0, 0));
    super.normal();
  }
  
  @Override
  public void underShootY() throws Exception {
    super.underShootY();
  }
  
  @Override
  public void underShootX() throws Exception {
    super.underShootX();
  }
  
  public void overShootX() throws Exception {
    super.overShootX();
  }
  
  @Override
  public void overShootY() throws Exception {
    super.overShootY();
  }
  
  @Override
  public void overShootBoth() throws Exception {
    super.overShootBoth();
  }
  
  @Override
  public void underShootBoth() throws Exception {
    super.underShootBoth();
  }
}
