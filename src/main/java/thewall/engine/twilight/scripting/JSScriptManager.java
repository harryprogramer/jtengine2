package thewall.engine.twilight.scripting;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Object;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import thewall.engine.twilight.scripting.api.JScriptAPIRegisterer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class JSScriptManager implements ScriptManager{
    private V8 jsEngine;

    @Override
    public void createRuntime() {
        jsEngine = V8.createV8Runtime();
        JScriptAPIRegisterer.registerApis(jsEngine);
    }

    @Override
    public ScriptPlugin executeScript(@NotNull File file) throws FileNotFoundException {
        try {
            return executeScript(readScript(file.toPath().toString()));
        }catch (IOException e){
            throw new FileNotFoundException(e.getMessage());
        }
    }

    @Override
    public ScriptPlugin executeScript(String script) {
        return executeScript(script, jsEngine);
    }

    @Contract(value = "_, _ -> new", pure = true)
    private @NotNull ScriptPlugin executeScript(String script, @NotNull V8 engine){
        engine.executeScript(script);
        return new ScriptPlugin(engine, ((V8Object)engine.get("pluginInfo")).getString("name"),
                ((V8Object)engine.get("pluginInfo")).getString("version"));
    }

    private String readScript(String file) throws IOException {
        return Files.readString(Path.of(file), StandardCharsets.UTF_8);
    }

}
