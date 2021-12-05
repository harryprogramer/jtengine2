package thewall.engine.twilight.hardware;

public interface SoundCard {
    String getDriverVersion();

    String getName();

    String getCodec();
}
