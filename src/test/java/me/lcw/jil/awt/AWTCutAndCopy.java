package me.lcw.jil.awt;

import me.lcw.jil.AWTImage;
import me.lcw.jil.CutAndCopyTests;

public class AWTCutAndCopy extends CutAndCopyTests{

  @Override
  public void start() throws Exception {
    img = AWTImage.open(filename);
  }

}
