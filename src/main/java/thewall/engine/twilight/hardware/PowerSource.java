package thewall.engine.twilight.hardware;

public interface PowerSource {
    double getVoltage();

    double getAmperage();

    boolean isPowerOnLine();

    boolean isDischarging();

    boolean isCharging();

    int getMaxCapacity();

    int getCurrentCapacity();

    double getTemperature();

    String getManufacturer();

    String getDeviceName();

    String getName();
}
