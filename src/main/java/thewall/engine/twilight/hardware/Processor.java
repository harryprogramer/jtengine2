package thewall.engine.twilight.hardware;

public interface Processor {
    String getName();

    String getVendor();

    String getFamily();

    String getModel();

    String getIdentifier();

    boolean is64Bit();

    interface LogicalProcessor{
        int getProcessorNumber();

        int getPhysicalProcessorNumber();

        int getPhysicalPackageNumber();
    }
}
