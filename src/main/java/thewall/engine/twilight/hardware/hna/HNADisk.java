package thewall.engine.twilight.hardware.hna;

import thewall.engine.twilight.hardware.Disk;

final class HNADisk implements Disk {
    private final String name, model, serial;
    private final long size, reads, writes, writeBytes, currentQueueLength, transferTime;


    public HNADisk(String name, String model, String serial,
                   long size, long reads, long writes, long writeBytes,
                   long currentQueueLength, long transferTime){
        this.name = name;
        this.model = model;
        this.serial = serial;

        this.size = size;
        this.reads = reads;
        this.writes = writes;
        this.writeBytes = writeBytes;
        this.currentQueueLength = currentQueueLength;
        this.transferTime = transferTime;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public String getSerial() {
        return serial;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public long getReads() {
        return reads;
    }

    @Override
    public long getWrites() {
        return writes;
    }

    @Override
    public long getWriteBytes() {
        return writeBytes;
    }

    @Override
    public long getCurrentQueueLength() {
        return currentQueueLength;
    }

    @Override
    public long getTransferTime() {
        return transferTime;
    }
}
