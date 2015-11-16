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

public class AlphaWithMergePasteTests extends PasteTests {

  @Override
  @Before
  public void start() throws ImageException, IOException{
    alpha = true;
    img = TestUtils.RGBAImageGenerator();
    subImg = TestUtils.RGBImageGenerator();
    img = img.changeMode(BaseImage.MODE.RGBA);
    subImg = subImg.changeMode(BaseImage.MODE.RGBA);
    subImg = subImg.resize(300, 300, true, BaseImage.ScaleType.NN);
    addAlphaToImage((byte)100, subImg);

  }
  
  @Override
  public void biggerPasteImage() throws Exception {
    subImg2 = JilImage.create(BaseImage.MODE.RGBA, 554, 371);
    subImg2.fillImageWithColor(Color.BLACK);
    addAlphaToImage((byte)100, subImg2);
    //AWTImage.fromBaseImage((JilImage)img).save("/tmp/test2.png");
    super.biggerPasteImage();
    //AWTImage.fromBaseImage((JilImage)img).save("/tmp/test.png");
    assertEquals("6fcad6c545c1c4726dd048edfde9651541ec63d296e310da77f7a0a0e1babf2d", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void normal() throws Exception {
    super.normal();
    assertEquals("330cbda75830353c813d4c0c07352976ba8c22c09106923c872a53602b495e20", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void underShootY() throws Exception {
    super.underShootY();
    assertEquals("2a29a707ded858b9135d4fc17386f1c6ce216dfc9a2cdd80bdd3b1170ebb3232", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void underShootX() throws Exception {
    super.underShootX();
    assertEquals("3c1d6ddd0e459982a5996b195546738638de6105a0392ab13d592c37fe16219c", TestUtils.hashByteArray(img.getArray()));
  }
  
  public void overShootX() throws Exception {
    super.overShootX();
    assertEquals("0ab5928bb6aadf7a8994c39d982d4df786a8b40d4e311902c6bd71e3b583b752", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void overShootY() throws Exception {
    super.overShootY();
    assertEquals("06b3d7361380b7c613c9c9168fe23ee56b4fc2b9ebf26878e607086163ea1f86", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void overShootBoth() throws Exception {
    super.overShootBoth();
    assertEquals("9eb044ecd1d35a72cf5f6473f2bdcf05d257854f1b43ce247997e7e2af6c24f4", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void underShootBoth() throws Exception {
    super.underShootBoth();
    assertEquals("7c7782e129323493ecc7103d919df09f392663d0b45e80a4132da4c4fedb5227", TestUtils.hashByteArray(img.getArray()));
  }
}
