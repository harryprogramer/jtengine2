package thewall.engine.twilight.runtime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import thewall.engine.twilight.errors.InitializationException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TwilightRuntimeService {
    private static final Logger logger = LogManager.getLogger(TwilightRuntimeService.class);
    private static final HashMap<Class<?>, Class<? extends AbstractRuntime<?>>> runtimes = new HashMap<>();
    private static final ConcurrentMap<Object, AbstractRuntime<?>> activeRuntime = new ConcurrentHashMap<>();

    private static boolean isInit = false;

    protected static void registerActiveRuntime(Object program, AbstractRuntime<?> runtime){
        activeRuntime.put(program, runtime);
    }

    protected static void deleteActiveRuntime(Object program){
        activeRuntime.remove(program);
    }

    public static Map<Object, AbstractRuntime<?>> getActiveRuntimes(){
        return activeRuntime;
    }

    public static void registerRuntime(Class<?> program, Class<? extends AbstractRuntime<?>> tRuntime){
        if(runtimes.containsKey(program)){
            throw new IllegalStateException("Runtime [" + tRuntime.getName() + "] is already registered");
        }
        runtimes.put(program, tRuntime);
    }

    @SuppressWarnings("unchecked")
    public static @Nullable <K> AbstractRuntime<K> findRuntime(Class<K> program){
        Class<? extends AbstractRuntime<?>> runtime;

        runtime = runtimes.get(program);
        if(runtime == null){
            return null;
        }

        try {
            return (AbstractRuntime<K>) runtime.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.warn("Cannot create runtime", e);
        }

       return null;
    }

    public static void init(){
        if(isInit){
            throw new InitializationException("Runtime service already initialized");
        }
        Runtime.getRuntime().addShutdownHook(new RuntimeServiceShutdown());
        isInit = true;
    }

    private static class RuntimeServiceShutdown extends Thread{
        public RuntimeServiceShutdown(){
            setName("Runtime Shutdown Service");
        }

        @Override
        public void run() {
            if(!activeRuntime.isEmpty()) {
                logger.info("Closing all runtimes");
                for (Iterator<AbstractRuntime<?>> taskIterator = activeRuntime.values().iterator(); taskIterator.hasNext(); ) {
                    AbstractRuntime<?> runtime = taskIterator.next();
                    runtime.forceStop();
                    taskIterator.remove();
                }
            }
        }
    }
}
