package thewall.engine.twilight.hardware.hna;

import org.junit.jupiter.api.Test;
import oshi.SystemInfo;

class RealtimeHNAccessTest {
    @Test
    void test(){
        RealtimeHNAccess hnAccess = new RealtimeHNAccess();
        SystemInfo systemInfo = new SystemInfo();
        System.out.println(hnAccess.getBaseboardModel());
    }

}