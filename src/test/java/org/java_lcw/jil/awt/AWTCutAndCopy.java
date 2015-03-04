package org.java_lcw.jil.awt;

import org.java_lcw.jil.AWTImage;
import org.java_lcw.jil.CutAndCopyTests;

public class AWTCutAndCopy extends CutAndCopyTests{

  @Override
  public void start() throws Exception {
    img = AWTImage.open(filename);
  }

}
