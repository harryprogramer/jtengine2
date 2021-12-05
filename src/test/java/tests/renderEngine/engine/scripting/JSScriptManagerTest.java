package tests.renderEngine.engine.scripting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import thewall.engine.twilight.scripting.JSScriptManager;
import thewall.engine.twilight.scripting.ScriptManager;
import thewall.engine.twilight.scripting.ScriptPlugin;

import java.io.File;
import java.io.FileNotFoundException;

class JSScriptManagerTest {

    ScriptPlugin scriptPlugin;

    @BeforeEach
    public void createRuntime() throws FileNotFoundException {
        ScriptManager scriptManager = new JSScriptManager();
        scriptManager.createRuntime();
        scriptPlugin = scriptManager.executeScript(new File("./test.js"));
    }

    @Test
    public void executeScript() {
        System.out.println(scriptPlugin.getName() );
        scriptPlugin.start();
    }
}