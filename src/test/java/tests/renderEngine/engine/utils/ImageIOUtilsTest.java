package tests.renderEngine.engine.utils;

import org.junit.jupiter.api.Test;
import jte2.engine.twilight.utils.ImageIOUtils;

import java.io.IOException;

class ImageIOUtilsTest {
    @Test
    void test() throws IOException {
        ImageIOUtils.checkImageFormat("./res/texture/grass.png");
    }

}