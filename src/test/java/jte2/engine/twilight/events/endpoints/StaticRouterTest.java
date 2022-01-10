package jte2.engine.twilight.events.endpoints;

import org.junit.jupiter.api.Test;

class StaticRouterTest {

    @Endpoint(route = "test_endpoint")
    void endpoint(){
        System.out.println("done!");
    }

    @Test
    void test(){
        StaticRouter staticRouter = new StaticRouter();
        staticRouter.findRoutes(this);
        staticRouter.callEndpoint("test_endpoint2");
    }
}