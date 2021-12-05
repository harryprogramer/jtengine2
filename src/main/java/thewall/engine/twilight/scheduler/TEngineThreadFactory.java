package thewall.engine.twilight.scheduler;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class TEngineThreadFactory implements ThreadFactory {
    @Getter
    private static final TEngineThreadFactory instance = new TEngineThreadFactory();

    private TEngineThreadFactory(){
        if(instance != null){
            throw new IllegalStateException("This class can be have only one instane");
        }
    }

    private final AtomicInteger atomicInteger = new AtomicInteger(0);
    @Override
    public Thread newThread(@NotNull Runnable r) {
        return new Thread(r, "twilight-thread-" + atomicInteger.incrementAndGet());
    }
}
