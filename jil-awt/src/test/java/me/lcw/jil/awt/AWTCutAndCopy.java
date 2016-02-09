package me.lcw.jil.awt;

import me.lcw.jil.CutAndCopyTests;
import me.lcw.jil.TestUtils;

public class AWTCutAndCopy extends CutAndCopyTests{

  @Override
  public void start() throws Exception {
    img = AWTImage.fromBaseImage(TestUtils.RGBAImageGenerator());
  }

}
