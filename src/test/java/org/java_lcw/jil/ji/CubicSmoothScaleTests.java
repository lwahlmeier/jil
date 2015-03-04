package org.java_lcw.jil.ji;

import static org.junit.Assert.assertEquals;

import org.java_lcw.jil.Color;
import org.java_lcw.jil.Image;
import org.java_lcw.jil.JavaImage;
import org.java_lcw.jil.ResizeTests;
import org.java_lcw.jil.TestUtils;
import org.junit.Test;

public class CubicSmoothScaleTests extends ResizeTests{
  
  @Override
  public void start() throws Exception {
    scaleType = Image.ScaleType.CUBIC_SMOOTH;
    imgRGBA = JavaImage.create(Image.MODE_RGBA, 200, 200);
    imgRGBA.fillImageWithColor(Color.WHITE);
    imgRGBA.getImageDrawer().drawCircle(0, 0, 50, Color.GREEN, 3, false);
    imgRGBA.getImageDrawer().drawLine(0, 0, 200, 200, Color.GREY, 5);
    imgRGBA.getImageDrawer().drawRect(50, 50, 250, 250, Color.RED, 5, true);
    imgRGBA.getImageDrawer().drawCircle(100, 100, 75, Color.BLACK, 3, true);
    imgRGB = imgRGBA.changeMode(Image.MODE_RGB);
    imgL = imgRGBA.changeMode(Image.MODE_L);

  }
  
  @Override
  @Test(expected=RuntimeException.class)
  public void NoAspectScaleUp() throws Exception {
    super.NoAspectScaleUp();
  }
  
  @Override
  public void NoAspectScaleDown() throws Exception {
    super.NoAspectScaleDown();

    assertEquals("f5506eba33e50ad99dbe9564d9b26d379635d04a03c2a0040774898c099369cb", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("c2635d24a00745a4a8b95aa4f473d80c2bd48513d26a44f3efbcd04b9061af02", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("d38b836c4a0ce7ce9a4668ef42e082a9775765ba6ddc14b40121e7082fa70a71", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  @Test(expected=RuntimeException.class)
  public void NoAspectUpHeight() throws Exception {
    super.NoAspectUpHeight();

    assertEquals("21133085adf4002d677bc594df538e270b2e0af82eb1ad0591ce3ff32abfd909", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("3291aa4c0bee5947a9e86d5f50601c54a4bf1e74a11ba06342077a4e30ee4dc4", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("48cb0cd78e86fc0ab16d04a1edfa4344b152e01028efa60400bea7123abd8aaf", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  @Test(expected=RuntimeException.class)
  public void NoAspectUpWidth() throws Exception {
    super.NoAspectUpWidth();

    assertEquals("dc085ef4598058d1a9b4c734ea0d9df26088d175b9f7a0c712256cdf3ff4967f", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("6bdeaca70853952f438a56ce08913cf22c1e2c7293f96f1dba0fd513c7defe22", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("07c5a5921291124e343c140609a11cb1ca62b4f1c88bf59c868e1040f084b350", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  @Test(expected=RuntimeException.class)
  public void AspectScaleUp() throws Exception {
    super.AspectScaleUp();

    assertEquals("3c5579c3e7664bbe57a4c168758bc3922caf6a385a6f1c165afdcca11257db78", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("351263bb703e9258ec0f5f2b3a43b9570a4413766032b1e15050782dc79458a9", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("b9cf31e2e6dd59e44f755fd92cd4fc0aa7ff0cf4c7d77314d9bdcf2bb4a757a1", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectScaleDown() throws Exception {
    super.AspectScaleDown();

    assertEquals("ed1617f27d3dfff27b9d7c6e5f514a1917d8db126351f748b80033148edd89fa", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("836b5b4897fd191894de56692d6a3397b775ab5ba26153432682c0d600ed0de5", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("3bd7aaa90d004d959282b2d52a5d501a5ab72d6b999caafa0fb97ffe99d1c213", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectUpHeight() throws Exception {
    super.AspectUpHeight();

    assertEquals("85dcd24f92d88c67a4d53ba85808a428ec51c67111c84590eedcca130ec0719d", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("74fca582e49048a15702b9bf4f5f6430fb6e7bccf68ba87af302d0f158b6ba95", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("52e04c069f0a432f810cc86b44daf3ab2418d906aa7428eda1bdde489d2669e8", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectUpWidth() throws Exception {
    super.AspectUpWidth();

    assertEquals("ed1617f27d3dfff27b9d7c6e5f514a1917d8db126351f748b80033148edd89fa", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("836b5b4897fd191894de56692d6a3397b775ab5ba26153432682c0d600ed0de5", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("3bd7aaa90d004d959282b2d52a5d501a5ab72d6b999caafa0fb97ffe99d1c213", TestUtils.hashByteArray(subImgL.getArray()));
  }

}
