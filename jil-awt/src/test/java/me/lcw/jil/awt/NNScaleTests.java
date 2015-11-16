package me.lcw.jil.awt;

import static org.junit.Assert.assertEquals;
import me.lcw.jil.Color;
import me.lcw.jil.BaseImage;
import me.lcw.jil.ResizeTests;
import me.lcw.jil.TestUtils;

public class NNScaleTests extends ResizeTests{
  
  @Override
  public void start() throws Exception {
    scaleType = BaseImage.ScaleType.NN;
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

    assertEquals("0ebff8d9c19ba55d3f8ea412eb0dc8fa1b77744efbbc52de43960d7f0ea3eb17", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("2ddd8a0600cca8fe84634de339d29d94990f96561ab7904b673cbf7a654faec7", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("35e468512dc2109c9bbafb8a45b84a2a61698a10bc981cf018de5bd746600357", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void NoAspectScaleDown() throws Exception {
    super.NoAspectScaleDown();

    assertEquals("13b8634d599bb664297a4cd4c2f202ef0830990bdf748aec6fb13e57d8d8a1ae", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("5bf9a4faeab4378cd72b7bac15a67f04a437536f28f34276c592b8dffcd1a14a", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("253e5e41a73aa8562ab6ae3e55ed3067edf61800c7880e4043405770f526473f", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void NoAspectUpHeight() throws Exception {
    super.NoAspectUpHeight();

    assertEquals("f53ae3d1737cd9047d2ba98ee5be345a0ad3bb61f716632bc92082ec7b91bb92", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("32d7c72565d709e9443ef03a7cfed5d5c9469dee482aeee544d24ffb53eedce2", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("419d288540149b2eb5a1e8769fabf110ac1df7d31667b527288919ae1cb39cbd", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void NoAspectUpWidth() throws Exception {
    super.NoAspectUpWidth();

    assertEquals("b9fdca307ea6ef0e4118b20e5b2b75c93c8412b3d6b98fa4f05964aafa209c38", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("21403d5d5cb483a878d4c14ac8ccc2fe1459a368361117e7caea4a43c7621cdf", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("2353eb7df34ed95521d97c29ff0bed8abe3942e0739cdd936dbaa0af7c2ecefb", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectScaleUp() throws Exception {
    super.AspectScaleUp();

    assertEquals("1467729abe0ca6d083c077884d7061dbbe49eec77f7e776d7887b27b97e3439e", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("aeb8d9935e7f37d18de4df8da668991b3bb25ba00b9631f1a824e635404dce90", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("96a30c2cf910686bf1c7f9bfafd2a45ba3fd413e7b7cc60202f9715ee9231158", TestUtils.hashByteArray(subImgL.getArray()));
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
