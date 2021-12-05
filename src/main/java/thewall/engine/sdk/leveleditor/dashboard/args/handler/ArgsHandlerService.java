package thewall.engine.sdk.leveleditor.dashboard.args.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thewall.engine.sdk.leveleditor.dashboard.DashboardSession;
import thewall.engine.sdk.leveleditor.dashboard.args.Argument;
import thewall.engine.sdk.leveleditor.dashboard.args.parser.LineArgument;
import thewall.engine.sdk.leveleditor.dashboard.args.parser.LineParser;
import thewall.engine.twilight.utils.Colour;
import thewall.engine.twilight.utils.Validation;

import java.util.HashMap;
import java.util.Map;

public final class ArgsHandlerService {
    private static final Logger logger = LogManager.getLogger(ArgsHandlerService.class);
    private final Map<String, Argument> args = new HashMap<>();
    private final LineParser lineParser;

    public ArgsHandlerService(LineParser lineParser){
        this.lineParser = lineParser;
    }

    public void parseText(String text, DashboardSession session){
        LineArgument lineArgument = lineParser.parseLine(text);
        Argument argument = args.get(lineArgument.getName());
        if(argument == null){
            logger.warn("There's no endpoint for argument [" + lineArgument.getName() + "]");
            session.writeLine("No handle found for [" + lineArgument.getName() + "]", Colour.RED);
            return;
        }
        argument.handle(lineArgument, session);
    }

    public void addHandle(String argName, Argument arg){
        Validation.checkNull(argName, argName);
        args.put(argName, arg);
    }

    public Argument getHandle(String argName){
        return args.get(argName);
    }

    public void removeHandle(String argName){
        args.remove(argName);
    }
}
