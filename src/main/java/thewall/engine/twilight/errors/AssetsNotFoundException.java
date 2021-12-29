package thewall.engine.twilight.errors;

public class AssetsNotFoundException extends RuntimeException {
    public AssetsNotFoundException(Throwable t){
        super(t);
    }

    public AssetsNotFoundException(String s){
        super(s);
    }

    public AssetsNotFoundException(){
        super();
    }
}
