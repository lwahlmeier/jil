package org.java_lcw.jil.ji;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.java_lcw.jil.Color;
import org.java_lcw.jil.Image;
import org.java_lcw.jil.JilImage;
import org.java_lcw.jil.ImageException;
import org.java_lcw.jil.PasteTests;
import org.java_lcw.jil.TestUtils;
import org.junit.Before;

public class AlphaWithMergePasteTests extends PasteTests {

  @Override
  @Before
  public void start() throws ImageException, IOException{
    alpha = true;
    img = JilImage.open(filename);
    subImg = JilImage.open(filename2);
    img = img.changeMode(Image.MODE_RGBA);
    subImg = subImg.changeMode(Image.MODE_RGBA);
    subImg = subImg.resize(300, 300, true, Image.ScaleType.NN);
    addAlphaToImage((byte)100, subImg);

  }
  
  @Override
  public void biggerPasteImage() throws Exception {
    subImg2 = JilImage.create(Image.MODE_RGBA, 4304, 4024);
    subImg2.fillImageWithColor(Color.BLACK);
    addAlphaToImage((byte)100, subImg2);
    super.biggerPasteImage();
    assertEquals("6fcad6c545c1c4726dd048edfde9651541ec63d296e310da77f7a0a0e1babf2d", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void normal() throws Exception {
    super.normal();
    assertEquals("e0e13ce55f469797062fcd0ed251991e660974a91068c27352cd49db73cec6d6", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void underShootY() throws Exception {
    super.underShootY();
    assertEquals("2cf7af7d5ba53e4de111bea2e51df4f1754922fad39c14dcb5c7ac3458c75862", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void underShootX() throws Exception {
    super.underShootX();
    assertEquals("1086b43ebda4fd5be8489838bce38b08b359a97db2a035d2b2b0ff5ac057d114", TestUtils.hashByteArray(img.getArray()));
  }
  
  public void overShootX() throws Exception {
    super.overShootX();
    assertEquals("4fb1f7cf1762de128d2adeb164cf8f5b6fbdbcea8fb8335cb70e6194fbd89c3b", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void overShootY() throws Exception {
    super.overShootY();
    assertEquals("0e04c103ee605c87515fe4d9f15ae689ddb44b6dd2cdddf5b40ad4d09249c78f", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void overShootBoth() throws Exception {
    super.overShootBoth();
    assertEquals("e9d0d3dca152fcc6d4f89332143ce0a4575fbdf212238ba9286e4b17121393ac", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void underShootBoth() throws Exception {
    super.underShootBoth();
    assertEquals("4db7ea410bbcbf6beeae549f24c85e0a49a23dee1ed1822835b59c40247044c3", TestUtils.hashByteArray(img.getArray()));
  }
}
