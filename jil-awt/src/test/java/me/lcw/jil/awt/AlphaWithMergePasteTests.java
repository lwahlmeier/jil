package me.lcw.jil.awt;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;

import me.lcw.jil.BaseImage;
import me.lcw.jil.Color;
import me.lcw.jil.ImageException;
import me.lcw.jil.PasteTests;
import me.lcw.jil.TestUtils;

public class AlphaWithMergePasteTests extends PasteTests {

  @Override
  @Before
  public void start() throws ImageException, IOException{
    alpha = true;
    img = AWTImage.fromBaseImage(TestUtils.RGBAImageGenerator());
    subImg = AWTImage.fromBaseImage(TestUtils.RGBImageGenerator().changeMode(BaseImage.MODE.RGBA).resize(300, 300, true, BaseImage.ScaleType.NN));
    addAlphaToImage((byte)100, subImg);

  }
  
  @Override
  public void biggerPasteImage() throws Exception {
    subImg2 = AWTImage.create(BaseImage.MODE.RGBA, 554, 371);
    subImg2.fillImageWithColor(Color.BLACK);
    addAlphaToImage((byte)10, subImg2);
    super.biggerPasteImage();
    assertEquals("6783219f3be8d0f85b97f71d126cc01408850f72fe686046c9c5ad40a7ada87c", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void normal() throws Exception {
    super.normal();
    assertEquals("0458cab7b3b7e3fcd18777f184db955a5ddb4beeb708629b71b6862e9536e9d9", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void underShootY() throws Exception {
    super.underShootY();
    
    assertEquals("39463b9b6903a5f921c49abf21718b471840a5fa0d3fd68a18ba7b2c4bc7ae74", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void underShootX() throws Exception {
    super.underShootX();
    
    assertEquals("9341c9e0a73709cbecd6f8bef979f39b648a815a2c740992447c4fe80b427c15", TestUtils.hashByteArray(img.getArray()));
  }
  
  public void overShootX() throws Exception {
    super.overShootX();
    
    assertEquals("f20978529684c1882eca78adb3cbc7b060c21ee5e19e3f168fc3cd4ac9548ff0", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void overShootY() throws Exception {
    super.overShootY();
    
    assertEquals("875facd6b6f4990900401dfe0ce9722e7a49b2152e6eac222d1d671692558962", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void overShootBoth() throws Exception {
    super.overShootBoth();
    
    assertEquals("20b8aab90bad42b1e830a59f57e6496896448eb97d26b769c59e8e2e16959fe6", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void underShootBoth() throws Exception {
    super.underShootBoth();
    assertEquals("b9034014485e55edd85b20975b55cd06d46fa73e62b26860ad55d4f1c8d58d72", TestUtils.hashByteArray(img.getArray()));
  }
}
