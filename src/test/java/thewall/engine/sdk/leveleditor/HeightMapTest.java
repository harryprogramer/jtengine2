package thewall.engine.sdk.leveleditor;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class HeightMapTest {
    @Test
    void test(){
        BufferedImage bufferedImage = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_RGB );
        for(int x = 0; x < 1280; x++){
            for(int y = 0; y < 720; y++){
                int randomValue = (int)(Math.random() * 256);
                Color randomColor = new Color( randomValue, randomValue, randomValue);
                bufferedImage.setRGB(x, y, randomColor.getRGB());
            }
        }

        File outputFile = new File("heightmap.png");
        try {
            ImageIO.write(bufferedImage, "png", outputFile);
        }catch (IOException ioex){
            ioex.printStackTrace();
        }
    }
}