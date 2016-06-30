package me.lcw.jil;

import java.io.IOException;

import org.junit.Test;

import me.lcw.jil.BaseImage.MODE;
import me.lcw.jil.BaseImage.ScaleType;

public class ScaleSpeedTests {

  
  @Test
  public void NNSpeed1() throws IOException {
    int tries = 100;
    ScaleType st = ScaleType.NN;
    JilImage ji = JilImage.create(MODE.RGBA, 200, 200);
//    NativeImage ni = NativeImage.fromJilImage(ji);
//    AWTImage ai = AWTImage.fromBaseImage(ji);
//    
//    
//    File fj = new File("/tmp/jil-out.tmp");
//    fj.delete();
//    fj.createNewFile();
//    Profiler p = new Profiler(10);
//    p.start();
    long j_start = System.currentTimeMillis();
    for(int i=0; i<tries; i++) {
      ji.resize(2000, 2000, true, st);
    }
    long j_end = System.currentTimeMillis();
    System.out.println("Jil:"+ (j_end-j_start));
//    p.stop();
//    p.dump(new FileOutputStream(fj));
//    
//    
//    File f = new File("/tmp/out.tmp");
//    f.delete();
//    f.createNewFile();
//    p.reset();
//    p.start();
//    long n_start = System.currentTimeMillis();
//    for(int i=0; i<tries; i++) {
//      ni.resize(2000, 2000, true, st);
//    }
//    long n_end = System.currentTimeMillis();
//    p.stop();
//    System.out.println("Nat:"+(n_end-n_start));
//    p.dump(new FileOutputStream(f));
//    
//    
//    File fa = new File("/tmp/awt-out.tmp");
//    fa.delete();
//    fa.createNewFile();
//    p.reset();
//    p.start();
//    long a_start = System.currentTimeMillis();
//    for(int i=0; i<tries; i++) {
//      ai.resize(2000, 2000, true, st).getArray();
//    }
//    long a_end = System.currentTimeMillis();
//    p.stop();
//    System.out.println("AWT:"+(a_end-a_start));
//    p.dump(new FileOutputStream(fa));
  }
}
