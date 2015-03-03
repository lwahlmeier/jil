package org.java_lcw.jil.awt;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.java_lcw.jil.AWTImage;
import org.java_lcw.jil.Color;
import org.java_lcw.jil.Image;
import org.java_lcw.jil.ImageException;
import org.java_lcw.jil.PasteTests;
import org.java_lcw.jil.TestUtils;
import org.junit.Before;

public class AlphaWithMergePasteTests extends PasteTests {

  @Override
  @Before
  public void start() throws ImageException, IOException{
    alpha = true;
    img = AWTImage.open(filename);
    subImg = AWTImage.open(filename2);
    img = img.changeMode(Image.MODE_RGBA);
    subImg = subImg.changeMode(Image.MODE_RGBA);
    subImg = subImg.toJavaImage().resize(300, 300);
    addAlphaToImage((byte)100, subImg);

  }
  
  @Override
  public void biggerPasteImage() throws Exception {
    subImg2 = AWTImage.create(Image.MODE_RGBA, 4304, 4024).toJavaImage();
    subImg2.fillColor(Color.BLACK);
    addAlphaToImage((byte)100, subImg2);
    super.biggerPasteImage();
    
    assertEquals("dadbd550a8a00a252fc8166e4d3397ad7c8fcb3ea3ca553adde94d30021ef065", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void normal() throws Exception {
    super.normal();
    assertEquals("dabbc970c258b116e2e9eed31c9fdbf7dc13c1f3e4e31556f737d3ed3ea814de", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void underShootY() throws Exception {
    super.underShootY();
    
    assertEquals("016bb8150b2921ae54f16b4487844fed9add93f2a75319d3c25f81ae582b6eca", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void underShootX() throws Exception {
    super.underShootX();
    
    assertEquals("f4f059c6482ef41039c238a1d2218a155ada95db5e3508497f817bc8960cb6ac", TestUtils.hashByteArray(img.getArray()));
  }
  
  public void overShootX() throws Exception {
    super.overShootX();
    
    assertEquals("f8fd6282953b75e6ac0ee294d5d272eeaa44bcd6e44540f13a335906efb6b445", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void overShootY() throws Exception {
    super.overShootY();
    
    assertEquals("48625556c8307a91d532beeb88547b79ab182db0c9a5468e2ef3c1aad0ec63b1", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void overShootBoth() throws Exception {
    super.overShootBoth();
    
    assertEquals("6b1bac5a53f69eb6cfbe30746613648b53958e9aada7b288a5bc241b6c031959", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void underShootBoth() throws Exception {
    super.underShootBoth();
    assertEquals("1a8ef51a204e9914e3426a7dd012e048eb9b8b41015b0cacb0e08187126fbdde", TestUtils.hashByteArray(img.getArray()));
  }
}
