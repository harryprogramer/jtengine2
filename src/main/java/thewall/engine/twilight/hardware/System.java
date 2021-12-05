package thewall.engine.twilight.hardware;

public interface System {
    int getCurrentProcessID();

    int getProcessCount();

    long getSystemUptime();

    long getSystemBootTime();

    String getSystemName();

    String getFamily();

    String getManufacturer();

    String getBuildNumber();

    String getBaseboardManufacturer();

    String getBaseboardModel();

    String getBaseboardVersion();

    String getBaseboardSerialNumber();

    String getVersion();

    PlatformEnum getPlatform();
}
