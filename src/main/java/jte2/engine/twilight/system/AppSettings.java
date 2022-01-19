package jte2.engine.twilight.system;

import jte2.engine.twilight.Area;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import jte2.engine.twilight.audio.SoundMaster;
import jte2.engine.twilight.audio.jmf.SoundManager;
import jte2.engine.twilight.input.glfw.GLFWInput;
import org.lwjgl.assimp.Assimp;

import static jte2.engine.twilight.utils.Validation.*;

import java.util.HashMap;
import java.util.Map;

public final class AppSettings {
    private final static Logger logger = LogManager.getLogger(AppSettings.class);
    HashMap<String, Object> settings;

    public AppSettings(){
        this.settings = loadDefault();
    }

    public enum SettingsType {
        SOUND_RENDERER("sound_renderer_context"),
        INPUT_CONTEXT("input_context"),
        DISPLAY_RESOLUTION("display_res"),
        AUTO_FOCUS("auto_focus"),
        ASYNC_INIT("")
        ;

        private final String name;

        SettingsType(String name){
            this.name = name;
        }

        public @NotNull String getName() {
            return name;
        }
    }

    @Deprecated
    public Map<String, Object> getSettingsMap(){
        return settings;
    }

    private static @NotNull HashMap<String, Object> loadDefault(){
        HashMap<String, Object> settings = new HashMap<>();
        settings.put(SettingsType.AUTO_FOCUS.getName(), true);
        settings.put(SettingsType.DISPLAY_RESOLUTION.getName(), new Area(600, 600));

        return settings;
    }

    public void setParam(@NotNull SettingsType type, Object param){
        settings.put(type.getName(), param);
    }

    public void setParam(@NotNull String type, Object param){
        settings.put(type, param);
    }

    public Object getParam(String param){
       return settings.get(param);
    }

    public boolean getParamBoolean(String param){
        return (boolean) settings.get(param);
    }

    public String getParamString(String param){
        return (String) settings.get(param);
    }

    public int getParamInt(String param){
        return (int) settings.get(param);
    }

    public long getParamLong(String param){
        return (long) settings.get(param);
    }

    public Object getParam(@NotNull SettingsType param){
        return settings.get(param.getName());
    }

    public boolean getParamBoolean(@NotNull SettingsType param){
        return (boolean) settings.get(param.getName());
    }

    public String getParamString(@NotNull SettingsType param){
        return (String) settings.get(param.getName());
    }

    public int getParamInt(@NotNull SettingsType param){
        return (int) settings.get(param.getName());
    }

    public long getParamLong(@NotNull SettingsType param){
        return (long) settings.get(param.getName());
    }

    public <T> T getParam(String param, Class<T> type){
        Object option = settings.get(param);
        if(option == null){
            logger.warn("Option from param [" + param + "] was not found");
            return null;
        }

        try {
            return type.cast(option);
        }catch (ClassCastException e){
            logger.warn("Invalid settings param type was provided, " +
                    "tried from [" + type.getSimpleName() + "] to " +
                    "[" + option.getClass().getSimpleName() + "]");
            return null;
        }
    }

    public <T> T getParam(SettingsType param, Class<T> type){
        return getParam(param.getName(), type);
    }
}
