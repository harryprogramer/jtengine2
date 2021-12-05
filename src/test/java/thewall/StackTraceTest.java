package thewall;

import org.junit.Test;

public class StackTraceTest {
    void caller1(){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for(StackTraceElement stackTraceElement : stackTraceElements){
            System.out.println(stackTraceElement.getMethodName());
        }
    }

    void caller2(){
        caller1();
    }

    void caller3(){
        caller2();
    }

    @Test
    public void test(){
        caller3();
    }
}
