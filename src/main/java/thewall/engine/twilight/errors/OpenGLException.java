package thewall.engine.twilight.errors;

public class OpenGLException extends RuntimeException {
    public OpenGLException(String error){
        super(error);
    }

    public OpenGLException(Throwable t){
        super(t);
    }

    public OpenGLException(){
        super("Unhandled or unknown OpenGL error");
    }
}
