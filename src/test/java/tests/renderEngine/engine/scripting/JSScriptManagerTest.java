package tests.renderEngine.engine.scripting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jte2.engine.twilight.scripting.JSScriptManager;
import jte2.engine.twilight.scripting.ScriptManager;
import jte2.engine.twilight.scripting.ScriptPlugin;

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