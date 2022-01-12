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
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

        if(arg.getArguments()[0].equalsIgnoreCase("-ls")){
            long start = System.currentTimeMillis();
            logger.info("Searching for available shaders in package [{}]", getClass().getPackageName());
            Reflections reflections = new Reflections("jte2",  new SubTypesScanner(false));
            Set<Class<?>> allClasses = reflections.getSubTypesOf(Object.class);
            List<Class<?>> availableShaders = new ArrayList<>();
            for(Class<?> clazz : allClasses){
                if(ShaderHandle.class.isAssignableFrom(clazz) &&
                        !Modifier.isAbstract(clazz.getModifiers()) &&
                        !Modifier.isInterface(clazz.getModifiers())){
                    availableShaders.add(clazz);
                }
            }
            long end = System.currentTimeMillis();
            logger.info("Found it {} shaders in package [jte2] in {}.s", availableShaders.size(), (end - start) / 1000.0);
            session.writeLine("Found it " + availableShaders.size() + " shaders", Colour.ORANGE);
            for(Class<?> shader : availableShaders){
                session.writeLine(String.format("%-10s - %s", shader.getSimpleName(), shader.getName()), Colour.GREEN);
            }
        }else {

            String shaderName = arg.getArguments()[0];
            ShaderHandle shader;
            try {
                Class<?> clazz = this.getClass().getClassLoader().loadClass(shaderName);
                shader = (ShaderHandle) clazz.getDeclaredConstructor().newInstance();
                editor.setShader(shader);
            } catch (Exception e) {
                echoText("Cannot load shader, " + e + " " + e.getMessage(), session);
                logger.error("Cannot load shader [{}]", shaderName, e);
                return;
            }

            logger.info("Shader [" + shader.getClass().getName() + "] loaded.");
            session.writeLine("Shader [" + shader.getClass().getName() + "] loaded.");
        }
    }

    @Override
    public String getHelpDescription() {
        return "-ls - search for available shaders | <shader_name> to set shader";
    }

    private void echoText(String text, @NotNull ConsoleSession session){
        logger.error(text);
        session.writeLine(text, Colour.RED);
    }
}
