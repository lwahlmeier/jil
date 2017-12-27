package me.lcw.jil.parsers.png;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

import me.lcw.jil.BaseImage.ImageType;
import me.lcw.jil.BaseImage.ImageMode;
import me.lcw.jil.Color;
import me.lcw.jil.JilImage;
import me.lcw.jil.TestUtils;

public class PNGEncodeTest {

	
	@Test
	public void simpleEncodeRGBFileTest() throws IOException, NoSuchAlgorithmException {
		JilImage ji = JilImage.create(ImageMode.RGB24, 200, 200);
		ji.fillImageWithColor(Color.BLUE);
		File f = File.createTempFile("test1", "png");
		ji.save(f.getAbsolutePath(), ImageType.PNG);
		RandomAccessFile raf = new RandomAccessFile(f, "r");
		byte[] ba = new byte[(int)raf.length()];
		raf.read(ba);
		raf.close();
		System.out.println(TestUtils.hashByteArray(ba));
		assertEquals("fc6b9674363a591086976768d142c4d01c491beb39cf4ec27044443cebce5f75", TestUtils.hashByteArray(ba));
		f.delete();
	}
	
	@Test
	public void simpleEncodeRGBTest() throws IOException, NoSuchAlgorithmException {
		JilImage ji = JilImage.create(ImageMode.RGB24, 200, 200);
		ji.fillImageWithColor(Color.BLUE);
		byte[] ba = PNGEncoder.encode(9, ji);
		System.out.println(TestUtils.hashByteArray(ba));
		assertEquals("1bef11dfddad1877265554c6beb0c8a04c5641b93e8caaf42d172537f528edb1", TestUtils.hashByteArray(ba));
	}
	
	@Test
	public void simpleEncodeRGBAFileTest() throws IOException, NoSuchAlgorithmException {
		JilImage ji = JilImage.create(ImageMode.RGBA32, 200, 200);
		Color c = Color.BLUE.changeAlpha((byte)100);
		ji.fillImageWithColor(c);
		File f = File.createTempFile("test1", "png");
		ji.save(f.getAbsolutePath(), ImageType.PNG);
		RandomAccessFile raf = new RandomAccessFile(f, "r");
		byte[] ba = new byte[(int)raf.length()];
		raf.read(ba);
		raf.close();
		System.out.println(TestUtils.hashByteArray(ba));
		assertEquals("e43634b126544d980edddd0b5399492a24113a98beb1edbf5c1ce61106a5ddb8", TestUtils.hashByteArray(ba));
		f.delete();
	}
	
	@Test
	public void simpleEncodeRGBATest() throws IOException, NoSuchAlgorithmException {
		JilImage ji = JilImage.create(ImageMode.RGBA32, 200, 200);
		Color c = Color.BLUE.changeAlpha((byte)100);
		ji.fillImageWithColor(c);
		byte[] ba = PNGEncoder.encode(ji);
		System.out.println(TestUtils.hashByteArray(ba));
		assertEquals("e43634b126544d980edddd0b5399492a24113a98beb1edbf5c1ce61106a5ddb8", TestUtils.hashByteArray(ba));
	}
	
	@Test
	public void simpleEncodeLFileTest() throws IOException, NoSuchAlgorithmException {
		JilImage ji = JilImage.create(ImageMode.GREY8, 200, 200);
		Color c = Color.BLUE.changeAlpha((byte)100);
		ji.fillImageWithColor(c);
		File f = File.createTempFile("test1", "png");
		ji.save(f.getAbsolutePath(), ImageType.PNG);
		RandomAccessFile raf = new RandomAccessFile(f, "r");
		byte[] ba = new byte[(int)raf.length()];
		raf.read(ba);
		raf.close();
		System.out.println(TestUtils.hashByteArray(ba));
		assertEquals("fe7bb20c22789ec0715ffb7cdbff8c786fa93a2a608ade47a553f4fc141c5a12", TestUtils.hashByteArray(ba));
		f.delete();
	}
	
	@Test
	public void simpleEncodeLTest() throws IOException, NoSuchAlgorithmException {
		JilImage ji = JilImage.create(ImageMode.GREY8, 200, 200);
		Color c = Color.BLUE.changeAlpha((byte)100);
		ji.fillImageWithColor(c);
		byte[] ba = PNGEncoder.encode(ji);
		System.out.println(TestUtils.hashByteArray(ba));
		assertEquals("fe7bb20c22789ec0715ffb7cdbff8c786fa93a2a608ade47a553f4fc141c5a12", TestUtils.hashByteArray(ba));
	}
}
