package thewall.engine.twilight.errors;

import org.jetbrains.annotations.NotNull;

public class TextureDecoderException extends RuntimeException{
    private Exception parentException;

    public TextureDecoderException(){
        super();
    }

    public TextureDecoderException(String cause){
        super(cause);
    }

    public TextureDecoderException(@NotNull Exception parentException){
        super(parentException.getMessage());
        this.parentException = parentException;
    }

    public boolean isParentExceptionCaught(){
        return parentException != null;
    }

    public Exception getParentException(){
        return parentException;
    }
}
