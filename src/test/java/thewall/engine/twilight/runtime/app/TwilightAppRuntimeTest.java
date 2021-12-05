package thewall.engine.twilight.runtime.app;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

class TwilightAppRuntimeTest {
    volatile boolean isDone = false;

    @Test
    @SneakyThrows
    void test2(){
        System.out.println("While loop speed:");
        AtomicLong lastTime = new AtomicLong(System.currentTimeMillis());
        AtomicInteger counter = new AtomicInteger(0);
        AtomicInteger tickLeft = new AtomicInteger();
        while (true) {
            counter.getAndIncrement();
            if (System.currentTimeMillis() - lastTime.get() > 1000) {
                System.out.println("Operation per second: " + counter.get());
                counter.set(0);
                lastTime.set(System.currentTimeMillis());
                if(tickLeft.incrementAndGet() == 5){
                    break;
                }
            }
        }

        System.out.println("Executor speed:");

        tickLeft.set(0);
        lastTime = new AtomicLong(System.currentTimeMillis());
        counter = new AtomicInteger(0);
        AtomicInteger finalCounter = counter;
        AtomicLong finalLastTime = lastTime;
        scheduler.scheduleAtFixedRate(() -> {
            finalCounter.getAndIncrement();
            if(System.currentTimeMillis() - finalLastTime.get() > 1000){
                System.out.println("Operation per second: " + finalCounter.get());
                finalCounter.set(0);
                finalLastTime.set(System.currentTimeMillis());
                if(tickLeft.incrementAndGet() == 5){
                    isDone = true;
                    scheduler.shutdown();
                }
            }
        }, 100, 0, TimeUnit.NANOSECONDS);

        while (true){
            if(isDone){
                break;
            }
            Thread.sleep(1000);
        }

    }

    private final static ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    @SneakyThrows
    void test(){
    }
}