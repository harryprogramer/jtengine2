package thewall.engine.twilight.hardware;

public interface Disk {
    String getName();

    String getModel();

    String getSerial();

    long getSize();

    long getReads();

    long getWrites();

    long getWriteBytes();

    long getCurrentQueueLength();

    long getTransferTime();
}
