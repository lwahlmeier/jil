package me.lcw.jil.ji;

import static org.junit.Assert.assertEquals;
import me.lcw.jil.Color;
import me.lcw.jil.Image;
import me.lcw.jil.JilImage;
import me.lcw.jil.ResizeTests;
import me.lcw.jil.TestUtils;

public class CubicScaleTests extends ResizeTests{
  
  @Override
  public void start() throws Exception {
    scaleType = Image.ScaleType.CUBIC;
    imgRGBA = JilImage.create(Image.MODE_RGBA, 200, 200);
    imgRGBA.fillImageWithColor(Color.WHITE);
    imgRGBA.getImageDrawer().drawCircle(0, 0, 50, Color.GREEN, 3, false);
    imgRGBA.getImageDrawer().drawLine(0, 0, 200, 200, Color.GREY, 5, false);
    imgRGBA.getImageDrawer().drawRect(50, 50, 250, 250, Color.RED, 5, true);
    imgRGBA.getImageDrawer().drawCircle(100, 100, 75, Color.BLACK, 3, true);
    imgRGB = imgRGBA.changeMode(Image.MODE_RGB);
    imgL = imgRGBA.changeMode(Image.MODE_L);

  }
  
  @Override
  public void NoAspectScaleUp() throws Exception {
    super.NoAspectScaleUp();

    assertEquals("5aa5ce9d0ada220cc6b3917f5332f00f80377201c8111b0e1ddd007d82b47ac6", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("63b0e8ae31c23aa6ff84d23ca0d087b829e59df1377981f75a625a3060448e9b", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("b501e95748872271d501273c879a232e2f88ae26049f65ab9d40058be17853d2", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void NoAspectScaleDown() throws Exception {
    super.NoAspectScaleDown();

    assertEquals("8efde45ab04333a3f974f81c863624af6bf8e974f63dcceaa8929e60a70b41eb", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("5a55300a272cb22accad7b70cd828b51dc7ae7f4fd876562e8969ad2f85115c4", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("6ca1833c148009ab0480c0c70010e375524073e5be46765280ab778e0bbc5fc1", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void NoAspectUpHeight() throws Exception {
    super.NoAspectUpHeight();

    assertEquals("21133085adf4002d677bc594df538e270b2e0af82eb1ad0591ce3ff32abfd909", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("3291aa4c0bee5947a9e86d5f50601c54a4bf1e74a11ba06342077a4e30ee4dc4", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("48cb0cd78e86fc0ab16d04a1edfa4344b152e01028efa60400bea7123abd8aaf", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void NoAspectUpWidth() throws Exception {
    super.NoAspectUpWidth();

    assertEquals("dc085ef4598058d1a9b4c734ea0d9df26088d175b9f7a0c712256cdf3ff4967f", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("6bdeaca70853952f438a56ce08913cf22c1e2c7293f96f1dba0fd513c7defe22", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("07c5a5921291124e343c140609a11cb1ca62b4f1c88bf59c868e1040f084b350", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectScaleUp() throws Exception {
    super.AspectScaleUp();

    assertEquals("3c5579c3e7664bbe57a4c168758bc3922caf6a385a6f1c165afdcca11257db78", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("351263bb703e9258ec0f5f2b3a43b9570a4413766032b1e15050782dc79458a9", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("b9cf31e2e6dd59e44f755fd92cd4fc0aa7ff0cf4c7d77314d9bdcf2bb4a757a1", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectScaleDown() throws Exception {
    super.AspectScaleDown();

    assertEquals("5047713c0aa5fb6c45cc091e7d1346a9a294b57f809a6d00ad5ab1e892e6bf94", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("ad166920b481951553674406bf00b9647f3c349d0dc9e994d8a1daf34acde4e7", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("fe806a32dea19fa80cb05188c38791299cd61ffdf69ab1b8336f2f0898dec584", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectUpHeight() throws Exception {
    super.AspectUpHeight();

    assertEquals("55d2a0a94824d5226909a04a1dbc7f883d59d86c469618e9d0f79e702d75ea9e", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("3e1bbe2027a780d3db19656bdd20c66c02d069ed3cb245d5eec7035ed1849943", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("1bf5404aa3b87cdcc4cd1570430b290144c80a91b6dd40bd7c2c0212c9af9f92", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectUpWidth() throws Exception {
    super.AspectUpWidth();

    assertEquals("5047713c0aa5fb6c45cc091e7d1346a9a294b57f809a6d00ad5ab1e892e6bf94", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("ad166920b481951553674406bf00b9647f3c349d0dc9e994d8a1daf34acde4e7", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("fe806a32dea19fa80cb05188c38791299cd61ffdf69ab1b8336f2f0898dec584", TestUtils.hashByteArray(subImgL.getArray()));
  }

}
