package me.lcw.jil.ji;

import static org.junit.Assert.assertEquals;
import me.lcw.jil.Color;
import me.lcw.jil.BaseImage;
import me.lcw.jil.JilImage;
import me.lcw.jil.ResizeTests;
import me.lcw.jil.TestUtils;

import org.junit.Test;

public class JilCubicSmoothScaleTests extends ResizeTests{
  
  @Override
  public void start() throws Exception {
    scaleType = BaseImage.ScaleType.CUBIC_SMOOTH;
    imgRGBA = JilImage.create(BaseImage.MODE.RGBA, 200, 200);
    imgRGBA.fillImageWithColor(Color.WHITE);
    imgRGBA.draw().circle(0, 0, 50, Color.GREEN, 3, false);
    imgRGBA.draw().line(0, 0, 200, 200, Color.GREY, 5, false);
    imgRGBA.draw().rect(50, 50, 250, 250, Color.RED, 5, true);
    imgRGBA.draw().circle(100, 100, 75, Color.BLACK, 3, true);
    imgRGB = imgRGBA.changeMode(BaseImage.MODE.RGB);
    imgL = imgRGBA.changeMode(BaseImage.MODE.GREY);

  }
  
  @Override
  @Test(expected=RuntimeException.class)
  public void NoAspectScaleUp() throws Exception {
    super.NoAspectScaleUp();
  }
  
  @Override
  public void NoAspectScaleDown() throws Exception {
    super.NoAspectScaleDown();

    assertEquals("7bfa1932103977978fcd0f53fc082fa11612148aa77c6c5e5cac76484168cbbb", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("5dbd350a7d04772620bb35f71d0540cee5c22d54f27c2618605e86583a92bd19", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("c35f2b15a2dfcfb01203e3110f172983c394e8e0a1010072c45bba0a513dcdd6", TestUtils.hashByteArray(subImgL.getArray()));
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
    assertEquals("9d69a30da579323acd4460e970b8a2e2517e5dafee66e861ab74db4f6a877008", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("5f116af96c1a4a0cd1d834de240be98d19530544443feda7e78f5257a54f03de", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("79bba4b46f62c9a0cf7db1f4f3216dc9e27bb10c767673bfb1c331a0ed98bec0", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectUpHeight() throws Exception {
    super.AspectUpHeight();

    assertEquals("7f95aff20a49a3709e2094a0124a6803678561b692046d56786ec3368c927ef5", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("db9c6435d0d4b52b05daa1d0bd09cb5b035d9023d0145491296f00c98c4a0199", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("6c2be01ea73dc5eb12621379b0a42c25f45652540edd8bfa022d0551388bbcc6", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectUpWidth() throws Exception {
    super.AspectUpWidth();
    assertEquals("9d69a30da579323acd4460e970b8a2e2517e5dafee66e861ab74db4f6a877008", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("5f116af96c1a4a0cd1d834de240be98d19530544443feda7e78f5257a54f03de", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("79bba4b46f62c9a0cf7db1f4f3216dc9e27bb10c767673bfb1c331a0ed98bec0", TestUtils.hashByteArray(subImgL.getArray()));
  }

}
