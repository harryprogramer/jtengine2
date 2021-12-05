package thewall.engine.twilight.scripting.api;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Object;

public class JScriptAPIRegisterer {
    public static void registerApis(V8 engine){
        V8Object consoleObject = new V8Object(engine);
        engine.add("console", consoleObject);
        consoleObject.registerJavaMethod(ApiFactory.CONSOLE, "log", "log", new Class<?>[]{String.class});
    }
}
