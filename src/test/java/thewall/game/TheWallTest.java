package thewall.game;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TheWallTest {
    @SneakyThrows
    @Test
    void main(){
        long time = System.currentTimeMillis();
        Thread.sleep(15);
        long finish = System.currentTimeMillis();
        System.out.println((finish - time) / 1000.0);
    }

}