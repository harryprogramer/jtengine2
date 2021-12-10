package thewall.engine.twilight;

import thewall.engine.twilight.audio.SoundMaster;
import thewall.engine.twilight.display.Display;
import thewall.engine.twilight.events.EventManager;
import thewall.engine.twilight.hardware.Hardware;
import thewall.engine.twilight.input.Input;
import thewall.engine.twilight.system.AppSettings;

public interface Application {
    void onInit();

    void update();

    void onClose();

    void setInput(Input input);

    void setDisplay(Display display);

    void setEventManager(EventManager eventManager);

    int getFrameLimit();

    Input getInput();

    void setSound(SoundMaster sound);

    AppSettings getSettings();

    ViewPort getGUIViewPort();

    ViewPort getViewPort();

    Hardware getHardware();

}
