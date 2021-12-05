package thewall.engine.twilight.audio.jmf;

import org.jetbrains.annotations.NotNull;
import thewall.engine.twilight.audio.SoundChannel;
import thewall.engine.twilight.audio.SoundMaster;
import thewall.engine.twilight.audio.SoundMetadata;
import thewall.engine.twilight.audio.errors.SoundStoppedException;
import thewall.engine.twilight.audio.errors.UnsupportedAudioFormat;

import javax.sound.sampled.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public final class SoundManager implements SoundMaster {

    private static class SoundThread extends Thread implements SoundChannel, SoundMetadata {

        private boolean isRunning = false;

        private final String file;
        private final float pitch;
        private final BufferedInputStream fileStream;
        private final float volume;

        private Clip clip;

        public SoundThread(float volume, float pitch, String file, BufferedInputStream fileStream){
            this.file = file;
            this.pitch = pitch;
            this.volume = volume;
            this.fileStream = fileStream;

            if (volume < 0f || volume > 1f)
                throw new IllegalArgumentException("Volume not valid: " + volume);
        }

        @Override
        public void run() {
            try {
                clip = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(fileStream);
                clip.open(inputStream);
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(20f * (float) Math.log10(volume));
                isRunning = true;
                clip.start();
            } catch (Exception e) {
                Logger.getLogger(SoundManager.class.getSimpleName()).log(Level.WARNING, String.format("Sound [%s] couldn't be played, [%s].", file, e.getMessage()));
            }
        }

        @Override
        public void close() throws IOException {
            if(!isRunning || clip == null){
                throw new SoundStoppedException(this);
            }
            isRunning = false;
            clip.stop();
        }

        @Override
        public boolean isOpen() {
            return isRunning || clip == null ||clip.isActive();
        }

        @Override
        public @NotNull Map<String, Object> getMetadata() {
            return clip == null ? new HashMap<>() : clip.getFormat().properties();
        }

        @Override
        public float getVolume() {
            return volume;
        }

        @Override
        public float getPitch() {
            return pitch;
        }

        @Override
        public @NotNull String getSoundPath() {
            return file;
        }

        @Override
        public @NotNull File getSoundFile() {
            return new File(file);
        }
    }
    @SuppressWarnings("unused")
    private @NotNull SoundChannel playSoundAsync(float volume, float pitch, String file){
        SoundThread soundThread;
        try{
            soundThread = new SoundThread(volume, volume, file, new BufferedInputStream(new FileInputStream(file)));
        }catch (FileNotFoundException e){
            try{
                soundThread = new SoundThread(volume, volume, file, new BufferedInputStream(new FileInputStream("./res/music/" + file)));
            }catch (FileNotFoundException ignored){
                throw new RuntimeException(String.format("File [%s] couldn't not be found", file));
            }
        }
        soundThread.start();
        return soundThread;
    }


    @Override
    public @NotNull SoundChannel playBackground(float volume, float pitch, @NotNull String file) {
        String format = file.substring(file.length() - 4); // TODO stupid format fucking idiot
        if(!format.equalsIgnoreCase(".wav")){
            throw new UnsupportedAudioFormat(format);
        }
        return playSoundAsync(volume, pitch, file);
    }
}
