package me.lcw.jil;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleTests {

  @Test
  public void test() throws InterruptedException {
    final ReentrantLock rl = new ReentrantLock();
    final Condition c = rl.newCondition();
    Thread t = new Thread(new Runnable() {
      @Override
      public void run() {
        System.out.println("new Thread");
        System.out.println(rl.getHoldCount());
        rl.lock();
        rl.lock();
        System.out.println("Got Lock");
        System.out.println(rl.getHoldCount());
        try {
          System.out.println("ba:"+rl.getHoldCount());
          c.await();
          System.out.println("aa:"+rl.getHoldCount());
          
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } finally {
          rl.unlock();
          System.out.println("aa:"+rl.getHoldCount());
          rl.unlock();
          System.out.println("aa:"+rl.getHoldCount());
        }
      }});
    //t.setDaemon(true);
    t.start();
    Thread.sleep(100);
    System.out.println("main lock:"+rl.getHoldCount());
    rl.lock();
    System.out.println("main signal:"+rl.getHoldCount());
    c.signal();
    System.out.println("main signal:unlock");
    rl.unlock();

    System.out.println("main signal:"+rl.getHoldCount());
    //c.notify();
    
    Thread.sleep(100);
  }
  
  
  public static void main(String[] args) throws InterruptedException {
    long start = System.currentTimeMillis();
    basicRLockingTest();
    System.out.println("basicRLockingTest: "+ (System.currentTimeMillis()-start) +"ms");
  }
  
  public static void basicRLockingTest() throws InterruptedException {
    final ReentrantLock lock = new ReentrantLock();
    for(int i=0; i<200; i++) {
      assertEquals(i, lock.getHoldCount());
      lock.lock();
    }
    for(int i=200; i>0; i--) {
      assertEquals(i, lock.getHoldCount());
      lock.unlock();
    }
    
    lock.lock();
    List<Thread> tl = new ArrayList<Thread>(200);
    for(int i=0; i<200; i++) {
      Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
          lock.lock();
          lock.unlock();
        }});
      tl.add(t);
      t.setDaemon(true);
      t.start();
    }
    long start = System.currentTimeMillis();
    while(System.currentTimeMillis() - start < 500) {
      if(lock.getQueueLength() == 200) {
        break;
      }
    }
    assertEquals(200, lock.getQueueLength());
    assertEquals(1, lock.getHoldCount());
    lock.unlock();
    for(Thread t: tl) {
      t.join();
    }
    assertEquals(0, lock.getQueueLength());
    assertEquals(0, lock.getHoldCount());    
  }

}
