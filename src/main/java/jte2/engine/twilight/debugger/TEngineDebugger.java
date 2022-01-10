package jte2.engine.twilight.debugger;

import lombok.Getter;
import jte2.engine.twilight.debugger.console.ConsoleOutProxy;
import jte2.engine.twilight.debugger.console.DebugConsole;

import java.io.PrintStream;

public final class TEngineDebugger {
    @Getter
    private static PrintStream nativeOut = null;

    @Getter
    private static PrintStream nativeOutErr = null;

    private TEngineDebugger(){
        throw new IllegalStateException("This class cannot have instance");
    }

    public static void setPrintProxyDebugger(DebugConsole console){
        ConsoleOutProxy.setConsole(console);
        nativeOut = System.out;
        nativeOutErr = System.err;
        System.setOut(ConsoleOutProxy.getInstance());
        System.setErr(ConsoleOutProxy.getInstanceErr());
    }

}
