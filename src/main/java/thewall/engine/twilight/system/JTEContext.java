package thewall.engine.twilight.system;

import thewall.engine.twilight.audio.SoundMaster;
import thewall.engine.twilight.display.Window;
import thewall.engine.twilight.events.endpoints.EndpointHandler;
import thewall.engine.twilight.events.EventManager;
import thewall.engine.twilight.input.Input;
import thewall.engine.twilight.renderer.Renderer;

public interface JTEContext {
    EndpointHandler getEndpointHandler();

    SoundMaster getSoundMaster();

    Window getDisplay();

    EventManager getEventManager();

    Renderer getRenderer();

    Input getInput();

    boolean shouldClose();

    void update();

    void init();

    void destroy();
}
