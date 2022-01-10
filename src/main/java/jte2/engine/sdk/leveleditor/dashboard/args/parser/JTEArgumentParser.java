package jte2.engine.sdk.leveleditor.dashboard.args.parser;

import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import jte2.engine.twilight.errors.SyntaxException;

import java.util.ArrayList;
import java.util.List;

public class JTEArgumentParser implements LineParser {
    private final static Logger logger = LogManager.getLogger(JTEArgumentParser.class);

    private @NotNull LineArgument parse(@NotNull String text){
        text = text.trim().replaceAll(" +", " ");

        int endIndex = text.indexOf(' ');
        if(endIndex == -1){
            endIndex = text.length();
        }
        String argName = text.substring(0, endIndex);

        List<Integer> spaces = findAllSpaces(text);
        String[] args = new String[spaces.size()];

        for(int i = 0; i < args.length; i++){
            if(i + 1 == args.length) {
                args[i] = text.substring(spaces.get(i));
            }else {
                args[i] = text.substring(spaces.get(i), spaces.get(i + 1) - 1);
            }
        }

        return new ArgumentLineImpl(argName, args, args.length);
    }

    private static @NotNull List<Integer> findAllSpaces(@NotNull String text){
        List<Integer> indexes = new ArrayList<>();
        int nearestIndex = 0;
        do {
            int index = text.indexOf(' ', nearestIndex + 1);
            if(index == -1){
                break;
            }

            nearestIndex = index;
            indexes.add(index + 1);

        }while (true);

        return indexes;
    }

    @Override
    @Contract(pure = true)
    public LineArgument parseLine(String text) {
        if(text == null){
            throw new NullPointerException("Text is null.");
        }
        try {
            return parse(text);
        }catch (Throwable e){
            logger.warn("Dashboard parse failed, syntax error from [" + text.replaceAll("\n", "") + "], [" + e.getMessage() + "/" + e.getClass().getName() + "]");
            logger.debug("", e);
            throw new SyntaxException(e);
        }

    }

    @Data
    private static class ArgumentLineImpl implements LineArgument {
        private final String name;
        private final String[] args;
        private final int argsSize;

        @Override
        public String[] getArguments() {
            return args;
        }

        @Override
        public int getArgumentsSize() {
            return argsSize;
        }
    }
}
