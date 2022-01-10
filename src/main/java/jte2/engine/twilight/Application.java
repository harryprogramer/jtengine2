package jte2.engine.twilight;

import jte2.engine.twilight.assets.AssetManager;
import jte2.engine.twilight.audio.SoundMaster;
import jte2.engine.twilight.display.Display;
import jte2.engine.twilight.events.EventManager;
import jte2.engine.twilight.hardware.Hardware;
import jte2.engine.twilight.input.Input;
import jte2.engine.twilight.shaders.ShaderHandle;
import jte2.engine.twilight.system.AppSettings;
import jte2.engine.twilight.viewport.ViewPort;
import jte2.engine.twilight.viewport.ViewPort2D;

public interface Application {
    void onInit();

    void update();

    void onClose();

    void setInput(Input input);

    void setDisplay(Display display);

    void setEventManager(EventManager eventManager);

    void setAssetsManager(AssetManager manager);

    int getFrameLimit();

    void setSound(SoundMaster sound);

    AppSettings getSettings();

    ViewPort2D getGUIViewPort();

    ViewPort getViewPort();

    Hardware getHardware();

    ShaderHandle getShader();
}
