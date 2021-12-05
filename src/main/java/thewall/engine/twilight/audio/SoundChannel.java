package thewall.engine.twilight.audio;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonBlocking;

import java.io.IOException;
import java.nio.channels.Channel;

public interface SoundChannel extends Channel {
    @Override
    @NonBlocking
    @Contract(pure = true)
    boolean isOpen();

    @Override
    @NonBlocking
    void close() throws IOException;

    @NonBlocking
    default void stop() throws IOException {
        close();
    }
}
