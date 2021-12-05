package thewall.engine.twilight.hardware.hna;

import oshi.SystemInfo;
import oshi.hardware.*;
import thewall.engine.twilight.hardware.*;
import thewall.engine.twilight.hardware.PowerSource;
import thewall.engine.twilight.hardware.SoundCard;

import java.util.ArrayList;
import java.util.List;

public abstract class HardwareNativeAccess implements Hardware {

    private final SystemInfo systemInfo;

    public HardwareNativeAccess(){
        this.systemInfo = new SystemInfo();
    }

    protected List<Disk> getHNADisks(){
        List<Disk> hnadisks = new ArrayList<>();
        List<HWDiskStore> disks = systemInfo.getHardware().getDiskStores();

        for(HWDiskStore disk : disks){
            hnadisks.add(new HNADisk(disk.getName(), disk.getModel(), disk.getSerial(),
                    disk.getSize(), disk.getReads(), disk.getWrites(), disk.getWriteBytes(), disk.getCurrentQueueLength(), disk.getTransferTime()));
        }
        return hnadisks;
    }

    protected List<Graphic> getHNAGraphics(){
        List<Graphic> hnaGraphic = new ArrayList<>();
        List<GraphicsCard> graphics = systemInfo.getHardware().getGraphicsCards();

        for(GraphicsCard graphic : graphics){
            hnaGraphic.add(new HNAGraphic(graphic.getName(), graphic.getVendor(), graphic.getDeviceId(), graphic.getVersionInfo(), graphic.getVRam()));
        }

        return hnaGraphic;
    }

    protected Memory getHNAMemory(){
        GlobalMemory glMemory = systemInfo.getHardware().getMemory();
        List<Memory.PhysicalMemory> physicalMemHNA = new ArrayList<>();
        List<PhysicalMemory> physicalMem = glMemory.getPhysicalMemory();

        for(PhysicalMemory memory : physicalMem){
            physicalMemHNA.add(new HNAMemory.HNAPhysicalMemory(memory.getCapacity(), memory.getManufacturer(),
                    memory.getMemoryType(), memory.getClockSpeed(), memory.getBankLabel()));
        }

        return new HNAMemory(glMemory.getTotal(), glMemory.getAvailable(), getMaxMemory(), getFreeMemory(), physicalMemHNA);
    }

    protected List<PowerSource> getHNAPowerSource(){
        List<oshi.hardware.PowerSource> powerSources = systemInfo.getHardware().getPowerSources();
        List<PowerSource> powerSourcesHNA = new ArrayList<>();

        for(oshi.hardware.PowerSource powerSource : powerSources){
            powerSourcesHNA.add(new HNAPowerSource(powerSource.getVoltage(), powerSource.getAmperage(), powerSource.isPowerOnLine(),
                    powerSource.isDischarging(), powerSource.isCharging(), powerSource.getMaxCapacity(), powerSource.getCurrentCapacity(),
                    powerSource.getTemperature(), powerSource.getManufacturer(), powerSource.getDeviceName(), powerSource.getName()));
        }

        return powerSourcesHNA;
    }

    protected Processor getHNAProcessor(){
        CentralProcessor centralProcessor = systemInfo.getHardware().getProcessor();
        return new HNAProcessor(centralProcessor.getProcessorIdentifier().getName(),
                centralProcessor.getProcessorIdentifier().getVendor(), centralProcessor.getProcessorIdentifier().getFamily(),
                centralProcessor.getProcessorIdentifier().getModel(), centralProcessor.getProcessorIdentifier().getIdentifier(),
                centralProcessor.getProcessorIdentifier().isCpu64bit());
    }

    protected List<SoundCard> getHNASoundCards(){
        List<oshi.hardware.SoundCard> soundCards = systemInfo.getHardware().getSoundCards();
        List<SoundCard> soundCardsHNA = new ArrayList<>();

        for(oshi.hardware.SoundCard soundCard : soundCards){
            soundCardsHNA.add(new HNASoundCard(soundCard.getDriverVersion(), soundCard.getName(), soundCard.getCodec()));
        }

        return soundCardsHNA;
    }

    private static long getMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    private static long getUsedMemory() {
        return getMaxMemory() - getFreeMemory();
    }

    private static long getTotalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    private static long getFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    protected int getHNACurrentProcessID(){
        return systemInfo.getOperatingSystem().getProcessId();
    }

    protected int getHNAProcessCount(){
        return systemInfo.getOperatingSystem().getProcessCount();
    }

    protected long getHNASystemUpTime(){
        return systemInfo.getOperatingSystem().getSystemUptime();
    }

    protected long getHNASystemBootTime(){
        return systemInfo.getOperatingSystem().getSystemBootTime();
    }

    protected String getHNASystemName(){
        return systemInfo.getOperatingSystem().getVersionInfo().toString();
    }

    protected String getHNASystemVersion(){
        return systemInfo.getOperatingSystem().getVersionInfo().getVersion();
    }

    protected String getHNASystemCodeName(){
        return systemInfo.getOperatingSystem().getVersionInfo().getCodeName();
    }

    protected String getHNASystemBuildNumber(){
        return systemInfo.getOperatingSystem().getVersionInfo().getBuildNumber();
    }

    protected String getHNASystemFamily(){
        return systemInfo.getOperatingSystem().getFamily();
    }

    protected String getHNASystemManufacturer(){
        return systemInfo.getOperatingSystem().getManufacturer();
    }

    protected String getHNABaseboardManufacturer(){
        return systemInfo.getHardware().getComputerSystem().getBaseboard().getManufacturer();
    }

    protected String getHNABaseboardModel(){
        return systemInfo.getHardware().getComputerSystem().getBaseboard().getModel();
    }

    protected String getHNABaseboardVersion(){
        return systemInfo.getHardware().getComputerSystem().getBaseboard().getVersion();
    }

    protected String getHNABaseboardSerialNumber(){
        return systemInfo.getHardware().getComputerSystem().getBaseboard().getSerialNumber();
    }

    protected PlatformEnum getHNAPlatform(){
        oshi.PlatformEnum platform = SystemInfo.getCurrentPlatform();
        PlatformEnum currentPlatform;

        switch (platform){
            case AIX -> currentPlatform = PlatformEnum.AIX;
            case ANDROID -> currentPlatform = PlatformEnum.ANDROID;
            case FREEBSD -> currentPlatform = PlatformEnum.FREEBSD;
            case GNU ->  currentPlatform = PlatformEnum.GNU;
            case LINUX -> currentPlatform = PlatformEnum.LINUX;
            case KFREEBSD -> currentPlatform = PlatformEnum.KFREEBSD;
            case MACOS, MACOSX -> currentPlatform = PlatformEnum.MACOS;
            case NETBSD ->  currentPlatform = PlatformEnum.NETBSD;
            case OPENBSD -> currentPlatform = PlatformEnum.OPENBSD;
            case SOLARIS ->  currentPlatform = PlatformEnum.SOLARIS;
            case WINDOWSCE -> currentPlatform = PlatformEnum.WINDOWSCE;
            case WINDOWS -> currentPlatform = PlatformEnum.WINDOWS;
            default -> currentPlatform = PlatformEnum.UNKNOWN;
        }

        return currentPlatform;
    }

}
