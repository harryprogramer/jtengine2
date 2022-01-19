package jte2.engine.twilight.texture.opengl;

import org.junit.jupiter.api.Test;
import xyz.codingdaddy.texture.manipulation.HeightMapToNormalMap;
import xyz.codingdaddy.texture.manipulation.ImageToHeightMap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class NormalMapping {
    @Test
    void test() throws IOException {
        BufferedImage image = ImageIO.read(new File("pexels.jpeg"));
        BufferedImage heightMap = ImageToHeightMap.convert(image);
        ImageIO.write(heightMap, "PNG", new File("pexels-height.png"));

        float scale = 5.0f;
        BufferedImage normalMap = HeightMapToNormalMap.convert(image, scale);
        ImageIO.write(normalMap, "PNG", new File("pexels-map.png"));
    }
}
