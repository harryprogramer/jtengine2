package thewall.engine.twilight.hardware.hna;

import thewall.engine.twilight.hardware.PowerSource;

final class HNAPowerSource implements PowerSource {
    private final double voltage, amperage, temperature;
    private final boolean powerOnLine, discharging, charging;
    private final int maxCapacity, currentCapacity;
    private final String manufacturer, deviceName, name;

    public HNAPowerSource(double voltage, double amperage, boolean powerOnLine,
                          boolean discharging, boolean charging, int maxCapacity,
                          int currentCapacity, double temperature, String manufacturer,
                          String deviceName, String name){
        this.voltage = voltage;
        this.amperage = amperage;

        this.powerOnLine = powerOnLine;
        this.discharging = discharging;
        this.charging = charging;

        this.maxCapacity = maxCapacity;
        this.currentCapacity = currentCapacity;

        this.temperature = temperature;

        this.manufacturer = manufacturer;
        this.deviceName = deviceName;
        this.name = name;
    }

    @Override
    public double getVoltage() {
        return voltage;
    }

    @Override
    public double getAmperage() {
        return amperage;
    }

    @Override
    public boolean isPowerOnLine() {
        return powerOnLine;
    }

    @Override
    public boolean isDischarging() {
        return discharging;
    }

    @Override
    public boolean isCharging() {
        return charging;
    }

    @Override
    public int getMaxCapacity() {
        return maxCapacity  ;
    }

    @Override
    public int getCurrentCapacity() {
        return currentCapacity;
    }

    @Override
    public double getTemperature() {
        return temperature;
    }

    @Override
    public String getManufacturer() {
        return manufacturer;
    }

    @Override
    public String getDeviceName() {
        return deviceName;
    }

    @Override
    public String getName() {
        return name;
    }
}
