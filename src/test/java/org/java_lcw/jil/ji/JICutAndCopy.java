package org.java_lcw.jil.ji;

import org.java_lcw.jil.CutAndCopyTests;
import org.java_lcw.jil.JilImage;

public class JICutAndCopy extends CutAndCopyTests{

  @Override
  public void start() throws Exception {
    img = JilImage.open(filename);
  }

}
