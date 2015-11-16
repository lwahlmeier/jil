package me.lcw.jil.awt;

import static org.junit.Assert.assertEquals;
import me.lcw.jil.Color;
import me.lcw.jil.BaseImage;
import me.lcw.jil.ResizeTests;
import me.lcw.jil.TestUtils;

public class LinerScaleTests extends ResizeTests {
  
  @Override
  public void start() throws Exception {
    scaleType = BaseImage.ScaleType.LINER;
    imgRGBA = AWTImage.create(BaseImage.MODE.RGBA, 200, 200);
    imgRGBA.fillImageWithColor(Color.WHITE);
    imgRGBA.getImageDrawer().drawCircle(0, 0, 50, Color.GREEN, 3, false);
    imgRGBA.getImageDrawer().drawLine(0, 0, 200, 200, Color.GREY, 5, false);
    imgRGBA.getImageDrawer().drawRect(50, 50, 250, 250, Color.RED, 5, true);
    imgRGBA.getImageDrawer().drawCircle(100, 100, 75, Color.BLACK, 3, true);
    imgRGB = imgRGBA.changeMode(BaseImage.MODE.RGB);
    imgL = imgRGBA.changeMode(BaseImage.MODE.GREY);
  }
  
  @Override
  public void NoAspectScaleUp() throws Exception {
    super.NoAspectScaleUp();

    assertEquals("d6ce8d665595dcb633d46e1bea11810b85b1ff12f94896e7346bf1bb07677975", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("21d172b7116591c75b87a0da9f8323503e2d0f58cca43020773823c3e9026579", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("7dc83af803aaefd2d28f3957fa292c71fda58f3ea79674e699077abb1a7ec7d3", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void NoAspectScaleDown() throws Exception {
    super.NoAspectScaleDown();

    assertEquals("aab1995600630dda6684d1fd910ac457d50ada39ee7779814ad89c3744960bdd", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("d45c9d8310def9274ab4f63be8687c3eb072d5c88540a52f5fb6ae5abe446ae6", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("1df5d573f83271536a34995d4cd35c127fb360b1ec46efe6732f4195242bed77", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void NoAspectUpHeight() throws Exception {
    super.NoAspectUpHeight();

    assertEquals("d12abab6a1917c23c78a251ceb5c0dccc99b11755fa278741d9236669b4e792c", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("4134f7f2478ea4a62eb55eb6833657c00eaa25aad1db0bc72fad03064d73cdd8", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("b8d7b4fc8f57e145035ed6dd4a94c6d22e1bbc9c87efd24ea098f4ac320551dd", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void NoAspectUpWidth() throws Exception {
    super.NoAspectUpWidth();

    assertEquals("3a24c43cc8536ca7bdda2c9df95fb586484c51f21129bff0b8a518a6483b82e1", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("c11946e99a7ee1ee0a6d785916f96714fe0eee02a58c9ce7702e60b4975895f1", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("d1535694ee4736d89a52ad841dcb2c21b74b146a525054ae33bbaf363e03238c", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectScaleUp() throws Exception {
    super.AspectScaleUp();

    assertEquals("f0421a020ee89dedba568cad9d9e025530e31517734a0de5db4cdda827d06ecb", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("f3352a7584532e049b41ce3c53e85816da065e0d362a8dc120bfd36150933be0", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("2999e38fb7c0f082379b0444ad64815871ae4b9a689e6a0fa3fa8beaaee8d462", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectScaleDown() throws Exception {
    super.AspectScaleDown();

    assertEquals("e86b4c12aea16ef50cdd051abb8b001cf486aa9c1f697ad606e005414dfcbe11", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("8145401694fa67fe51f88393a8a25eb1343d19c7eb52f6ffda30dc7e9c0ef278", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("6aadbf9536fe2600b6b95cce6c3e4f772e20185492feed4ef116a6afa0e5e250", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectUpHeight() throws Exception {
    super.AspectUpHeight();

    assertEquals("7244a582b2b9f7fbfcdd88ddae3dca2ed5e022b1cadc2486df1a3e1305a69f73", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("d9fcd93fb655eb0d5aec623f8b41a81ef204c4a0a47332f66b35defa07d8c2a8", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("03ef9171f805fa11979eff74cac351f053cdcaea3f2321f4f73ab1192501d38a", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectUpWidth() throws Exception {
    super.AspectUpWidth();

    assertEquals("e86b4c12aea16ef50cdd051abb8b001cf486aa9c1f697ad606e005414dfcbe11", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("8145401694fa67fe51f88393a8a25eb1343d19c7eb52f6ffda30dc7e9c0ef278", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("6aadbf9536fe2600b6b95cce6c3e4f772e20185492feed4ef116a6afa0e5e250", TestUtils.hashByteArray(subImgL.getArray()));
  }
}
