package me.lcw.jil.ji;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import me.lcw.jil.Color;
import me.lcw.jil.Image;
import me.lcw.jil.ImageException;
import me.lcw.jil.JilImage;
import me.lcw.jil.PasteTests;
import me.lcw.jil.TestUtils;

import org.junit.Before;

public class AlphaNoMergePasteTests extends PasteTests {

  @Override
  @Before
  public void start() throws ImageException, IOException{
    img = JilImage.open(filename);
    subImg = JilImage.open(filename2);
    subImg = subImg.changeMode(Image.MODE_RGBA);
    subImg = subImg.resize(300, 300, true, Image.ScaleType.NN);
  }
  
  @Override
  public void biggerPasteImage() throws Exception {
    subImg2 = JilImage.create(Image.MODE_RGBA, 4304, 4024);
    subImg2.fillImageWithColor(Color.BLACK);
    super.biggerPasteImage();
    assertEquals("8359ae3b89f24240d8abdbd443808c9147e9cbb843bbe70b6556eb902be6cfef", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void normal() throws Exception {
    //We add alpha value to make sure its not merged, but instead just over written
    addAlphaToImage((byte)100, subImg);
    super.normal();
    img.save("/tmp/test.png");
    assertEquals("2ad9b9b9b5a72383db959d90f3e585182052f1fd67e2e493ae49ada54a7c716b", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void underShootY() throws Exception {
    super.underShootY();
    assertEquals("486b4fd69c2a51c23363a9197b82a7090c3fa382fc63ee33e425ad9f19eab6e9", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void underShootX() throws Exception {
    super.underShootX();
    assertEquals("c22de96063def07a302961da7b2601a27c2b74510ec3cd11edc2531257a48f2e", TestUtils.hashByteArray(img.getArray()));
  }
  
  public void overShootX() throws Exception {
    super.overShootX();
    assertEquals("cb3705a2713948d5b52c47ab916f7913937842d21aaf474fbc52b1cba28e6212", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void overShootY() throws Exception {
    super.overShootY();
    assertEquals("d415660ef3b3df478d2fd76b8301d7ccdad06957d617f15561378b1e7e191460", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void overShootBoth() throws Exception {
    super.overShootBoth();
    assertEquals("5baed67a9e93e74e31f0380e8906d280271a3596dcb234c298de27e1f2b10563", TestUtils.hashByteArray(img.getArray()));
  }
  
  @Override
  public void underShootBoth() throws Exception {
    super.underShootBoth();
    assertEquals("8d57db74f7b80be234de72f3e8f36c5c4f60ebcd8f0720fa169d0191cd2fbd7e", TestUtils.hashByteArray(img.getArray()));
  }
}
