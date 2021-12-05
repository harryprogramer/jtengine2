package thewall.engine.twilight.audio;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public interface VectorSoundMaster extends SoundMaster{
    @Override
    @NotNull SoundChannel playBackground(float volume, float pitch, String file);

    @NotNull SoundChannel playVector(Vector3f vector3f, float volume, float pitch, String file);
}
