package jte2.engine.sdk.leveleditor.dashboard.args.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import jte2.engine.sdk.leveleditor.dashboard.ConsoleSession;
import jte2.engine.sdk.leveleditor.dashboard.args.Argument;
import jte2.engine.sdk.leveleditor.dashboard.args.parser.LineArgument;
import jte2.engine.sdk.leveleditor.dashboard.args.parser.LineParser;
import jte2.engine.twilight.material.Colour;
import jte2.engine.twilight.utils.Validation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ArgsHandlerService {
    private final ExecutorService executor = Executors.newFixedThreadPool(50);
    private static final Logger logger = LogManager.getLogger(ArgsHandlerService.class);
    private final Map<String, Argument> args = new HashMap<>();
    private final LineParser lineParser;

    public ArgsHandlerService(LineParser lineParser){
        this.lineParser = lineParser;
    }

    public void parseText(String text, ConsoleSession session) {
        LineArgument lineArgument = lineParser.parseLine(text);
        Argument argument = args.get(lineArgument.getName());
        if (argument == null) {
            logger.warn("There's no endpoint for argument [" + lineArgument.getName() + "]");
            session.writeLine("No handle found for [" + lineArgument.getName() + "]", Colour.RED);
            return;
        }

        if (argument.isAsync()) {
            executor.submit(() -> argument.handle(lineArgument, session));
        } else {
            argument.handle(lineArgument, session);
        }
    }

    public void addHandle(String argName, Argument arg){
        Validation.checkNull(argName, argName);
        args.put(argName, arg);
    }

    public Argument getHandle(String argName){
        return args.get(argName);
    }

    @Contract(pure = true)
    public @NotNull Map<String, Argument> getHandles(){
        return args;
    }

    public void removeHandle(String argName){
        args.remove(argName);
    }

    public void stop(){
        executor.shutdown();
    }
}
