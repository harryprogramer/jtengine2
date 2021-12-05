package thewall.engine.twilight.system;

import org.jetbrains.annotations.NotNull;
import thewall.engine.twilight.audio.SoundMaster;
import thewall.engine.twilight.audio.jmf.SoundManager;
import thewall.engine.twilight.audio.openal.OpenALSoundManager;
import thewall.engine.twilight.input.InputProvider;
import thewall.engine.twilight.input.glfw.GLFWInput;

import static thewall.engine.twilight.utils.Validation.*;

import java.util.HashMap;
import java.util.Map;

public final class AppSettings {
    HashMap<SettingsType, Object> settings;

    public AppSettings(){
        this.settings = loadDefault();
    }

    public enum SettingsType {
        TITLE("window_title"),
        WINDOW_X("window_x"),
        WINDOW_Y("window_y"),
        SOUND_MANAGER("sound"),
        INPUT_MANAGER("input"),
        CUSTOM(null),
        ;

        private String name;

        SettingsType(String name){
            this.name = name;
        }

        public @NotNull String getName() {
            return name;
        }

        public void setName(@NotNull String name){
            if(!name().equalsIgnoreCase("CUSTOM")){
                throw new IllegalStateException("This enum is final name, it cannot be changed");
            }
            this.name = name;
        }

    }

    public Map<SettingsType, Object> getSettingsMap(){
        return settings;
    }

    private static HashMap<SettingsType, Object> loadDefault(){
        HashMap<SettingsType, Object> settings = new HashMap<>();
        settings.put(SettingsType.TITLE, "App");
        settings.put(SettingsType.WINDOW_X, 800);
        settings.put(SettingsType.WINDOW_Y, 600);
        settings.put(SettingsType.SOUND_MANAGER, SoundManager.class);
        settings.put(SettingsType.INPUT_MANAGER, GLFWInput.class);

        return settings;
    }

    public void setTitle(String title){
        checkNull(title);
        settings.replace(SettingsType.TITLE, title);
    }

    public void setWidth(int x){
        checkNull(settings);
        settings.replace(SettingsType.WINDOW_X, x);
    }

    public void setHeight(int y){
        checkNull(y);
        settings.replace(SettingsType.WINDOW_Y, y);
    }

    public void setNativeSound(Class<? extends SoundMaster> sound){
        checkNull(sound);
        settings.put(SettingsType.SOUND_MANAGER, sound);
    }
}
