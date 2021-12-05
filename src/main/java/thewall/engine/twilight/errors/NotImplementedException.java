package thewall.engine.twilight.errors;

public class NotImplementedException extends UnsupportedOperationException{
    public static final NotImplementedException NOT_IMPLEMENTED = new NotImplementedException();

    public NotImplementedException(){
        super("Not implemented yet.");
    }
}
