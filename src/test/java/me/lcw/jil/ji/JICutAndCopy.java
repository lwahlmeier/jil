package me.lcw.jil.ji;

import me.lcw.jil.CutAndCopyTests;
import me.lcw.jil.JilImage;

public class JICutAndCopy extends CutAndCopyTests{

  @Override
  public void start() throws Exception {
    img = JilImage.open(filename);
  }

}
