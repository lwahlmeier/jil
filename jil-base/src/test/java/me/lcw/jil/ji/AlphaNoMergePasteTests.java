package me.lcw.jil.ji;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import me.lcw.jil.Color;
import me.lcw.jil.BaseImage;
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
    subImg = TestUtils.RGBAImageGenerator();
    subImg = subImg.changeMode(BaseImage.MODE.RGBA);
    subImg = subImg.resize(300, 300, true, BaseImage.ScaleType.NN);
  }
  
  @Override
  public void biggerPasteImage() throws Exception {
    subImg2 = JilImage.create(BaseImage.MODE.RGBA, 554, 371);
    subImg2.fillImageWithColor(Color.BLACK);
    super.biggerPasteImage();
    assertEquals("8f0b42f2510072bccf187838811a60c69c835427dbad481ec3d81a1d1547bb91", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void normal() throws Exception {
    //We add alpha value to make sure its not merged, but instead just over written
    addAlphaToImage((byte)100, subImg);
    super.normal();
    assertEquals("28fdc4a5c457dc88d76e3285cc89f73514fdccc5926bce23b15b5c3c8f1a075d", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void underShootY() throws Exception {
    super.underShootY();
    
    assertEquals("319a27a933dee4637afad30a0001490f0bf4f461a00bbc5546a0ef60e3278c9e", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void underShootX() throws Exception {
    super.underShootX();
    assertEquals("559a6fce0ef7868b47b222db8dd460a9fe6bfcd60f14d72918eec60476924669", TestUtils.hashByteArray(img.getArray()));
  }
  
  public void overShootX() throws Exception {
    super.overShootX();
    assertEquals("939ae155c8fcd4ad7f90534049e3a13e79bc17db8cfec29df3244bf97c46b8d2", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void overShootY() throws Exception {
    super.overShootY();
    assertEquals("2266a51b0c069151f61d5b55e522039b1730c83a4916d06f0cf818e69710a56a", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void overShootBoth() throws Exception {
    super.overShootBoth();

    assertEquals("84a4ec2dc88fe3a247970002dd0bd50384b30e39da992e89d743901c92d096e9", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void underShootBoth() throws Exception {
    super.underShootBoth();
    assertEquals("c401aee186d6ff5b2032e0da9bf2270ef07d0c30f80fc6a78caac3c1cd4c2643", TestUtils.hashByteArray(img.getArray()));
  }
}
