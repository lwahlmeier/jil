package me.lcw.jil.ji;

import java.io.IOException;

import me.lcw.jil.BaseImage;
import me.lcw.jil.Color;
import me.lcw.jil.ImageException;
import me.lcw.jil.JilImage;
import me.lcw.jil.PasteTests;
import me.lcw.jil.TestUtils;

import org.junit.Before;

public class AlphaNoMergePasteTests extends PasteTests {

  @Override
  @Before
  public void start() throws ImageException, IOException{
    img = TestUtils.RGBImageGenerator();
    subImg = TestUtils.RGBAImageGenerator().resize(300, 300, true, BaseImage.ScaleType.NN);
  }
  
  @Override
  public void biggerPasteImage() throws Exception {
    subImg2 = JilImage.create(BaseImage.ImageMode.RGBA32, 554, 371);
    subImg2.fillImageWithColor(Color.BLACK);
    super.biggerPasteImage();
  }
  
  @Override
  public void normal() throws Exception {
    //We add alpha value to make sure its not merged, but instead just over written
    addAlphaToImage((byte)100, subImg);
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
