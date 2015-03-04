package org.java_lcw.jil.ji;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.java_lcw.jil.Image;
import org.java_lcw.jil.ImageException;
import org.java_lcw.jil.JavaImage;
import org.java_lcw.jil.ResizeTests;
import org.java_lcw.jil.TestUtils;

public class NNScaleTests extends ResizeTests{
  
  @Override
  public void start() throws Exception {
    scaleType = Image.ScaleType.NN;
    imgRGBA = JavaImage.open(filename).changeMode(Image.MODE_RGBA);
    imgRGB = JavaImage.open(filename).changeMode(Image.MODE_RGB);
    imgL = JavaImage.open(filename).changeMode(Image.MODE_L);
  }
  
  @Override
  public void NoAspectScaleUp() throws Exception {
    super.NoAspectScaleUp();
    assertEquals("3d964d77d776f0b2a8b14271965f09ece09937ac72c84340cca8719a3df78f33", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("a1c0e5908e6e5b347d23582f3063372522b7b70302a7dd5b7e643f18516be269", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("76fac7f2f190c39e39c8975a96bd95c0d6d486afcb2edcf8240b445b90e39dba", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void NoAspectScaleDown() throws Exception {
    super.NoAspectScaleDown();
    assertEquals("a38e617fc06df2a5083f6137b73d3ef62448c45153ce576093e803d1a0cede74", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("f56d2f0ef1b4c2726678370bbd8b6b949443265111d6a7c753b6168643fb2042", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("dd552b61f8793ff06d104197ab2779edc69164e741054e64ccf20994c5e71e78", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void NoAspectUpHeight() throws Exception {
    super.NoAspectUpHeight();
    assertEquals("e99c10cf4a670c1efc64161463c38e432651400b248cb7dcfb7c8bf7fa1f70f0", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("405d65fc5d1c164ff6b4380060a366f7a0d3367eb1f486564cc6ebfeca3065d3", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("637eb014d2650ff71ef95b319d6673c68e4a81fde8694e0dda6d3fc1da6aad42", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void NoAspectUpWidth() throws Exception {
    super.NoAspectUpWidth();
    assertEquals("a8d2c66ff8c356b87d3f4eb6d408543957c6f679d998afb7bf79f3745f7786a9", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("5715ab8dca2a2514eecc5f8041b0dbce886be19a483b9d8e59da0f9149cba828", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("70d44ed9a99febecc403727424f8210b4d2bb2db98eaa240918fbe7f2306c3b5", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectScaleUp() throws Exception {
    super.AspectScaleUp();
    assertEquals("f4315a33062abde8b6b77deefb20de2133ceecf5a4bd4130eab7a3418ab306e1", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("fa8970c81a6d9f06f8085846d608bc824baa3ed22a8806874ea8fd75fa042ecb", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("098eff8f30451ad5653f072d63f022c7edfa9bd64ae1257680c3710d835808ab", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectScaleDown() throws Exception {
    super.AspectScaleDown();
    assertEquals("01c632fb3f62c68059966723be5ff7efa2220f2e91ff7ab39a60be3a603684e3", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("08f1d1bd970a04017699513f0f9eb579918a3a76e0b2712af4a027809d580679", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("ab9ef274e64ad6042c0bb152334b0658d352894ecdc6111432d98b748a48e6c2", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectUpHeight() throws Exception {
    super.AspectUpHeight();
    assertEquals("94259f95e7df2b1a1ec0379cbbad8e920714931083591d634a815199f81f3686", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("d70729307e087d2d8d12d38c70077371cc1e9861dfcc063a3cf708e68892c1df", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("b9a852df2d78ee380fce0d3c7b392327b5a87f377192802e74d288bdaa3ae9dd", TestUtils.hashByteArray(subImgL.getArray()));
  }
  
  @Override
  public void AspectUpWidth() throws Exception {
    super.AspectUpWidth();
//    System.out.println(TestUtils.hashByteArray(subImgRGBA.getArray()));
//    System.out.println(TestUtils.hashByteArray(subImgRGB.getArray()));
//    System.out.println(TestUtils.hashByteArray(subImgL.getArray()));
    assertEquals("01c632fb3f62c68059966723be5ff7efa2220f2e91ff7ab39a60be3a603684e3", TestUtils.hashByteArray(subImgRGBA.getArray()));
    assertEquals("08f1d1bd970a04017699513f0f9eb579918a3a76e0b2712af4a027809d580679", TestUtils.hashByteArray(subImgRGB.getArray()));
    assertEquals("ab9ef274e64ad6042c0bb152334b0658d352894ecdc6111432d98b748a48e6c2", TestUtils.hashByteArray(subImgL.getArray()));
  }

}
