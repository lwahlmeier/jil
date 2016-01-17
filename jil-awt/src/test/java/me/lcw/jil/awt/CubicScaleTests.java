package me.lcw.jil.awt;

import static org.junit.Assert.assertEquals;
import me.lcw.jil.Color;
import me.lcw.jil.BaseImage;
import me.lcw.jil.ResizeTests;
import me.lcw.jil.TestUtils;

public class CubicScaleTests extends ResizeTests{
  
  @Override
  public void start() throws Exception {
    scaleType = BaseImage.ScaleType.CUBIC;
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
  public void NoAspectScaleUp() throws Exception {
    super.NoAspectScaleUp();

    assertEquals("39696c5e13bd0e4848a31f8c73bdb02dea27a2774f7b1be430f987433e1bfaba", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("ac2dfe3a4792b0b636bb3cf4aa3ba89b455ab1a4a1369e1cef69a00dcae15464", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("71acdbdb2dfef72d9c59cdef160881551d9b081c104e30565b3df86db27fb3a2", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void NoAspectScaleDown() throws Exception {
    super.NoAspectScaleDown();

    assertEquals("53a391dd7235d8185f27c126b9cae646202258916b741965cd99d8fdb0e0d8df", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("45d589167a1adcf57656e0689fa928bf605263560534ed54357f8178d1cfa059", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("4960ddafab0931a16cc359c36a31e3efe1e011c489dc72c2e6017958db733bae", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void NoAspectUpHeight() throws Exception {
    super.NoAspectUpHeight();

    assertEquals("766ac30a045f219392fa30eeb875dd4b3d278de4a245dcdae97d60f4c9cebd1b", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("0ad99c95c6a0a54b844fb5e8aa88982b516b0e6827c3e7333bdb1562f742cb1b", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("eb924ac4ce882d3f887b18d2e9b1e6065cd8607a90b89fc43824e029ecb6ae3e", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void NoAspectUpWidth() throws Exception {
    super.NoAspectUpWidth();

    assertEquals("d3163d556cd931821c545aa94a75521900468be91c25f911f4996f77c63a075a", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("b086b302ed9e53371742f7f4de7ec6332872ec0de8814c2d481803f9c1f909a4", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("833cf6788825d06135fe509f338e0432025322df35281796b151f857d2389508", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectScaleUp() throws Exception {
    super.AspectScaleUp();

    assertEquals("199986de92d85a7a91ce0abfb094b99e08478b4311d881206133c22768fb3ea0", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("4d0d1f274a757e04eb6c1b440e345c4d85de5345a2b2a29a416fcb9192e73fe1", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("56d708d26dd51473682f721deb137e050599f81a27006e56a7a901e6fff8eff2", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectScaleDown() throws Exception {
    super.AspectScaleDown();

    assertEquals("b56cb44afbd1ab55d6180c823fd744d6d5ed6df1a912a57db714f069458eb6c2", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("a9fb2d254a2d339612331f00901469f076654e5d7cc8948f1077da6395aefb8d", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("5c0639f5d137cf575d2845772a0471bdf90e5b55bbad7126f393dd444ef4d06b", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectUpHeight() throws Exception {
    super.AspectUpHeight();

    assertEquals("e32dc430be3071aa0548877625efa8d6d821891f7fe9648bb62b715fb28ef644", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("482ba38f9f70fb4d6f417caa40cccb65497894c7671206765f195c626a18f23f", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("a92c96db3314ef2d504daade4bc001241e5126deae5d5fff22b329ac75bb6de5", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectUpWidth() throws Exception {
    super.AspectUpWidth();

    assertEquals("b56cb44afbd1ab55d6180c823fd744d6d5ed6df1a912a57db714f069458eb6c2", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("a9fb2d254a2d339612331f00901469f076654e5d7cc8948f1077da6395aefb8d", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("5c0639f5d137cf575d2845772a0471bdf90e5b55bbad7126f393dd444ef4d06b", TestUtils.hashByteArray(subImgL.getArray()));
  }

}
