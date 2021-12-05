package thewall.engine.twilight.audio.errors;

public class UnsupportedAudioFormat extends RuntimeException{
    public UnsupportedAudioFormat(String format){
        super(String.format("Unsupported audio stream [%s], only .wav is supported in this moment", format));
    }
}
