package jte2.engine.twilight.pipeline;

import jte2.engine.twilight.scheduler.TEngineThreadFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.*;

public class AppStateManager {
    private static final int MAX_THREADS = Runtime.getRuntime().availableProcessors();

    private final static Logger logger = LogManager.getLogger(AppStateManager.class);
    private final Map<Integer, AppState> states = new ConcurrentHashMap<>();
    private final ExecutorService asyncStateExecutor = new ThreadPoolExecutor(0, MAX_THREADS, 60L, TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(), TEngineThreadFactory.INSTANCE);

    public void update(RenderManager renderer){

    }

    public void attachState(AppState state){

    }

    public void detachState(String uuid){

    }

    public void detachState(AppState uuid){

    }
}
