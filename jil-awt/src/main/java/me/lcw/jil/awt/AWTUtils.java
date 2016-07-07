package me.lcw.jil.awt;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import me.lcw.jil.Color;
import me.lcw.jil.Utils.JilUtils;

public class AWTUtils {


  public static byte[] bufferedImageToByteArray(BufferedImage bufferedImage) {
    if(bufferedImage.getType() == BufferedImage.TYPE_BYTE_GRAY) {
      return ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
    }
    return JilUtils.intsToBytes(bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null , 0, bufferedImage.getWidth()), (byte) 32);
  }

  
  public static java.awt.Color toAWTColor(Color c) {
    return new java.awt.Color( c.getRed()&0xff, c.getGreen()&0xff, c.getBlue()&0xff, c.getAlpha()&0xff);
  }

  public static Color toJILColor(java.awt.Color c) {
    return Color.fromRGBAValue( (byte)c.getRed(), (byte)c.getGreen(), (byte)c.getBlue(), (byte)c.getAlpha());
  }
  
  
//  public static JilImage fromBufferedImage(BufferedImage BI) {
//    JilImage img;
//    if (BI.getType() == BufferedImage.TYPE_BYTE_GRAY) {
//      img = JilImage.fromByteArray(MODE_L, BI.getWidth(), BI.getHeight(), Utils.bufferedImageToByteArray(BI));
//    } else if(BI.getType() == BufferedImage.TYPE_4BYTE_ABGR) {
//      img = JilImage.fromByteArray(MODE_RGBA, BI.getWidth(), BI.getHeight(), Utils.bufferedImageToByteArray(BI));
//    } else if(BI.getType() == BufferedImage.TYPE_3BYTE_BGR) {
//      img = JilImage.fromByteArray(MODE_RGBA, BI.getWidth(), BI.getHeight(), Utils.bufferedImageToByteArray(BI)).changeMode(MODE_RGB);
//    } else {
//      img = JilImage.fromByteArray(MODE_RGBA, BI.getWidth(), BI.getHeight(), Utils.bufferedImageToByteArray(BI));
//    }
//    return img;
//  }
}
