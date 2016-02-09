package me.lcw.jil.awt;

import static org.junit.Assert.assertEquals;
import me.lcw.jil.Color;
import me.lcw.jil.BaseImage;
import me.lcw.jil.ResizeTests;
import me.lcw.jil.TestUtils;

import org.junit.Test;

public class CubicSmoothScaleTests extends ResizeTests{
  
  @Override
  public void start() throws Exception {
    scaleType = BaseImage.ScaleType.CUBIC_SMOOTH;
    imgRGBA = AWTImage.create(BaseImage.MODE.RGBA, 200, 200);
    imgRGBA.fillImageWithColor(Color.WHITE);
    imgRGBA.draw().drawCircle(0, 0, 50, Color.GREEN, 3, false);
    imgRGBA.draw().drawLine(0, 0, 200, 200, Color.GREY, 5, false);
    imgRGBA.draw().drawRect(50, 50, 250, 250, Color.RED, 5, true);
    imgRGBA.draw().drawCircle(100, 100, 75, Color.BLACK, 3, true);
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

    assertEquals("13b8634d599bb664297a4cd4c2f202ef0830990bdf748aec6fb13e57d8d8a1ae", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("5bf9a4faeab4378cd72b7bac15a67f04a437536f28f34276c592b8dffcd1a14a", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("253e5e41a73aa8562ab6ae3e55ed3067edf61800c7880e4043405770f526473f", TestUtils.hashByteArray(subImgL.getArray()));
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

    assertEquals("ac999ba0b712d6e7dcb5f36b3252f8202a61dfdf5239e68695929c892fee8e83", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("a0e55eb84057d8e6c2f39c72dbb2667aedbecc383e30d6addd14221b3422eb33", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("83a7fcd81d7ae1ddb7c559772494b273329dc28e7144ae8c5e5e4a222697dd83", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectUpHeight() throws Exception {
    super.AspectUpHeight();

    assertEquals("0b560bd20a5e0502f78af83ad26c3d3e7be2d56753fbb48f78fde5567aa636f1", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("328b0bf404fcb832ee7168b2476f274b97e7c470c6428a9c5946a9d8b465050a", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("968a44a6394890265f415500e37ee1f2313297aa24164fb04bffca77510910b4", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectUpWidth() throws Exception {
    super.AspectUpWidth();

    assertEquals("ac999ba0b712d6e7dcb5f36b3252f8202a61dfdf5239e68695929c892fee8e83", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("a0e55eb84057d8e6c2f39c72dbb2667aedbecc383e30d6addd14221b3422eb33", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("83a7fcd81d7ae1ddb7c559772494b273329dc28e7144ae8c5e5e4a222697dd83", TestUtils.hashByteArray(subImgL.getArray()));
  }

}
