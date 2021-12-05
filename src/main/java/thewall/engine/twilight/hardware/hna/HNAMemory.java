package thewall.engine.twilight.hardware.hna;

import thewall.engine.twilight.hardware.Memory;

import java.util.List;

final class HNAMemory implements Memory {
    private long total, available, totalAlloc, availableAlloc;
    List<PhysicalMemory> psymem;

    public HNAMemory(long total, long available, long totalAlloc, long availableAlloc ,List<PhysicalMemory> psymem){
        this.total = total;
        this.available = available;
        this.totalAlloc = totalAlloc;
        this.availableAlloc = availableAlloc;
        this.psymem = psymem;
    }

    @Override
    public long getTotal() {
        return total;
    }

    @Override
    public long getAvailable() {
        return available;
    }

    @Override
    public long getTotalAllocated() {
        return totalAlloc;
    }

    @Override
    public long getAvailableAllocated() {
        return availableAlloc;
    }

    @Override
    public List<PhysicalMemory> getPhysicalMemory() {
        return psymem;
    }

    static final class HNAPhysicalMemory implements Memory.PhysicalMemory {
        private final long capacity, clockSpeed;
        private final String manufacturer, memoryType, bankLabel;

        public HNAPhysicalMemory(long capacity, String manufacturer, String memoryType, long clockSpeed, String bankLabel){
            this.capacity = capacity;
            this.clockSpeed = clockSpeed;

            this.manufacturer = manufacturer;
            this.memoryType = memoryType;
            this.bankLabel = bankLabel;
        }

        @Override
        public long getCapacity() {
            return capacity;
        }

        @Override
        public String getManufacturer() {
            return manufacturer;
        }

        @Override
        public String getMemoryType() {
            return memoryType;
        }

        @Override
        public long getClockSpeed() {
            return clockSpeed;
        }

        @Override
        public String getBankLabel() {
            return bankLabel;
        }
    }
}
