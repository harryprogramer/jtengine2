package jte2.engine.sdk.leveleditor.dashboard.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import jte2.engine.sdk.leveleditor.Editor;
import jte2.engine.sdk.leveleditor.dashboard.ConsoleSession;
import jte2.engine.sdk.leveleditor.dashboard.args.Argument;
import jte2.engine.sdk.leveleditor.dashboard.args.parser.LineArgument;
import jte2.engine.twilight.material.Colour;
import jte2.engine.twilight.shaders.ShaderHandle;

public class ShaderArgument extends Argument {
    private final static Logger logger = LogManager.getLogger(ShaderArgument.class);
    private final Editor editor;

    public ShaderArgument(Editor editor){
        this.editor = editor;
    }

    @Override
    public void handle(LineArgument arg, ConsoleSession session) {
        if(arg.getArgumentsSize() != 1){
            echoText("Syntax error, Excepted 1 argument (shader_name)", session);
            return;
        }

        String shaderName = arg.getArguments()[0];
        ShaderHandle shader;
        try {
            Class<?> clazz = this.getClass().getClassLoader().loadClass(shaderName);
            shader = (ShaderHandle) clazz.getDeclaredConstructor().newInstance();
            editor.setShader(shader);
        }catch (Exception e) {
            echoText("Cannot load shader, " + e + " " + e.getMessage(), session);
            logger.error("Cannot load shader [{}]", shaderName, e);
            return;
        }

        logger.info("Shader [" + shader.getClass().getName() + "] loaded.");
        session.writeLine("Shader [" + shader.getClass().getName() + "] loaded.");
    }

    private void echoText(String text, @NotNull ConsoleSession session){
        logger.error(text);
        session.writeLine(text, Colour.RED);
    }
}
