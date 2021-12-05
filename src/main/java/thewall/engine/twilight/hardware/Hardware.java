package thewall.engine.twilight.hardware;

import java.util.List;

public interface Hardware extends System {
    List<Disk> getDisks();

    List<Graphic> getGraphicCards();

    List<SoundCard> getSoundCards();

    List<PowerSource> getPowerSources();

    Graphic getUsedGraphic();

    Memory getMemory();

    Processor getProcessor();
}
