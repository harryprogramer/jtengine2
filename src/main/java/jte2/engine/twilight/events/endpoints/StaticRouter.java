package jte2.engine.twilight.events.endpoints;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StaticRouter extends EndpointRouter {
    private final static Logger logger = LogManager.getLogger(StaticRouter.class);
    private final ExecutorService executor = Executors.newFixedThreadPool(50);

    @Override
    protected void invokeEndpoint(List<Runnable> endpoint, boolean async, String route) {
        if(async){
            for(Runnable runnable : endpoint){
                executor.submit(runnable);
            }
        }else {
            for(Runnable runnable : endpoint){
                try{
                    runnable.run();
                }catch (Throwable t){
                    logger.warn("Exception in endpoint [" + runnable + "] for route [" + route + "]", t);
                }
            }
        }
    }

    @Override
    protected void onClose() {
        logger.info("Closing static endpoint router...");
        executor.shutdown();
        executor.shutdownNow();
    }
}
