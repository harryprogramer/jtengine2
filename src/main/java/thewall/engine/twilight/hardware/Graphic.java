package thewall.engine.twilight.hardware;

public interface Graphic {
    String getName();

    String getVendor();

    String getDeviceID();

    String getVersionInfo();

    long getVRam();
}
