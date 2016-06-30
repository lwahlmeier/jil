package me.lcw.jil.awt;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import org.junit.Test;

import me.lcw.jil.BaseImage;
import me.lcw.jil.Color;
import me.lcw.jil.ImageException;

public class AWTFillTest {

    //@Test
    public void percolation() throws ImageException, IOException {
        AWTImage ai = AWTImage.create(BaseImage.MODE.RGB, 512, 512, Color.BLACK);
        int maxIt = (int)(512 * 512 /1.15);
        Random rnd = new Random();
        //FileOutputStream fos = new FileOutputStream("/tmp/outpipe");
        for(int i=0; i<maxIt; i++) {
            int x = rnd.nextInt(512);
            int y = rnd.nextInt(512);
            Color c1 = Color.fromARGB(rnd.nextInt());
            Color c2 = Color.fromARGB(rnd.nextInt());
            ai.setPixel(x, y, c1);
            ai.draw().floodFill(x, y, c2, Color.BLACK, true);
            //fos.write(ai.getArray());
        }
        ai.save("/tmp/fill/test.png");
    }
    
    //@Test
    public void timeTest() throws ImageException, IOException {

        AWTImage ai = AWTImage.create(BaseImage.MODE.RGB, 1920, 1080, Color.BLACK);
        //ai.draw().circle(500, 500, 60, Color.WHITE, 3, false);
        //ai.save("/tmp/fill/test2.png");
        long start = System.currentTimeMillis();
        ai.draw().floodFill(ai.getWidth()/2, ai.getHeight()/2, Color.WHITE, null, true);
        long end = System.currentTimeMillis();
        System.out.println("Total:"+(end-start));
        ai.save("/tmp/fill/test.png");
    }
}
