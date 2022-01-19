package jte2.engine.twilight.system;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JTESystemTest {
    @Test
    void test(){
        JTESystem.showErrorDialog("Test Title", "Test Message");
    }

}