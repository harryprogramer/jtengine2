package thewall.engine.twilight.errors;

import org.jetbrains.annotations.NotNull;

public class InitializationException extends RuntimeException{
    public InitializationException(String source, String additionalMsg){
        super(source  + " cannot be initialized, error: [" + additionalMsg + "]");
    }

    public InitializationException(String source, @NotNull Class<?> sourceclass){
        super(source + " is not initialized, " + sourceclass.getSimpleName());
    }

    public InitializationException(String msg){
        super(msg);
    }
}
