package thewall.engine.twilight.debugger.console;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public final class ConsoleOutProxy extends PrintStream {
    @Setter
    private static DebugConsole console;
    @Getter
    private static final ConsoleOutProxy instance = new ConsoleOutProxy(System.out, System.err, false);

    @Getter
    private static final ConsoleOutProxy instanceErr = new ConsoleOutProxy(System.out, System.err, true);

    private final static Logger logger = LogManager.getLogger("System.out");


    private ConsoleOutProxy(PrintStream systemOut, PrintStream systemOutError, boolean err){
        super(err ? new OutStreamError() : new OutStream());

        if(!err) {
            OutStream.getInstance().systemOut = systemOut;
        }else {
            OutStreamError.getInstance().systemOut = systemOutError;
        }
    }

    private static class OutStream extends OutputStream {
        private PrintStream systemOut;

        private static final Logger logger = LogManager.getLogger(OutStream.class);

        @Getter
        private static OutStream instance;

        private final List<Character> characters = new ArrayList<>();

        public OutStream() {
            if (getInstance() != null) {
                throw new IllegalStateException("Console proxy can has only one instance");
            }
            instance = this;
        }

        private void flushConsole() {
            StringBuilder stringBuilder = new StringBuilder();
            for (char character : characters) {
                if (character == 0x0A || character == 0x0D) {
                    continue;
                }
                stringBuilder.append((character));
            }
            console.info(stringBuilder.toString().trim());
            logger.info(stringBuilder.toString().trim());
            characters.clear();
        }

        @Override
        public void write(int b) throws IOException {
            if (systemOut != null && console != null) {
                if (characters.size() > 4096) {
                    logger.warn("Buffer print limit, over 2048 characters, flushing out...");
                    flushConsole();
                    return;
                }

                if (b == 0x0A) {
                    if (!(characters.size() < 2)) {
                        flushConsole();
                    }
                }
                characters.add((char) b);
            }
        }
    }
        private static class OutStreamError extends OutputStream {
            private PrintStream systemOut;

            private static final Logger logger = LogManager.getLogger(ConsoleOutProxy.OutStreamError.class);

            @Getter
            private static OutStreamError instance;

            private final List<Character> characters = new ArrayList<>();

            public OutStreamError(){
                if(getInstance() != null){
                    throw new IllegalStateException("Console proxy can has only one instance");
                }
                instance = this;
            }

            private void flushConsole(){
                StringBuilder stringBuilder = new StringBuilder();
                for(char character : characters){
                    if(character == 0x0A || character == 0x0D){
                        continue;
                    }
                    stringBuilder.append((character));
                }
                console.warn(stringBuilder.toString().trim());
                logger.error(stringBuilder.toString().trim());
                characters.clear();
            }

            @Override
            public void write(int b) throws IOException {
                if(systemOut != null && console != null){
                    if(characters.size() > 4096){
                        logger.warn("Buffer print limit, over 2048 characters, flushing out...");
                        flushConsole();
                        return;
                    }

                    if(b == 0x0A){
                        if(!(characters.size() < 2)) {
                            flushConsole();
                        }
                    }
                    characters.add((char) b);
                }
            }
    }
}
