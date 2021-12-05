package thewall.engine.twilight.debugger;

import lombok.Setter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import thewall.engine.twilight.debugger.console.DebugConsole;

@Plugin(name = "TEngineDebugConsoleAppender", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE)
public class TEngineLoggerAppender extends AbstractAppender {
    @Setter
    private static DebugConsole debugConsole = DebugConsole.getConsole();

    protected TEngineLoggerAppender(String name, Filter filter) {
        super(name, filter, null);
    }

    @Override
    public void append(LogEvent event) {
        if(debugConsole != null){
            if (Level.INFO.equals(event.getLevel())) {
                debugConsole.info(event.getMessage().getFormattedMessage());
            }else if(Level.WARN.equals(event.getLevel())){
                debugConsole.warn(event.getMessage().getFormattedMessage());
            }else if(Level.FATAL.equals(event.getLevel())){
                debugConsole.fatal(event.getMessage().getFormattedMessage());
            }else if(Level.DEBUG.equals(event.getLevel())){
                debugConsole.debug(event.getMessage().getFormattedMessage());
            }
        }
    }

    @Contract("_, _ -> new")
    @PluginFactory
    public static @NotNull TEngineLoggerAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Filter") Filter filter) {
        return new TEngineLoggerAppender(name, filter);
    }
}
