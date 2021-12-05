package thewall.engine.twilight.hardware;

import java.util.List;

public interface Memory {
    long getTotal();

    long getAvailable();

    long getTotalAllocated();

    long getAvailableAllocated();

    List<PhysicalMemory> getPhysicalMemory();

    interface PhysicalMemory {
        long getCapacity();

        String getManufacturer();

        String getMemoryType();

        long getClockSpeed();

        String getBankLabel();
    }
}
