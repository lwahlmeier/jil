package org.java_lcw.jil;


public interface Draw {

  public void drawRect(int x, int y, int w, int h, Color c, int lineWidth, boolean fill);
  public void floodFill(int x, int y, Color c);
  public void floodFill(int x, int y, Color c, Color edge);
  public void floodFill(int x, int y, Color c, Color edge, boolean keepAlpha);
  public void fillColor(int x, int y, Color c);
  public void drawCircle(int cx, int cy, int size, Color c, int lineWidth, boolean fill);
  public void drawLine(int startX, int startY, int endX, int endY, Color c, int lineWidth);
  public void drawLine(int startX, int startY, int endX, int endY, Color c, int lineWidth, boolean alphaMerge);
  
  
  
}




