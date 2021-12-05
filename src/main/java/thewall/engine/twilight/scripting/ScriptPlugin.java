package thewall.engine.twilight.scripting;

import com.eclipsesource.v8.V8;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class ScriptPlugin {
    @Getter
    private final String name;
    @Getter
    private final String version;

    private final V8 jsengine;

    protected ScriptPlugin(@NotNull V8 engine, @NotNull String name, @NotNull String version){
        this.name = name;
        this.version = version;
        this.jsengine = engine;
    }

    public void start(){
        jsengine.executeJSFunction("init");
    }

    public void stop(){

    }

    public void isEnable(){

    }

}
