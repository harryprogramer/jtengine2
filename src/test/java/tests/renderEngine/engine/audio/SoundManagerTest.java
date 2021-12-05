package tests.renderEngine.engine.audio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import thewall.engine.twilight.audio.SoundChannel;
import thewall.engine.twilight.audio.jmf.SoundManager;

class SoundManagerTest {
    private SoundManager soundManager;

    @Test
    void TestMethod() throws InterruptedException {
        SoundChannel soundChannel =  soundManager.playBackground(0.2f, 0.2f, "./res/music/theme1.wav");
        Thread.sleep(1000);
        while(true){
            if(!soundChannel.isOpen()){
                break;
            }
            Thread.onSpinWait();
        }
    }

    @BeforeEach
    void setUp(){
        soundManager = new SoundManager();
    }
}