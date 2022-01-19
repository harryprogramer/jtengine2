package jte2.engine.twilight.audio.jmf;

import io.swagger.models.auth.In;
import jte2.engine.twilight.errors.AssetsNotFoundException;
import jte2.engine.twilight.utils.SafeArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import jte2.engine.twilight.audio.SoundChannel;
import jte2.engine.twilight.audio.SoundMaster;
import jte2.engine.twilight.audio.SoundMetadata;
import jte2.engine.twilight.audio.errors.SoundStoppedException;
import jte2.engine.twilight.audio.errors.UnsupportedAudioFormat;

import javax.sound.sampled.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;


public final class SoundManager implements SoundMaster {
    private final static Logger logger = LogManager.getLogger(SoundManager.class);
    private final static Map<Short, SoundThread> clips = new ConcurrentHashMap<>();

    private static class SoundThread extends Thread implements SoundChannel, SoundMetadata {
        private final static Logger logger = LogManager.getLogger(SoundThread.class);
        private boolean isRunning = false;

        private final short id;
        private final String file;
        private final float pitch;
        private final BufferedInputStream fileStream;
        private final float volume;

        private Clip clip;

        public SoundThread(float volume, float pitch, String file, BufferedInputStream fileStream, short id){
            this.file = file;
            this.pitch = pitch;
            this.volume = volume;
            this.fileStream = fileStream;
            this.id = id;

            setName("AudioThread-" + id);

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
                logger.info("Starting audio [{}] with id: {}, buffer: {}, frames: {}, position: {}", file, id, clip.getBufferSize(), clip.getFrameLength(), clip.getLongFramePosition());
            } catch (Exception e) {
                logger.info(String.format("Sound [%s] IO fault detected while playing [%s].", file, e.getMessage()));
            }
        }

        @Override
        public void close() throws IOException {
            if(!isRunning || clip == null){
                throw new SoundStoppedException(this);
            }

            logger.info("Stopping clip [{}], id: {}, end frame: {}", file, id ,clip.getLongFramePosition());

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
        short id = (short) new Random().nextInt(Short.MAX_VALUE + 1);
        try{
            soundThread = new SoundThread(volume, volume, file, new BufferedInputStream(new FileInputStream(file)), id);
        }catch (FileNotFoundException e){
            try{
                soundThread = new SoundThread(volume, volume, file, new BufferedInputStream(new FileInputStream("./res/music/" + file)), id);
            }catch (FileNotFoundException ignored){
                throw new AssetsNotFoundException(String.format("Cannot find file %s, is the resource folder in the same folder as the program?", file));
            }
        }
        clips.put(id, soundThread);
        soundThread.start();
        return soundThread;
    }


    @Override
    public @NotNull SoundChannel playBackground(float volume, float pitch, @NotNull String file) {
        String format = file.substring(file.length() - 4); // TODO stupid format check fucking idiot
        if(!format.equalsIgnoreCase(".wav")){
            throw new UnsupportedAudioFormat(format);
        }
        return playSoundAsync(volume, pitch, file);
    }

    @Override
    public void stopMaster() {
        logger.info("Stopping JMF Sound Master, [{}] clips to close.", clips.size());
        for (Map.Entry<Short, SoundThread> entry : clips.entrySet()) {
            SoundThread channel = entry.getValue();
            short id = entry.getKey();
            if(channel.isOpen()) {
                logger.info("Interrupting clip [{}] with id: [{}]", channel.getSoundPath(), id);
                try {
                    channel.close();
                } catch (IOException e) {
                    logger.error("Cannot interrupt clip, io error [{}], {}", id, e.getMessage());
                }
            }else {
                logger.info("Clip [{}] is already closed, skipping.", id);
            }
        }
        clips.clear();
    }
}
