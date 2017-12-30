package me.lcw.jil.ji;

import static org.junit.Assert.assertEquals;
import me.lcw.jil.Color;
import me.lcw.jil.BaseImage;
import me.lcw.jil.JilImage;
import me.lcw.jil.ResizeTests;
import me.lcw.jil.TestUtils;

import org.junit.Test;

public class CubicSmoothScaleTests extends ResizeTests{
  
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

    assertEquals("c9dafe0c64083a2f7fe9e151b116aa2d55d4fa11328876faf3f8d512595ca44f", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("75182f918346fc84fcabe1b2f217f156f6f3c4a3473af62ce27fe29af17ab800", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("c98617ca9aa8b3374fc267b10fd8ef0d75cebd1b756263efcd8cc063a8f92c09", TestUtils.hashByteArray(subImgL.getArray()));
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

    assertEquals("8980d0f793db01e3ad7d69b36d415ac8f058d74ce2754148beab96201612e054", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("d410989dddb117fe7833ba956f3413a7d20669f35e4687901a668639a3ff29f6", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("230be9a0cbdea82b208ee49164d53aec6e86e46278c433e30b624ce4af829395", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectUpHeight() throws Exception {
    super.AspectUpHeight();

    assertEquals("48bdb3a84d0e6aaefec70f5f7c02e24e44aa38a5b1158a57b453a024556c7fe0", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("05dc1d11f5c941d2a255063479424bc59be64d5425e5d6a9901d023cdbb06cc9", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("438006068b5044d0eef44e3963bd68d2553d2ccd8ca578d392244352cc0090e5", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectUpWidth() throws Exception {
    super.AspectUpWidth();

    assertEquals("8980d0f793db01e3ad7d69b36d415ac8f058d74ce2754148beab96201612e054", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("d410989dddb117fe7833ba956f3413a7d20669f35e4687901a668639a3ff29f6", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("230be9a0cbdea82b208ee49164d53aec6e86e46278c433e30b624ce4af829395", TestUtils.hashByteArray(subImgL.getArray()));
  }

}
