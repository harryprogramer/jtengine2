package jte2.engine.twilight.errors;

public class SyntaxException extends RuntimeException {
    public SyntaxException(Throwable t){
        super(t);
    }

    public SyntaxException(String msg){
        super(msg);
    }
}
