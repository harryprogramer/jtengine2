package thewall.engine.twilight.audio.errors;

import thewall.engine.twilight.audio.SoundMetadata;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SoundStoppedException extends IOException {
    public SoundStoppedException(@NotNull SoundMetadata meta){
        super("This sound [" + valueOfString(meta) + "] is already stopped");
    }

    public static @NotNull String valueOfString(@NotNull SoundMetadata soundMetadata){
        return "SoundMetadata{" +
                "soundPath=" + soundMetadata.getSoundPath() +
                ", metadata=" + soundMetadata.getMetadata() +
                ", soundFile=" + soundMetadata.getSoundFile() +
                ", volume=" + soundMetadata.getVolume() +
                ", pitch=" + soundMetadata.getPitch() +
                '}';
    }
}
