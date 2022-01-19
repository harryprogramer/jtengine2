package jte2.engine.twilight.video;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class JCodecTest {
    @Test
    void test() throws JCodecException, IOException {
        double startSec = 51.632;
        int frameCount = 10;
        File file = new File("res/video/video2.mp4");

        FrameGrab grab = FrameGrab.createFrameGrab(NIOUtils.readableChannel(file));
        grab.seekToSecondPrecise(startSec);

        for (int i=0;i<frameCount;i++) {
            Picture picture = grab.getNativeFrame();
            System.out.println(picture.getWidth() + "x" + picture.getHeight() + " " + picture.getColor());
            //for JDK (jcodec-javase)
            BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
            ImageIO.write(bufferedImage, "png", new File("frame"+i+".png"));
        }
    }
}
