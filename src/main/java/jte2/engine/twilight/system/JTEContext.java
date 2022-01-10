package jte2.engine.twilight.system;

import jte2.engine.twilight.assets.AssetManager;
import jte2.engine.twilight.audio.SoundMaster;
import jte2.engine.twilight.display.Display;
import jte2.engine.twilight.events.endpoints.EndpointHandler;
import jte2.engine.twilight.events.EventManager;
import jte2.engine.twilight.input.Input;
import jte2.engine.twilight.renderer.Renderer;

public interface JTEContext {
    AssetManager getAssetsManager();

    EndpointHandler getEndpointHandler();

    SoundMaster getSoundMaster();

    Display getDisplay();

    EventManager getEventManager();

    Renderer getRenderer();

    Input getInput();

    boolean shouldClose();

    void update();

    void init();

    void destroy();
}
