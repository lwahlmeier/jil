package me.lcw.jil.ji;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import me.lcw.jil.BaseImage;
import me.lcw.jil.Color;
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
    subImg = TestUtils.RGBImageGenerator().changeMode(BaseImage.ImageMode.RGBA32).resize(300, 300, true, BaseImage.ScaleType.NN);
    addAlphaToImage((byte)100, subImg);
  }
  
  @Override
  public void biggerPasteImage() throws Exception {
    subImg2 = JilImage.create(BaseImage.ImageMode.RGBA32, 554, 371);
    subImg2.fillImageWithColor(Color.BLACK);
    addAlphaToImage((byte)10, subImg2);
    super.biggerPasteImage();
    assertEquals("77de746267d9c77b9d39a678b6a335f388c732a3591db9f98e604b942f57db27", TestUtils.hashByteArray(img.getByteArray()));
  }
  
  @Override
  public void normal() throws Exception {
    super.normal();
    assertEquals("c34e9fee2ea98fd6a929399f6d02b834d63652c97345e42ac090f966061b93e2", TestUtils.hashByteArray(img.getByteArray()));
  }
  
  @Override
  public void underShootY() throws Exception {
    super.underShootY();
    assertEquals("c3a0c9c4642016904608abab3ebb56bc195b009be9dc75717f408658d3a1fbaa", TestUtils.hashByteArray(img.getByteArray()));
  }
  
  @Override
  public void underShootX() throws Exception {
    super.underShootX();
    assertEquals("c093310b0acad3d97ce5a5d04d00e490102e185fbd9a47d5d179e6301f352c7d", TestUtils.hashByteArray(img.getByteArray()));
  }
  
  public void overShootX() throws Exception {
    super.overShootX();
    assertEquals("cc5f130578634483ddda14e77070ab2b568995b2f5c63ac3df2013e29111d127", TestUtils.hashByteArray(img.getByteArray()));
  }
  
  @Override
  public void overShootY() throws Exception {
    super.overShootY();
    assertEquals("b5e97682ba4f64238de2a810f39eb8f3ead18f6eb8f27c976910434a4af8b143", TestUtils.hashByteArray(img.getByteArray()));
  }
  
  @Override
  public void overShootBoth() throws Exception {
    super.overShootBoth();
    assertEquals("241ab4f6621f83f642f789d78186e65fcd51c73381988dc834a8d026281cd2fb", TestUtils.hashByteArray(img.getByteArray()));
  }
  
  @Override
  public void underShootBoth() throws Exception {
    super.underShootBoth();
    assertEquals("41ee222ecc40025b2f689d37ba42159b3a213bcc4380d839410f3b3bb6665683", TestUtils.hashByteArray(img.getByteArray()));
  }
}
