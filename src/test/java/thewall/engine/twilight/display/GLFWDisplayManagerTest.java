package thewall.engine.twilight.display;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

class GLFWDisplayManagerTest {
    @SneakyThrows
    @Test
    void test(){
        long t1 = System.currentTimeMillis();

        Thread.sleep(500);

        float test = (System.currentTimeMillis() - t1) / 1000.0f;

        System.out.println(test);
    }
}