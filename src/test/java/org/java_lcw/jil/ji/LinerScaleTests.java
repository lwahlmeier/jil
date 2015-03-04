package org.java_lcw.jil.ji;

import static org.junit.Assert.assertEquals;

import org.java_lcw.jil.Color;
import org.java_lcw.jil.Image;
import org.java_lcw.jil.JavaImage;
import org.java_lcw.jil.ResizeTests;
import org.java_lcw.jil.TestUtils;

public class LinerScaleTests extends ResizeTests {
  
  @Override
  public void start() throws Exception {
    scaleType = Image.ScaleType.LINER;
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
  public void NoAspectScaleUp() throws Exception {
    super.NoAspectScaleUp();
    assertEquals("a83d657af0677f8857f6b48615c10abfc41bc23e0e39897ed058ffa2a08f276f", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("b94d14376b0741fba067d1e98a94f30f310e166a3327bb09a81fa5640d95ef99", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("0c3cafa02fbd5ea986eab24f6814cb3203c9b4efb31a88f96d525a87bb956239", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void NoAspectScaleDown() throws Exception {
    super.NoAspectScaleDown();
    assertEquals("1729cd1ba3fb2f511e6831574af2af8a5d4debbbe9f162f793064e871eb40c2c", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("54f024804ddd0ee535161f8b210a987f9bebddd389f9a0bc5597a486b62a6b9c", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("7e987527a78dd9e576e99e6b78b2340dea88e28990c2d1e64145d3b67d126e4d", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void NoAspectUpHeight() throws Exception {
    super.NoAspectUpHeight();
    assertEquals("f4c72819487a207ac573646e00d268594519a1196b59d8781c0ea045faa3d235", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("6686713d2d44852116016b7cb01b4d5b31c7b0ff4647cc86df554b94dbe3c5ce", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("865cf9870a105ccf1bea21b15ecc57268bfe9200c3d6cd9b0cbf4f05c802d530", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void NoAspectUpWidth() throws Exception {
    super.NoAspectUpWidth();
    assertEquals("964e5ecfc6215c1fc72cba5c98fb717758cfb02db0a8845680e0d0b85f1523f5", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("e097b812104c2685cc9c087ad76fee94b7549f9a071582c5542a7124a7c20fb9", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("cf5b444c7c521deaaeaa88e30869fdffae499eaf04f48ce0ca8e5a36bcbc2895", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectScaleUp() throws Exception {
    super.AspectScaleUp();
    assertEquals("321bdd044a455b06430a514c9b73b092e48eaa3e45a3f787752503305dbbcaf2", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("060c992fa008d99233812071d841f7f651361a25bd9a58f83597c7dc4ae55934", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("dc3e5ef76de769fde71d45f26ca42dfa585662211ac56a68e5614d54af818b06", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectScaleDown() throws Exception {
    super.AspectScaleDown();
    assertEquals("9bc4e50acaa071a41f1ccc68d37cbd3fd898cfe8323b471344131bb20409810b", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("6c92a218858d34e2e9e842275cc435c6d10aaf26ded8fd707fd812e16051ee3b", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("5e454c8d9da191f5a3f181e4945020f1ac7c5ecee2a495796c3c7e0bc2262bb2", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectUpHeight() throws Exception {
    super.AspectUpHeight();
    assertEquals("a160a7c1f9e67af35d1963c91cf95e8f0a58458e915a3f71d81a25888e7267fb", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("e6600d843a5ce96b7a692b717ca7cbe9bc9abbd2ea930a71e15735bf9d7ce135", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("0ecbd45287cec6beff89089c67f1de16fc88d2a92ad6f9720b580775e91fb09b", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectUpWidth() throws Exception {
    super.AspectUpWidth();
    assertEquals("9bc4e50acaa071a41f1ccc68d37cbd3fd898cfe8323b471344131bb20409810b", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("6c92a218858d34e2e9e842275cc435c6d10aaf26ded8fd707fd812e16051ee3b", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("5e454c8d9da191f5a3f181e4945020f1ac7c5ecee2a495796c3c7e0bc2262bb2", TestUtils.hashByteArray(subImgL.getArray()));
  }
}
