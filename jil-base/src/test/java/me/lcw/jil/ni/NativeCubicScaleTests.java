package me.lcw.jil.ni;

import static org.junit.Assert.assertEquals;

import me.lcw.jil.BaseImage;
import me.lcw.jil.Color;
import me.lcw.jil.NativeImage;
import me.lcw.jil.ResizeTests;
import me.lcw.jil.TestUtils;

public class NativeCubicScaleTests extends ResizeTests{
  
  @Override
  public void start() throws Exception {
    scaleType = BaseImage.ScaleType.CUBIC;
    imgRGBA = NativeImage.create(BaseImage.MODE.RGBA, 200, 200);
    imgRGBA.fillImageWithColor(Color.WHITE);
    imgRGBA.draw().circle(0, 0, 50, Color.GREEN, 3, false);
    imgRGBA.draw().line(0, 0, 200, 200, Color.GREY, 5, false);
    imgRGBA.draw().rect(50, 50, 250, 250, Color.RED, 5, true);
    imgRGBA.draw().circle(100, 100, 75, Color.BLACK, 3, true);
    imgRGB = imgRGBA.changeMode(BaseImage.MODE.RGB);
    imgL = imgRGBA.changeMode(BaseImage.MODE.GREY);

  }
  
  @Override
  public void NoAspectScaleUp() throws Exception {
    super.NoAspectScaleUp();

    assertEquals("5aa5ce9d0ada220cc6b3917f5332f00f80377201c8111b0e1ddd007d82b47ac6", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("63b0e8ae31c23aa6ff84d23ca0d087b829e59df1377981f75a625a3060448e9b", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("c5b00b730b78af4dcc801f369b6d5730c9beeca98a858769c4532b1c3cd96732", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void NoAspectScaleDown() throws Exception {
    super.NoAspectScaleDown();

    assertEquals("8efde45ab04333a3f974f81c863624af6bf8e974f63dcceaa8929e60a70b41eb", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("5a55300a272cb22accad7b70cd828b51dc7ae7f4fd876562e8969ad2f85115c4", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("57e510022ae1fc07c4f1df7ec3a11d457f229b07b561fc02776f73ee519547e3", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void NoAspectUpHeight() throws Exception {
    super.NoAspectUpHeight();

    assertEquals("21133085adf4002d677bc594df538e270b2e0af82eb1ad0591ce3ff32abfd909", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("3291aa4c0bee5947a9e86d5f50601c54a4bf1e74a11ba06342077a4e30ee4dc4", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("cf47647361938c715f3830311697e9b0ad153cbf1451770662019c109aaed9cf", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void NoAspectUpWidth() throws Exception {
    super.NoAspectUpWidth();

    assertEquals("dc085ef4598058d1a9b4c734ea0d9df26088d175b9f7a0c712256cdf3ff4967f", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("6bdeaca70853952f438a56ce08913cf22c1e2c7293f96f1dba0fd513c7defe22", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("bbbc95269130c51a5b53ffd57b2f2518ba58332e6fb7d3f140ab2d6a43c360b4", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectScaleUp() throws Exception {
    super.AspectScaleUp();

    assertEquals("3c5579c3e7664bbe57a4c168758bc3922caf6a385a6f1c165afdcca11257db78", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("351263bb703e9258ec0f5f2b3a43b9570a4413766032b1e15050782dc79458a9", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("9d1b201ca8f0b5dc026e53ec33d89c2610b1c626642cceca0bab39128eb48aa0", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectScaleDown() throws Exception {
    super.AspectScaleDown();

    assertEquals("5047713c0aa5fb6c45cc091e7d1346a9a294b57f809a6d00ad5ab1e892e6bf94", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("ad166920b481951553674406bf00b9647f3c349d0dc9e994d8a1daf34acde4e7", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("b6fd885f33aef8a00cf13885c5d5e6cd91506790d8663763e450e97d6ced7aa8", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectUpHeight() throws Exception {
    super.AspectUpHeight();

    assertEquals("55d2a0a94824d5226909a04a1dbc7f883d59d86c469618e9d0f79e702d75ea9e", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("3e1bbe2027a780d3db19656bdd20c66c02d069ed3cb245d5eec7035ed1849943", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("93e60fe920052793e40d1ee9766470dc6ff4f7d8bd0d2502f2e1990ce812b14f", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectUpWidth() throws Exception {
    super.AspectUpWidth();

    assertEquals("5047713c0aa5fb6c45cc091e7d1346a9a294b57f809a6d00ad5ab1e892e6bf94", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("ad166920b481951553674406bf00b9647f3c349d0dc9e994d8a1daf34acde4e7", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("b6fd885f33aef8a00cf13885c5d5e6cd91506790d8663763e450e97d6ced7aa8", TestUtils.hashByteArray(subImgL.getArray()));
  }

}
