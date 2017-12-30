package me.lcw.jil.ji;

import static org.junit.Assert.assertEquals;
import me.lcw.jil.Color;
import me.lcw.jil.BaseImage;
import me.lcw.jil.JilImage;
import me.lcw.jil.ResizeTests;
import me.lcw.jil.TestUtils;

public class CubicScaleTests extends ResizeTests{
  
  @Override
  public void start() throws Exception {
    scaleType = BaseImage.ScaleType.CUBIC;
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


    
    assertEquals("bbb09b38086fae6a9ccabe31f071ee57387495c1b03b64c0e1fb6baaff991996", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("64c86e2aca5e8d52379a747f9d431d8fd863fd3e60ef263d2b7e55bcf74f4527", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("a7e4bb2e9c2d7a4a743e2de5a21bd857681e88525ff33d6971f39ca6b83f59f9", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void NoAspectScaleDown() throws Exception {
    super.NoAspectScaleDown();
    

    assertEquals("43f90a4b11273b9000b371fe1b9b7d67f67b44561c413d69d417947d1c26816f", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("6522b27f951f26d08f656265f0e4ffcc5f21c8dcedc2d9bc0ae266971f452204", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("aa934b60a81a130e718152c27e76981b45bbc16a121fd456df641ae17d8da67c", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void NoAspectUpHeight() throws Exception {
    super.NoAspectUpHeight();


    assertEquals("092d779b9ba2337a3c661dc4db8f852f189064a1d2b23184e2dc593f76f32ab6", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("dc582c2d8f64a3b84f07f589a728a884e7a4e291b6761908aacf86bd68ad300a", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("df9f9ecb4f39fe7b9d8d2eefa3de976d7e864e83a5ef9cc2ca149a31236bea7f", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void NoAspectUpWidth() throws Exception {
    super.NoAspectUpWidth();

    assertEquals("f553a878a7b51e912faf5d1646514e38ecd7be156e7767353e9e01d1ab3928fc", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("1c0e91237c5b0a1207f5a74c6e73d1ba893b0e71bd4c9c037fb83f0ebd198622", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("6aaae806627c99c944552b10b38c424efb3d0df433998c7446bd4c8489e3f56d", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectScaleUp() throws Exception {
    super.AspectScaleUp();

    assertEquals("8a812a03b4ee996e6a2b2b4d5b3f51fbf5b7d9541287228813cd9a3942258fd7", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("0dfad3a5e485c8d7f379684d710945a9753b7f712bf7d299bd569cf6967049f6", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("2b8d030dd28bbf61ab1096796eb518c1d4855b44072e313109eb1317073e1ba2", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectScaleDown() throws Exception {
    super.AspectScaleDown();

    assertEquals("30605c454e04ecc20166efbeb697d1b12c80a211dd5479d0692b5a9c958cf058", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("cd9d723761b1bc4322fc11acfcf5908472c05bbe787a11b8a365f3f091f0fefb", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("e672895ed4e734bffb5b9b88e3c1bc01dcf278f6da23fd9ca47ad0a8237a0bdd", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectUpHeight() throws Exception {
    super.AspectUpHeight();

    assertEquals("5fae64c0baf3f5ccc2e5b7110f7915206e7595ae234c76e99ba2cf15199ad6fd", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("e836587434f06a285af31fe32b086eeb1334ba3b630c16b2ea62290de8deb040", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("1f5996b9d31be4a8b1cfbf4b9b4c7ac9edac2258d22f6e46480478a0edad8294", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectUpWidth() throws Exception {
    super.AspectUpWidth();
    
    assertEquals("30605c454e04ecc20166efbeb697d1b12c80a211dd5479d0692b5a9c958cf058", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("cd9d723761b1bc4322fc11acfcf5908472c05bbe787a11b8a365f3f091f0fefb", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("e672895ed4e734bffb5b9b88e3c1bc01dcf278f6da23fd9ca47ad0a8237a0bdd", TestUtils.hashByteArray(subImgL.getArray()));
  }

}
