package me.lcw.jil.ji;

import static org.junit.Assert.assertEquals;
import me.lcw.jil.BaseImage;
import me.lcw.jil.Color;
import me.lcw.jil.JilImage;
import me.lcw.jil.ResizeTests;
import me.lcw.jil.TestUtils;

public class NNScaleTests extends ResizeTests{
  
  @Override
  public void start() throws Exception {
    scaleType = BaseImage.ScaleType.NN;
    imgRGBA = JilImage.create(BaseImage.MODE.RGBA, 200, 200);
    imgRGBA.fillImageWithColor(Color.WHITE);
    imgRGBA.draw().drawCircle(0, 0, 50, Color.GREEN, 3, false);
    imgRGBA.draw().drawLine(0, 0, 200, 200, Color.GREY, 5, false);
    imgRGBA.draw().drawRect(50, 50, 250, 250, Color.RED, 5, true);
    imgRGBA.draw().drawCircle(100, 100, 75, Color.BLACK, 3, true);
    imgRGB = imgRGBA.changeMode(BaseImage.MODE.RGB);
    imgL = imgRGBA.changeMode(BaseImage.MODE.GREY);
  }
  
  @Override
  public void NoAspectScaleUp() throws Exception {
    super.NoAspectScaleUp();
    assertEquals("9414f5a081163350430ac402cc49d0e22746bb65f35f1e88c7418daf49b30122", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("75775c5173dca7a50c34f2c3b3ec9e6713986d1caf66dd397ec2fad5022aae0c", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("18308164838f0af1ee0e81e903981247cacd0cc7016b63efeb991bd9e629f8bb", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void NoAspectScaleDown() throws Exception {
    super.NoAspectScaleDown();
    assertEquals("f5506eba33e50ad99dbe9564d9b26d379635d04a03c2a0040774898c099369cb", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("c2635d24a00745a4a8b95aa4f473d80c2bd48513d26a44f3efbcd04b9061af02", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("5667bbefaebaf02ff8f3140ce148f5a04a002e25498e7dd57f1a7d947a45ed6a", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void NoAspectUpHeight() throws Exception {
    super.NoAspectUpHeight();
    assertEquals("936b8e36dac30ebd6b45289979fde69438fb315e807e5799e9974eb5e8863899", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("d39bda269b8541eb48775f7f11ca471aad18455df9d2da9477e10bf30c0e77de", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("d608e3ae4bd8d53eea4ed5068871f1ce10add78cbae220493ee05da16d9c03fb", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void NoAspectUpWidth() throws Exception {
    super.NoAspectUpWidth();
    assertEquals("765909bdcb824d45a76032f33382febd6f359866db1e3b30f66e7b5ea2455b40", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("109a81e28c2408fd5907f44ab3120ae01d2865b39ea4e382023c3fadc117a90d", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("eba541c1aafebe525f9ff7d620526353ec400ce7ca1fea3d368de35474ff79cb", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectScaleUp() throws Exception {
    super.AspectScaleUp();
    assertEquals("23833572e2265992c78fdef40b7f972deaa6514e63f26fdc10fafb77d0252973", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("8d657e1716b7406e45b3e7004821e8b7b809cd215f863da9949c1fd82d2f21af", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("8821e22e87fe6748795560de48ad23ae42c778445dc792e024d5756fd86a0ab3", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectScaleDown() throws Exception {
    super.AspectScaleDown();
    assertEquals("ed1617f27d3dfff27b9d7c6e5f514a1917d8db126351f748b80033148edd89fa", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("836b5b4897fd191894de56692d6a3397b775ab5ba26153432682c0d600ed0de5", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("84ab1b253670780f8d6896d1c39b2f4af493818ca6302769ba0705854a959184", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectUpHeight() throws Exception {
    super.AspectUpHeight();
    assertEquals("85dcd24f92d88c67a4d53ba85808a428ec51c67111c84590eedcca130ec0719d", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("74fca582e49048a15702b9bf4f5f6430fb6e7bccf68ba87af302d0f158b6ba95", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("b84780928266641dd6cc88e4fdf08f4261a8ceafa7fa39a8f4ae9e3416e797c6", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectUpWidth() throws Exception {
    super.AspectUpWidth();
    assertEquals("ed1617f27d3dfff27b9d7c6e5f514a1917d8db126351f748b80033148edd89fa", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("836b5b4897fd191894de56692d6a3397b775ab5ba26153432682c0d600ed0de5", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("84ab1b253670780f8d6896d1c39b2f4af493818ca6302769ba0705854a959184", TestUtils.hashByteArray(subImgL.getArray()));
  }

}
