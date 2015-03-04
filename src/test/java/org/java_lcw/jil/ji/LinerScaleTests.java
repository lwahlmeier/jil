package org.java_lcw.jil.ji;

import static org.junit.Assert.assertEquals;

import org.java_lcw.jil.Image;
import org.java_lcw.jil.JavaImage;
import org.java_lcw.jil.ResizeTests;
import org.java_lcw.jil.TestUtils;

public class LinerScaleTests extends ResizeTests {
  
  @Override
  public void start() throws Exception {
    scaleType = Image.ScaleType.LINER;
    imgRGBA = JavaImage.open(filename).changeMode(Image.MODE_RGBA);
    imgRGB = JavaImage.open(filename).changeMode(Image.MODE_RGB);
    imgL = JavaImage.open(filename).changeMode(Image.MODE_L);
  }
  
  @Override
  public void NoAspectScaleUp() throws Exception {
    super.NoAspectScaleUp();
    assertEquals("79bb767de925470c83dcb4f0aa698ec4b369c102a4b612b3dc1240b70ea08d5f", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("946a65307637418fff3291b39d0c1615ddb7d4305ce74c1c2a3d2b467bf72fd4", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("f952f9dd0be86b3c4f5d8dd74fdd526eca8499dac182a075edb41e6a0547ab73", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void NoAspectScaleDown() throws Exception {
    super.NoAspectScaleDown();
    assertEquals("66beda25aa6b4630c7daa5ec13cabcae6a0bdc1a9f5b28a0526926748f491fc5", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("e50de8fe44ed60f73ea35c3c991416524f37905b5502602ef7d4336d183836e7", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("3bd423bfed62c7478a2093e3ac60e17c8d8c3725045b27344703bd87d21e169b", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void NoAspectUpHeight() throws Exception {
    super.NoAspectUpHeight();

    assertEquals("89064b90f13b0f3a03227959b21d93f82d3f609d8c33fef8cbd48be4826a8831", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("0a79ce6c3ad5f69826885aee0e561ba4ae29ee18c4259ed9c092ad4c9f98a8bb", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("f4e90210cd3f73596f1fcfd24f99e683bda9f33ae654901a34d2750b5200db74", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void NoAspectUpWidth() throws Exception {
    super.NoAspectUpWidth();
    assertEquals("e80d19b755b010c95cc80c390cc900fe10ad9198b0d0bb60fe81f02dd4619df0", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("8da6488d05645c85243ebd62ad2fb32f41c62ed17874aa4fa4a05ba16d450887", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("13cf505c607a12290719c2884eea73393e943600e789bf7270e3fa90df4f3bf7", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectScaleUp() throws Exception {
    super.AspectScaleUp();
    assertEquals("1f3e198412c11f9e7210faba1be8001a835357bef0b15b74da9c4de7a5f2d219", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("b98599e557d1b82956bbdd1d3eb1235a462b3cc1d6e2f13459742a4fd7baaf65", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("41b0de62589347abae7357088e66995ffd535f177a04b5eec90616797286ba21", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectScaleDown() throws Exception {
    super.AspectScaleDown();
    assertEquals("01c632fb3f62c68059966723be5ff7efa2220f2e91ff7ab39a60be3a603684e3", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("08f1d1bd970a04017699513f0f9eb579918a3a76e0b2712af4a027809d580679", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("ab9ef274e64ad6042c0bb152334b0658d352894ecdc6111432d98b748a48e6c2", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectUpHeight() throws Exception {
    super.AspectUpHeight();

    assertEquals("0a3adca651f7fb984dda5768ac0c0d4bc97f509f15ce2de8f5f4cdddeed712d8", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("fcde8f29de0808ff356d84b927986829bf8e92737db938a38cb27230c9ffdad6", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("c67eaaf09e812236723b86ebcace264c21faa3f516e1b8bad8fe29c4300682bd", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectUpWidth() throws Exception {
    super.AspectUpWidth();
    assertEquals("0374c64553a291be14a63505ae8e9e08ee66be5b9e391b9df3b0bedc26430012", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("24db249f0d0e062c8997e6846f9d0b9c5ba8bc3bbb6fdb77450059d578c1a58d", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("0db2b1626c8d2c96b26ef6972795a984f80134d6e017113d27cdef0fdd0437f2", TestUtils.hashByteArray(subImgL.getArray()));
  }
}
