package me.lcw.jil;

import java.io.IOException;

public class ImageException extends IOException {
  private static final long serialVersionUID = 713250734097347352L;
  public ImageException() {
    super();
  }
  public ImageException(String string) {
    super(string);
  }
}
