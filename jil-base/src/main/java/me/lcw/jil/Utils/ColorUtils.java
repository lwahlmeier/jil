package me.lcw.jil.Utils;

import me.lcw.jil.Color;
import me.lcw.jil.color.ColorRGBA8888;
import me.lcw.jil.color.ColorYUV444;

public class ColorUtils {
  
  public static byte colorsToGrey(byte red, byte green, byte blue) {
    double r = (((red&0xff)*0.2126));//+(red&0xff));
    double g = (((green&0xff)*0.7152));//+(green&0xff));
    double b = (((blue&0xff)*0.0722));//+(blue&0xff));
    return (byte) Math.ceil((r+g+b));
  }
  
  public static double colorsToGrey(double red, double green, double blue) {
    double r = ((red*0.2126));//+(red&0xff));
    double g = ((green*0.7152));//+(green&0xff));
    double b = ((blue*0.0722));//+(blue&0xff));
    return r+g+b;
  }
  
  public static Color mergeColors(Color first, Color second) {
    double napct = (second.getAlphaPct()+(first.getAlphaPct()*(1-second.getAlphaPct())));
    byte na = (byte) Math.round(napct*255);
    byte nr = (byte) Math.round(((second.getRedPct()*second.getAlphaPct() + 
        first.getRedPct()*first.getAlphaPct()*(1-second.getAlphaPct()))/napct)*255);
    byte nb = (byte) Math.round(((second.getBluePct()*second.getAlphaPct() + 
        first.getBluePct()*first.getAlphaPct()*(1-second.getAlphaPct()))/napct)*255);
    byte ng = (byte) Math.round(((second.getGreenPct()*second.getAlphaPct() + 
        first.getGreenPct()*first.getAlphaPct()*(1-second.getAlphaPct()))/napct)*255);
    Color nC = new ColorRGBA8888(nr, ng, nb, na);
    return nC;
  }
  
//  public static ColorYUV444 RGBtoYUV444(final byte r, final byte g, final byte b) {
//    
//  }
}
