package thewall.engine.twilight.scripting;

import java.io.File;
import java.io.FileNotFoundException;

public interface ScriptManager {
    void createRuntime();

    ScriptPlugin executeScript(File file) throws FileNotFoundException;

    ScriptPlugin executeScript(String script);

}
