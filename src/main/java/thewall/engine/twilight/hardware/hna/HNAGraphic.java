package thewall.engine.twilight.hardware.hna;

import thewall.engine.twilight.hardware.Graphic;

final class HNAGraphic implements Graphic {
    private final String name, vendor, deviceID, version;
    private final long vram;

    public HNAGraphic(String name, String vendor,
                      String deviceID, String version,
                      long vram){

        this.name = name;
        this.vendor = vendor;
        this.deviceID = deviceID;
        this.version = version;

        this.vram = vram;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getVendor() {
        return vendor;
    }

    @Override
    public String getDeviceID() {
        return deviceID;
    }

    @Override
    public String getVersionInfo() {
        return version;
    }

    @Override
    public long getVRam() {
        return vram;
    }
}
