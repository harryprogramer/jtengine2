package thewall.engine.twilight.hardware.hna;

import thewall.engine.twilight.hardware.SoundCard;

final class HNASoundCard implements SoundCard {
    private final String driverVersion, name, codec;

    public HNASoundCard(String driverVersion, String name, String codec){
        this.driverVersion = driverVersion;
        this.name = name;
        this.codec = codec;
    }

    @Override
    public String getDriverVersion() {
        return driverVersion;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCodec() {
        return codec;
    }
}
