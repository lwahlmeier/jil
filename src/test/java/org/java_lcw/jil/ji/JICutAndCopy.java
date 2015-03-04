package org.java_lcw.jil.ji;

import org.java_lcw.jil.CutAndCopyTests;
import org.java_lcw.jil.JavaImage;

public class JICutAndCopy extends CutAndCopyTests{

  @Override
  public void start() throws Exception {
    img = JavaImage.open(filename);
  }

}
