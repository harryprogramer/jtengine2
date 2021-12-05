package tests.renderEngine.engine.debugger.console;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.*;

class DebugConsoleTest {
    @Test
    void test(){
        ConcurrentMap<Integer, String> logQueue = new ConcurrentHashMap<>();
        for(int i = 0; i < 15; i++){
            logQueue.put(1, "test" + i);
        }

        System.out.println(logQueue);
    }
}