package thewall.engine.twilight.events.endpoints;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.*;

public abstract class EndpointRouter implements EndpointHandler {
    private final static Logger logger = LogManager.getLogger(EndpointRouter.class);
    private final Map<String, List<Runnable>> routes = new HashMap<>();
    private Object routeObj = null;

    @Contract(pure = true)
    private void routeAll(@NotNull Object obj){
        Class<?> clazz = obj.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for(Method method : methods){
            if(method.isAnnotationPresent(Endpoint.class)){
                Endpoint endpoint = method.getAnnotation(Endpoint.class);
                String route = endpoint.route();
                if(route == null || route.length() == 0){
                    logger.warn("Endpoint [" + method + "] has null route.");
                    continue;
                }

                addRoute(route, method);
            }
        }
    }


    private void registerRoute(String route, Runnable runnable){
        List<Runnable> endpointsList = routes.get(route);
        if(endpointsList == null){
            List<Runnable> methodList = new ArrayList<>();
            methodList.add(runnable);
            routes.put(route, methodList);
        }else {
            endpointsList.add(runnable);
            routes.put(route, endpointsList);
        }
    }

    @Override
    public void addRoute(String route, Method endpoint){
        addRoute(route, () -> {
            try {
                endpoint.invoke(routeObj);
            }catch (Throwable t){
                logger.error("Endpoint route exception", t);
                throw new RuntimeException(t);
            }
        });
    }

    @Override
    public void findRoutes(Object obj) {
        if(obj == null){
            throw new NullPointerException("Object is null");
        }

        this.routeObj = obj;
        routeAll(obj);
    }

    @Override
    public void callEndpoint(String route) {
        if(this.routeObj == null){
            throw new IllegalStateException("Endpoints is not scanned yet.");
        }
        callEndpoint(route, false);
    }

    @Override
    public void callEndpoint(String route, boolean async) {
        if(this.routeObj == null){
            throw new IllegalStateException("Endpoints is not scanned yet.");
        }

        if(route == null || route.length() == 0){
            throw new NullPointerException("Route is null");
        }

        List<Runnable> endpoint = routes.get(route);

        if(endpoint == null){
            logger.warn("No endpoint available for route [" + route + "]");
            return;
        }

        try {
            invokeEndpoint(endpoint, async, route);
        }catch (Throwable t){
            logger.warn("Endpoint exception", t);
        }
    }

    @Override
    public void clearRoutes() {
        this.routeObj = null;
        routes.clear();
    }


    @Override
    public void addRoute(String route, Runnable r) {
        if(route == null || route.length() == 0){
            throw new NullPointerException("Route is null");
        }

        if(r == null){
            throw new NullPointerException("Endpoint is null");
        }

        registerRoute(route, r);
    }

    @Override
    public void stopRouter() {
        clearRoutes();
        onClose();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Runnable[] getRoutes(String endpoint) {
        Object obj = routes.get(endpoint);
        return obj != null ? (Runnable[]) routes.get(endpoint).toArray() : null;
    }

    protected abstract void onClose();

    protected abstract void invokeEndpoint(List<Runnable> endpoint, boolean async, String route);
}
