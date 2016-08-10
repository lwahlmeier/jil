package me.lcw.jil.ji;

import static org.junit.Assert.assertEquals;
import me.lcw.jil.Color;
import me.lcw.jil.BaseImage;
import me.lcw.jil.JilImage;
import me.lcw.jil.ResizeTests;
import me.lcw.jil.TestUtils;

public class LinerScaleTests extends ResizeTests {
  
  @Override
  public void start() throws Exception {
    scaleType = BaseImage.ScaleType.LINER;
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
  public void NoAspectScaleUp() throws Exception {
    super.NoAspectScaleUp();
    subImgRGBA.save("/tmp/jil/test2.png");
    assertEquals("a83d657af0677f8857f6b48615c10abfc41bc23e0e39897ed058ffa2a08f276f", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("b94d14376b0741fba067d1e98a94f30f310e166a3327bb09a81fa5640d95ef99", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("dcb6eaba59d72210efaf28cd48b26292dd4f3f9656dc0b9ea270cdd6b8e78170", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void NoAspectScaleDown() throws Exception {
    super.NoAspectScaleDown();
    assertEquals("1729cd1ba3fb2f511e6831574af2af8a5d4debbbe9f162f793064e871eb40c2c", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("54f024804ddd0ee535161f8b210a987f9bebddd389f9a0bc5597a486b62a6b9c", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("77f3890d94d7dfac7e6af03bd82f81394e1bec85acee98e378d959bf518f3486", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void NoAspectUpHeight() throws Exception {
    super.NoAspectUpHeight();
    assertEquals("f4c72819487a207ac573646e00d268594519a1196b59d8781c0ea045faa3d235", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("6686713d2d44852116016b7cb01b4d5b31c7b0ff4647cc86df554b94dbe3c5ce", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("7239febf48efa960a16313b83e6306af6b179e695e802296c638f0a4a48c1a4a", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void NoAspectUpWidth() throws Exception {
    super.NoAspectUpWidth();
    assertEquals("964e5ecfc6215c1fc72cba5c98fb717758cfb02db0a8845680e0d0b85f1523f5", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("e097b812104c2685cc9c087ad76fee94b7549f9a071582c5542a7124a7c20fb9", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("c91effbe18c32af00785985a698a78eb3663f6662231f7ec6d32e11bf5ab0aaa", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectScaleUp() throws Exception {
    super.AspectScaleUp();
    assertEquals("321bdd044a455b06430a514c9b73b092e48eaa3e45a3f787752503305dbbcaf2", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("060c992fa008d99233812071d841f7f651361a25bd9a58f83597c7dc4ae55934", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("10abe2d041093b5270c8c00dbc5e2c22d16629a6ffe6be3360750502c29abb8d", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectScaleDown() throws Exception {
    super.AspectScaleDown();
    assertEquals("9bc4e50acaa071a41f1ccc68d37cbd3fd898cfe8323b471344131bb20409810b", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("6c92a218858d34e2e9e842275cc435c6d10aaf26ded8fd707fd812e16051ee3b", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("690174cf53dfc625ba90aba5fc4dfe8f48232dbf77df90d2a71dc04ab52762e3", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectUpHeight() throws Exception {
    super.AspectUpHeight();
    assertEquals("a160a7c1f9e67af35d1963c91cf95e8f0a58458e915a3f71d81a25888e7267fb", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("e6600d843a5ce96b7a692b717ca7cbe9bc9abbd2ea930a71e15735bf9d7ce135", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("d725a19eef94f04e6ef03095c95211c1de42a2b7300660f79c942e3de9d32428", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectUpWidth() throws Exception {
    super.AspectUpWidth();
    assertEquals("9bc4e50acaa071a41f1ccc68d37cbd3fd898cfe8323b471344131bb20409810b", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("6c92a218858d34e2e9e842275cc435c6d10aaf26ded8fd707fd812e16051ee3b", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("690174cf53dfc625ba90aba5fc4dfe8f48232dbf77df90d2a71dc04ab52762e3", TestUtils.hashByteArray(subImgL.getArray()));
  }
}
