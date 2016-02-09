package me.lcw.jil;

/**
 * The generic Draw Interface.  These are the basic draw functions needed by any image implementing a "Drawer". 
 * 
 */
public interface Draw {

  /**
   * Draws a rectangle on the Image.
   * 
   * @param x the x position to put the top right corner of the rectangle.
   * @param y the y position to put the top right corner of the rectangle.
   * @param w the width of the rectangle.
   * @param h the height of the rectangle.
   * @param c the color to make the rectangle.
   * @param lineWidth the width of the border of the rectangle, the rect will be of w/h and the lineWidth will be internal from there.
   * @param fill boolean to set if the rectangle should be filled or not.
   */
  public void drawRect(int x, int y, int w, int h, Color c, int lineWidth, boolean fill);
  
  /**
   * Flood fill from the specified positions.  All other colors matching the color of the position at that time will be filled.
   * @param x the x position to start filling.
   * @param y the y position to start filling.
   * @param c the color to fill with.
   * @param edge the edge color to stop filling at, or null if to use original color.
   * @param keepAlpha set true to keep the alpha level of the original image.
   */
  public void floodFill(int x, int y, Color c, Color edge, boolean keepAlpha);
  
  /**
   * Change the color at the set position to the specified color.
   * 
   * @param x the x position to use.
   * @param y the y position to use.
   * @param c the color to set.
   */
  public void fillColor(int x, int y, Color c);
  
  /**
   * Draw a circle at set position.
   * 
   * @param cx the x position for the center of the circle.
   * @param cy the y position for the center of the circle.
   * @param size the diameter of the circle.
   * @param c the color of the circle.
   * @param lineWidth the width of the edge of the circle. The diameter will stay at set size and lines will travel inwards.
   * @param fill set to fill the circle with set color.
   */
  public void drawCircle(int cx, int cy, int size, Color c, int lineWidth, boolean fill);
  
  /**
   * Draws a line along specified path.
   *  
   * @param startX the x position to start the line at.
   * @param startY the y position to start the line at.
   * @param endX the x position to end the line at.
   * @param endY the y position to end the line at.
   * @param c the lines color
   * @param lineWidth the lines width.
   * @param alphaMerge set to merge the alpha changes of the lines color and the backing image.
   */
  public void drawLine(int startX, int startY, int endX, int endY, Color c, int lineWidth, boolean alphaMerge);
  
}




