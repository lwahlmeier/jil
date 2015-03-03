package org.java_lcw.jil.ji;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.java_lcw.jil.Color;
import org.java_lcw.jil.ImageException;
import org.java_lcw.jil.JavaImage;
import org.java_lcw.jil.TestUtils;
import org.junit.Test;



public class JavaImageTests {
  
  @Test(expected=IOException.class)
  public void exceptionTest() throws ImageException, IOException {
    JavaImage.open("/bad/path/to/open.png");
  }
  
  @Test(expected=RuntimeException.class)
  public void exceptionTest2() throws ImageException, IOException {
    JavaImage.fromByteArray(JavaImage.MODE_RGB, 200, 200, new byte[2]);
  }
  
  @Test(expected=FileNotFoundException.class)
  public void exceptionTest3() throws ImageException, IOException {
    JavaImage.open("TEST.BLAH");
  }
  
  @Test
  public void openTest1() throws ImageException, IOException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.jpg").getFile();
    JavaImage.open(filename);
    JavaImage.open(filename);
  }
  
  @Test
  public void modeChageTest() throws ImageException, IOException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    JavaImage img = JavaImage.open(filename);
    JavaImage imgL = img.changeMode(JavaImage.MODE_L);
    imgL.changeMode(JavaImage.MODE_RGB);
    imgL.changeMode(JavaImage.MODE_RGBA);
    JavaImage imgRGB = img.changeMode(JavaImage.MODE_RGB);
    imgRGB.changeMode(JavaImage.MODE_RGBA);
    imgRGB.changeMode(JavaImage.MODE_L);
    JavaImage imgRGBA = img.changeMode(JavaImage.MODE_RGBA);
    imgRGBA.changeMode(JavaImage.MODE_L);
    imgRGBA.changeMode(JavaImage.MODE_RGB);
  }
  
  @Test
  public void randomRGBAImageTest() throws IOException, ImageException {
    int h = TestUtils.getSize(40, 500);
    int w = TestUtils.getSize(40, 500);
    JavaImage img = JavaImage.create(JavaImage.MODE_RGBA, w, h);
    img.mkRandom();
    assertEquals((img.getWidth()*img.getHeight()*img.getColors()), img.getArray().length);  }
  
  @Test
  public void randomRGBImageTest() throws IOException, ImageException {
    int h = TestUtils.getSize(40, 500);
    int w = TestUtils.getSize(40, 500);
    JavaImage img = JavaImage.create(JavaImage.MODE_RGB, w, h);
    img.mkRandom();
    assertEquals((img.getWidth()*img.getHeight()*img.getColors()), img.getArray().length);
  }
  
  @Test
  public void randomLImageTest() throws IOException, ImageException {
    int h = TestUtils.getSize(40, 500);
    int w = TestUtils.getSize(40, 500);
    JavaImage img = JavaImage.create(JavaImage.MODE_L, w, h);
    img.mkRandom();
    assertEquals((img.getWidth()*img.getHeight()*img.getColors()), img.getArray().length);
  }

  @Test
  public void openTiffFile() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    JavaImage img = JavaImage.open(filename);
    img.save("/tmp/test.tiff");
    img = JavaImage.open("/tmp/test.tiff");
    assertEquals("d1d0f3e8bf13708e56fe5ecb8c151402d489cf7595cf8d2e0026ba8e5fe17dae", TestUtils.hashByteArray(img.getArray()));
    File file = new File("/tmp/test.tiff");
    file.delete();
  }
  
  @Test
  public void openJPEGFile() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.jpg").getFile();
    JavaImage img = JavaImage.open(filename);
    img.save("/tmp/test.png");
    assertEquals("3ba178edbaab22b850174f17989144f74b340c90543abb911f3441e5ad8b357b",TestUtils.hashByteArray(img.getArray()));
  }
  
  @Test
  public void saveJPEGFile() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    JavaImage img = JavaImage.open(filename);
    img.save("/tmp/test.jpg");
    File file = new File("/tmp/test.jpg");
    file.delete();
  }
  
  @Test
  public void openPNGFile() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    JavaImage img = JavaImage.open(filename);
    assertEquals("c57c5fe4cf97763ecbded98e82ced7faee5138adb6bd68641d009b4f4ab4c975", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Test
  public void savePNGFile() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    JavaImage img = JavaImage.open(filename);
    img.save("/tmp/test.png");
    File file = new File("/tmp/test.png");
    file.delete();
    assertEquals("c57c5fe4cf97763ecbded98e82ced7faee5138adb6bd68641d009b4f4ab4c975", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Test
  public void fillColorTestRGB() throws ImageException, IOException, NoSuchAlgorithmException {
    JavaImage img = JavaImage.create(JavaImage.MODE_RGB, 200, 400);
    Color c = new Color((byte)10, (byte)220, (byte)110);
    img.fillColor(c);
    assertEquals("a745a437a586082d12e79f1d4a94550aec33adc7d08c6da682a669f1fb638569", TestUtils.hashByteArray(img.getArray()));
  }
  @Test
  public void fillColorTestRGBA() throws ImageException, IOException, NoSuchAlgorithmException {
    JavaImage img = JavaImage.create(JavaImage.MODE_RGBA, 1203, 1226);
    Color c = new Color((byte)223,(byte)101,(byte)30, (byte)240);
    img.fillColor(c);
    assertEquals("fa61315bf8ed035d399b528207b5cbe04beed45631ae98b1f84136113f94eb38", TestUtils.hashByteArray(img.getArray()));
  }
  @Test
  public void fillColorTestL() throws ImageException, IOException, NoSuchAlgorithmException {
    JavaImage img = JavaImage.create(JavaImage.MODE_L, 1440, 19887);
    Color c = new Color((byte)231);
    c.setGrey((byte)231);
    img.fillColor(c);
    assertEquals("164be1e3cd389318de8cce67781687ebda9e98571c7592c5ec65d12b40eae091", TestUtils.hashByteArray(img.getArray()));
  }

  
  @Test
  public void pasteImagesTestRGBA() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    JavaImage img = JavaImage.open(filename);
    filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    JavaImage subImg = JavaImage.open(filename);
    subImg = subImg.resize(img.getWidth()/3, img.getHeight()/3);
    img.paste(img.getWidth()/3, img.getHeight()/3, subImg);
    img.save("/tmp/test.png");
    assertEquals("75ea1d4c88532aa94bcd474b4ae21ec252a39147be7db196408f3d82a270c6fb", TestUtils.hashByteArray(img.getArray()));
  }
  
  
  @Test
  public void pasteImagesTestRGBA2() throws ImageException, IOException, NoSuchAlgorithmException {

  }

  @Test
  public void pasteImagesTestL() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    JavaImage img = JavaImage.open(filename);
    img = img.changeMode(JavaImage.MODE_L);
    filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    JavaImage subImg = JavaImage.open(filename);
    subImg =  subImg.changeMode(JavaImage.MODE_L);
    subImg = subImg.resize(img.getWidth()/3, img.getHeight()/3);
    img.paste(img.getWidth()/3, img.getHeight()/3, subImg);
    assertEquals("0f09ebbd7713ee60ba3bc9f721350845893518b5029dd3a8fc990d2db8241357", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Test
  public void pasteImagesTestRGB() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    JavaImage img = JavaImage.open(filename);
    img = img.changeMode(JavaImage.MODE_RGB);
    filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    JavaImage subImg = JavaImage.open(filename);
    subImg =  subImg.changeMode(JavaImage.MODE_RGB);
    subImg = subImg.resize(img.getWidth()/3, img.getHeight()/3);
    img.paste(img.getWidth()/3, img.getHeight()/3, subImg);
    assertEquals("6b2f19c7fdeb85aef4043f2bda2bbbb127079abb3621088a669f818b99d421b7", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Test
  public void pasteImagesTestOverSample() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    JavaImage img = JavaImage.open(filename);
    img = img.changeMode(JavaImage.MODE_RGB);
    filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    JavaImage subImg = JavaImage.open(filename);
    subImg = subImg.changeMode(JavaImage.MODE_RGB);
    img.paste(img.getWidth()/3, img.getHeight()/3, subImg);
    assertEquals("be1af56c3ac7b5fcaec60f837e0449ea0168630afb30deb519124aae8c408079", TestUtils.hashByteArray(img.getArray()));
  }
  
  
  //@Test
  public void mergeImagesTestDiffColors() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    JavaImage img = JavaImage.open(filename);
    filename = ClassLoader.getSystemClassLoader().getResource("resources/testImageBW.png").getFile();
    JavaImage subImg = JavaImage.open(filename);
    subImg = subImg.changeMode(JavaImage.MODE_L);
    subImg = subImg.changeMode(JavaImage.MODE_RGB);
    //subImg = subImg.resize(150, 150);
    img.paste(img.getWidth()/3, img.getHeight()/3, subImg);

    //assertEquals("2740469d3f7d98b6fbb713f12826395be721bf5bf58bb90b9118ea2b6b3505ff", hashByteArray(img.toArray()));
  }
  
  @Test
  public void scaleTestAspect() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    JavaImage img = JavaImage.open(filename);
    double ratio = img.getWidth()/(double)img.getHeight();
    img = img.resize(1442, 1800, true);
    double newRatio = img.getWidth()/(double)img.getHeight();
    img.save("/tmp/test.png");
    assertTrue(.02 > Math.abs((ratio - newRatio)));
    assertEquals("e6a976f003e228d3d8b2b5d720d38d73081d680dc77037c1a0067a657c9c4677", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Test
  public void scaleTestAspect2() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    JavaImage img = JavaImage.open(filename);
    double ratio = img.getWidth()/(double)img.getHeight();
    img = img.resize(1800, 420, true);
    double newRatio = img.getWidth()/(double)img.getHeight();
    assertTrue(.02 > Math.abs((ratio - newRatio)));
    assertEquals("a7364c646968aeb1e192922c40226b84bd51dc92781a20a2baf8ef107222ec9c", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Test
  public void scaleTestRGBA() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    JavaImage img = JavaImage.open(filename);
    JavaImage newimg = img.resize((int)(img.getWidth()*1.5), (int)(img.getHeight()*1.5), true);
    assertEquals("41c927ebd731eaac71807ba5954bffec3e699573ec2ce68e55032eac195cca6c", TestUtils.hashByteArray(newimg.getArray()));
    newimg = img.resize(640, img.getHeight()/2, true);
    assertEquals("3be385c3ee1189a86248eedcbedbb1143c1ece24ad9c045442bc60e5138c928e", TestUtils.hashByteArray(newimg.getArray()));
    newimg = img.resize(200, 480, false);
    assertEquals("e4c249a9912c8f04cbbd3c94c140ae64205d1fcda0a9351801c14bbd4be38a1e", TestUtils.hashByteArray(newimg.getArray()));
  }
  
  @Test
  public void scaleTestRGB() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    JavaImage img = JavaImage.open(filename);
    img = img.changeMode(JavaImage.MODE_RGB);
    img = img.resize(640, 480, false);
    assertEquals("2705868b18f63cfbc6d6bbab6c4b56a0a3dac6f0d03f4fe9080568bf0bd34a5a", TestUtils.hashByteArray(img.getArray()));
    img = img.resize(200, 480, false);
    assertEquals("4030bb05b38efa7e662c8310b4c09fa6b5e39d4439a7b581d94dd5e7c1633ad1", TestUtils.hashByteArray(img.getArray()));
  }
  
  
  @Test
  public void scaleTestL() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage3.png").getFile();
    JavaImage img = JavaImage.open(filename);
    img = img.changeMode(JavaImage.MODE_L);
    img = img.resize(640, 480, false);
    assertEquals("b167ffd61ac3ac06905d38b65d03a953f6b073be66670557444882437a1255ad", TestUtils.hashByteArray(img.getArray()));
    img = img.resize(200, 480, false);
    assertEquals("03a889dc23d588dcda9077dae30d032f12d20a374b60dc2dd58d20a7eb3be7d6", TestUtils.hashByteArray(img.getArray()));
  }  
  
  @Test
  public void scaleBiliniar() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    JavaImage img = JavaImage.open(filename);
    //img.save("/tmp/testFull.png");
    img = img.changeMode(JavaImage.MODE_RGB);
    img = img.resize(640, 480, true, JavaImage.ScaleType.LINER);
    assertEquals("1364572b971be93d082c420ce4bd3594d6ae5d252bcec1edc5e1b6b5bea64aaf", TestUtils.hashByteArray(img.getArray()));
    img = img.resize(200, 480, false);
    assertEquals("0edcbf8eab4103991907d61e71bf47b1451182f7bd88cec462c2608c3034203d", TestUtils.hashByteArray(img.getArray()));
  }  
  
  @Test
  public void scaleBiCubic() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    JavaImage img = JavaImage.open(filename);
    JavaImage newimg = img.resize((int)(img.getWidth()*1.5), (int)(img.getHeight()*1.5), true, JavaImage.ScaleType.CUBIC);
    assertEquals("82b4962155e8b7debec4523851fad39412151d222886ff55da2b4fdfd02cc0fc", TestUtils.hashByteArray(newimg.getArray()));
    newimg = img.resize(640, img.getHeight()/2, true, JavaImage.ScaleType.CUBIC);
    assertEquals("395b6e4850c66dab6c848097f17aa37a96839b128c322c17d6cd88ce00da55cb", TestUtils.hashByteArray(newimg.getArray()));
    newimg = img.resize(200, 480, false, JavaImage.ScaleType.CUBIC);
    assertEquals("4960701619ed06260707ff4d08e13eec9787107c493761340bed18d9857a5e69", TestUtils.hashByteArray(newimg.getArray()));
  }
  
  @Test
  public void cutImageTest() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    JavaImage img = JavaImage.open(filename);
    JavaImage newImg = img.cut(0, 0, img.getWidth(), img.getHeight());
    assertEquals(TestUtils.hashByteArray(img.getArray()), TestUtils.hashByteArray(newImg.getArray()));
    newImg = img.cut(20, 20, 500, 500);
    assertEquals("c88577ff4c2e4ade9e1919c22ede1e01a1269bd1b996d8e5ae4ceac203e17e93", TestUtils.hashByteArray(newImg.getArray()));
    newImg = img.cut(0, 0, 200, 50);
    assertEquals("54e9c46019df5ef9e02a82c22f2b4c950d20c6cfa27e790a72c4b41255e96077", TestUtils.hashByteArray(newImg.getArray()));
  }  
  
  @Test
  public void copyImageTest() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    JavaImage img = JavaImage.open(filename);
    JavaImage newImg = img.copy();
    assertEquals(TestUtils.hashByteArray(img.getArray()), TestUtils.hashByteArray(newImg.getArray()));
  }  
  
  @Test
  public void testAlphaMerge() throws ImageException, IOException, NoSuchAlgorithmException {
    String filename = ClassLoader.getSystemClassLoader().getResource("resources/testImage2.png").getFile();
    JavaImage img = JavaImage.open(filename);
    img = img.resize(640, 480, false);
    String filename2 = ClassLoader.getSystemClassLoader().getResource("resources/cursor.png").getFile();
    JavaImage img2 = JavaImage.open(filename2);
    img2 = img2.resize(20, 20);
    img.merge(10, 10, img2);
    img.merge(200, 10, img2);
    assertEquals("502b04b1dba3dd53a5c9ad16041f369128c60ccece136bd1844ed41c680265e2", TestUtils.hashByteArray(img.getArray()));
  } 
  
}