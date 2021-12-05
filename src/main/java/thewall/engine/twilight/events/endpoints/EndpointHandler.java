package thewall.engine.twilight.events.endpoints;

import java.lang.reflect.Method;

public interface EndpointHandler {
    void findRoutes(Object obj);

    void callEndpoint(String endpoint);

    void callEndpoint(String endpoint, boolean async);

    void clearRoutes();

    void addRoute(String route, Method method);

    void addRoute(String route, Runnable r);

    void stopRouter();

    Runnable[] getRoutes(String endpoint);
}
