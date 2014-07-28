package org.java_lcw.jil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.java_lcw.jil.Image.ImageException;
import org.junit.Test;



public class ImageTest {
  
  
  public static int getSize() {
    return getSize(1, 500);
  }
  public static int getSize(int max) {
    return getSize(1, max);
  }
  public static int getSize(int min, int max) {
    Random rnd = new Random();
    int w = 0;
    if (max <= Byte.MAX_VALUE) {
      while (w < min || w > max) {
        w = rnd.nextInt() & 0xff;
      }
    } else if (max <= Short.MAX_VALUE) {
      while (w < min || w > max) {
        w = rnd.nextInt() & 0xffff;
      }
    } else {
      while (w < min || w > max) {
        w = rnd.nextInt();
      }
    }
    return w;
  }
  
  public static String hashFile(String filename) throws NoSuchAlgorithmException, IOException {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    FileInputStream fis = new FileInputStream(filename);
    byte[] dataBytes = new byte[1024];
    
    int nread = 0; 
    while ((nread = fis.read(dataBytes)) != -1) {
      md.update(dataBytes, 0, nread);
    };
    byte[] mdbytes = md.digest();
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < mdbytes.length; i++) {
      sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
    }
    fis.close();
    return sb.toString();
  }
  
  public static String hashByteArray(byte[] data) throws NoSuchAlgorithmException, IOException {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    md.update(data);
    byte[] mdbytes = md.digest();
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < mdbytes.length; i++) {
      sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
    }
    return sb.toString();
  }

  
 
  @Test
  public void colorTest() {
    Color c = new Color();
    c.setRed((byte)10);
    c.setBlue((byte)10);
    c.setGreen((byte)10);
    assertEquals(c.getRed(), 10);
    assertEquals(c.getBlue(), 10);
    assertEquals(c.getGreen(), 10);
    c.setAlpha((byte)10);
    assertEquals(c.getAlpha(), 10);
  }
  
  @Test(expected=IOException.class)
  public void exceptionTest() throws ImageException, IOException {
    Image.open("/bad/path/to/open.png");
  }
  
  @Test(expected=ImageException.class)
  public void exceptionTest2() throws ImageException, IOException {
    Image.fromByteArray(Image.MODE_RGB, 200, 200, new byte[2]);
  }
  
  @Test(expected=FileNotFoundException.class)
  public void exceptionTest3() throws ImageException, IOException {
    Image.open("TEST.BLAH");
  }
  
  @Test
  public void openTest1() throws ImageException, IOException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.jpg").getFile();
    Image.open(filename);
    Image.open(filename);
  }
  
  @Test
  public void modeChageTest() throws ImageException, IOException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    Image img = Image.open(filename);
    Image imgL = img.changeMode(Image.MODE_L);
    imgL.changeMode(Image.MODE_RGB);
    imgL.changeMode(Image.MODE_RGBA);
    Image imgRGB = img.changeMode(Image.MODE_RGB);
    imgRGB.changeMode(Image.MODE_RGBA);
    imgRGB.changeMode(Image.MODE_L);
    Image imgRGBA = img.changeMode(Image.MODE_RGBA);
    imgRGBA.changeMode(Image.MODE_L);
    imgRGBA.changeMode(Image.MODE_RGB);
  }
  
  @Test
  public void randomRGBAImageTest() throws IOException, ImageException {
    int h = getSize(40, 500);
    int w = getSize(40, 500);
    Image img = Image.create(Image.MODE_RGBA, w, h);
    img.mkRandom();
    assertEquals((img.getWidth()*img.getHeight()*img.getChannels()), img.toArray().length);  }
  
  @Test
  public void randomRGBImageTest() throws IOException, ImageException {
    int h = getSize(40, 500);
    int w = getSize(40, 500);
    Image img = Image.create(Image.MODE_RGB, w, h);
    img.mkRandom();
    assertEquals((img.getWidth()*img.getHeight()*img.getChannels()), img.toArray().length);
  }
  
  @Test
  public void randomLImageTest() throws IOException, ImageException {
    int h = getSize(40, 500);
    int w = getSize(40, 500);
    Image img = Image.create(Image.MODE_L, w, h);
    img.mkRandom();
    assertEquals((img.getWidth()*img.getHeight()*img.getChannels()), img.toArray().length);
  }

  @Test
  public void openTiffFile() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    Image img = Image.open(filename);
    img.save("/tmp/test.tiff");
    img = Image.open("/tmp/test.tiff");
    assertEquals("d1d0f3e8bf13708e56fe5ecb8c151402d489cf7595cf8d2e0026ba8e5fe17dae", hashByteArray(img.toArray()));
    File file = new File("/tmp/test.tiff");
    file.delete();
  }
  @Test
  public void openJPEGFile() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.jpg").getFile();
    Image img = Image.open(filename);
    assertEquals("2740469d3f7d98b6fbb713f12826395be721bf5bf58bb90b9118ea2b6b3505ff", hashByteArray(img.toArray()));
  }
  
  @Test
  public void saveJPEGFile() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    Image img = Image.open(filename);
    img.save("/tmp/test.jpg");
    File file = new File("/tmp/test.jpg");
    file.delete();
  }
  
  @Test
  public void openPNGFile() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    System.out.println(filename);
    Image img = Image.open(filename);
    assertEquals("c57c5fe4cf97763ecbded98e82ced7faee5138adb6bd68641d009b4f4ab4c975", hashByteArray(img.toArray()));
  }
  
  @Test
  public void savePNGFile() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    Image img = Image.open(filename);
    img.save("/tmp/test.png");
    File file = new File("/tmp/test.png");
    file.delete();
    assertEquals("c57c5fe4cf97763ecbded98e82ced7faee5138adb6bd68641d009b4f4ab4c975", hashByteArray(img.toArray()));
  }
  
  @Test
  public void fillColorTestRGB() throws ImageException, IOException, NoSuchAlgorithmException {
    Image img = Image.create(Image.MODE_RGB, 200, 400);
    Color c = new Color((byte)10, (byte)220, (byte)110);
    img.fillColor(c);
    assertEquals("63d760feb63b56590e374362ede227a3b0e560f0f1ee63773598f88ae06537e4", hashByteArray(img.toArray()));
  }
  @Test
  public void fillColorTestRGBA() throws ImageException, IOException, NoSuchAlgorithmException {
    Image img = Image.create(Image.MODE_RGBA, 1203, 1226);
    Color c = new Color((byte)223,(byte)101,(byte)30, (byte)240);
    img.save("/tmp/test.png");
    img.fillColor(c);
    assertEquals("93ecec7b6dacb67c7a5b7c27dca28880fe8fd5da5f2960f4bdd159e2ef2df7ce", hashByteArray(img.toArray()));
  }
  @Test
  public void fillColorTestL() throws ImageException, IOException, NoSuchAlgorithmException {
    Image img = Image.create(Image.MODE_L, 1440, 19887);
    Color c = new Color((byte)231);
    c.setGrey((byte)231);
    img.fillColor(c);
    assertEquals("164be1e3cd389318de8cce67781687ebda9e98571c7592c5ec65d12b40eae091", hashByteArray(img.toArray()));
  }

  
  @Test
  public void pasteImagesTestRGBA() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    Image img = Image.open(filename);
    filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    Image subImg = Image.open(filename);
    subImg = subImg.resize(img.getWidth()/3, img.getHeight()/3);
    img.paste(img.getWidth()/3, img.getHeight()/3, subImg);
    img.save("/tmp/test.png");
    assertEquals("75ea1d4c88532aa94bcd474b4ae21ec252a39147be7db196408f3d82a270c6fb", hashByteArray(img.toArray()));
  }

  @Test
  public void pasteImagesTestL() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    Image img = Image.open(filename);
    img = img.changeMode(Image.MODE_L);
    filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    Image subImg = Image.open(filename);
    subImg =  subImg.changeMode(Image.MODE_L);
    subImg = subImg.resize(img.getWidth()/3, img.getHeight()/3);
    img.paste(img.getWidth()/3, img.getHeight()/3, subImg);
    assertEquals("0f09ebbd7713ee60ba3bc9f721350845893518b5029dd3a8fc990d2db8241357", hashByteArray(img.toArray()));
  }
  
  @Test
  public void pasteImagesTestRGB() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    Image img = Image.open(filename);
    img = img.changeMode(Image.MODE_RGB);
    filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    Image subImg = Image.open(filename);
    subImg =  subImg.changeMode(Image.MODE_RGB);
    subImg = subImg.resize(img.getWidth()/3, img.getHeight()/3);
    img.paste(img.getWidth()/3, img.getHeight()/3, subImg);
    assertEquals("6b2f19c7fdeb85aef4043f2bda2bbbb127079abb3621088a669f818b99d421b7", hashByteArray(img.toArray()));
  }
  
  @Test
  public void pasteImagesTestOverSample() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    Image img = Image.open(filename);
    img = img.changeMode(Image.MODE_RGB);
    filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    Image subImg = Image.open(filename);
    subImg = subImg.changeMode(Image.MODE_RGB);
    img.paste(img.getWidth()/3, img.getHeight()/3, subImg);
    assertEquals("be1af56c3ac7b5fcaec60f837e0449ea0168630afb30deb519124aae8c408079", hashByteArray(img.toArray()));
  }
  
  
  @Test
  public void mergeImagesTestDiffColors() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    Image img = Image.open(filename);
    filename = ClassLoader.getSystemClassLoader().getResource("resources/testImageBW.png").getFile();
    Image subImg = Image.open(filename);
    subImg.save("/tmp/test1.png");
    subImg = subImg.changeMode(Image.MODE_L);
    subImg.save("/tmp/test2.png");
    subImg = subImg.changeMode(Image.MODE_RGB);
    subImg.save("/tmp/test3.png");
    //subImg = subImg.resize(150, 150);
    img.paste(img.getWidth()/3, img.getHeight()/3, subImg);
    img.save("/tmp/test.png");
    //assertEquals("2740469d3f7d98b6fbb713f12826395be721bf5bf58bb90b9118ea2b6b3505ff", hashByteArray(img.toArray()));
  }
  
  @Test
  public void scaleTestAspect() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    Image img = Image.open(filename);
    double ratio = img.getWidth()/(double)img.getHeight();
    img = img.resize(1442, 1800, true);
    double newRatio = img.getWidth()/(double)img.getHeight();
    assertTrue(.02 > Math.abs((ratio - newRatio)));
    assertEquals("e6a976f003e228d3d8b2b5d720d38d73081d680dc77037c1a0067a657c9c4677", ImageTest.hashByteArray(img.toArray()));
  }
  
  @Test
  public void scaleTestAspect2() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    Image img = Image.open(filename);
    double ratio = img.getWidth()/(double)img.getHeight();
    img = img.resize(1800, 420, true);
    double newRatio = img.getWidth()/(double)img.getHeight();
    assertTrue(.02 > Math.abs((ratio - newRatio)));
    assertEquals("a7364c646968aeb1e192922c40226b84bd51dc92781a20a2baf8ef107222ec9c", ImageTest.hashByteArray(img.toArray()));
  }
  
  @Test
  public void scaleTestRGBA() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    Image img = Image.open(filename);
    img = img.resize(640, 480, false);
    assertEquals("b03185220896e6e7672664cda9777924df509a0c740c7a28f1550d172ac31773", hashByteArray(img.toArray()));
    img = img.resize(200, 480, false);
    assertEquals("6780546dc43dd72de1e09dfe23372c2178397fc0969c4a0f5bbbcff06e7648e1", hashByteArray(img.toArray()));
  }
  
  @Test
  public void scaleTestRGB() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    Image img = Image.open(filename);
    img = img.changeMode(Image.MODE_RGB);
    img = img.resize(640, 480, false);
    assertEquals("2705868b18f63cfbc6d6bbab6c4b56a0a3dac6f0d03f4fe9080568bf0bd34a5a", hashByteArray(img.toArray()));
    img = img.resize(200, 480, false);
    assertEquals("4030bb05b38efa7e662c8310b4c09fa6b5e39d4439a7b581d94dd5e7c1633ad1", hashByteArray(img.toArray()));
  }
  
  
  @Test
  public void scaleTestL() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    Image img = Image.open(filename);
    img = img.changeMode(Image.MODE_L);
    img = img.resize(640, 480, false);
    assertEquals("b167ffd61ac3ac06905d38b65d03a953f6b073be66670557444882437a1255ad", hashByteArray(img.toArray()));
    img = img.resize(200, 480, false);
    assertEquals("03a889dc23d588dcda9077dae30d032f12d20a374b60dc2dd58d20a7eb3be7d6", hashByteArray(img.toArray()));
  }  
  
  @Test
  public void scaleBiliniar() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    Image img = Image.open(filename);
    //img.save("/tmp/testFull.png");
    img = img.changeMode(Image.MODE_RGB);
    img = img.resize(640, 480, true, Image.ScaleType.LINER);
    assertEquals("1364572b971be93d082c420ce4bd3594d6ae5d252bcec1edc5e1b6b5bea64aaf", hashByteArray(img.toArray()));
    img = img.resize(200, 480, false);
    assertEquals("0edcbf8eab4103991907d61e71bf47b1451182f7bd88cec462c2608c3034203d", hashByteArray(img.toArray()));
  }  
  
  @Test
  public void scaleBiCubic() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    Image img = Image.open(filename);
    Image newimg = img.resize((int)(img.getWidth()*1.5), (int)(img.getHeight()*1.5), true, Image.ScaleType.CUBIC);
    assertEquals("82b4962155e8b7debec4523851fad39412151d222886ff55da2b4fdfd02cc0fc", hashByteArray(newimg.toArray()));
    newimg = img.resize(640, img.getHeight()/2, true, Image.ScaleType.CUBIC);
    assertEquals("395b6e4850c66dab6c848097f17aa37a96839b128c322c17d6cd88ce00da55cb", hashByteArray(newimg.toArray()));
    newimg = img.resize(200, 480, false, Image.ScaleType.CUBIC);
    assertEquals("4960701619ed06260707ff4d08e13eec9787107c493761340bed18d9857a5e69", hashByteArray(newimg.toArray()));
  }
  
  @Test
  public void cutImageTest() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    Image img = Image.open(filename);
    Image newImg = img.cut(0, 0, img.getWidth(), img.getHeight());
    assertEquals(hashByteArray(img.toArray()), hashByteArray(newImg.toArray()));
    newImg = img.cut(20, 20, 500, 500);
    assertEquals("c88577ff4c2e4ade9e1919c22ede1e01a1269bd1b996d8e5ae4ceac203e17e93", hashByteArray(newImg.toArray()));
    newImg = img.cut(0, 0, 200, 50);
    assertEquals("54e9c46019df5ef9e02a82c22f2b4c950d20c6cfa27e790a72c4b41255e96077", hashByteArray(newImg.toArray()));
  }  
  
  @Test(expected=ImageException.class)
  public void badCutImageTest() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    Image img = Image.open(filename);
    img.cut(0, 0, img.getWidth()+10, img.getHeight()+10);
  }  
  
  @Test
  public void copyImageTest() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    Image img = Image.open(filename);
    Image newImg = img.copy();
    assertEquals(hashByteArray(img.toArray()), hashByteArray(newImg.toArray()));
  }  
  
  @Test
  public void testAlphaMerge() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    Image img = Image.open(filename);
    img = img.resize(640, 480, false);
    img.save("/tmp/test1.png");
    String filename2 = ClassLoader.getSystemClassLoader().getResource("resources/cursor.png").getFile();
    Image img2 = Image.open(filename2);
    img2 = img2.resize(20, 20);
    img.paste(10, 10, img2, true);
    img.paste(200, 10, img2, true);
    img.save("/tmp/test2.png");
    assertEquals("502b04b1dba3dd53a5c9ad16041f369128c60ccece136bd1844ed41c680265e2", hashByteArray(img.toArray()));
  } 
  
}