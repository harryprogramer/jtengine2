package thewall.engine.twilight.hardware.hna;

import thewall.engine.twilight.hardware.Processor;

final class HNAProcessor implements Processor {
    private final String name, vendor, family, model, identifier;
    private final boolean is64Bit;


    public HNAProcessor(String name, String vendor, String family,
                        String model, String identifier, boolean is64Bit){

        this.name = name;
        this.vendor = vendor;
        this.family = family;
        this.model = model;
        this.identifier = identifier;

        this.is64Bit = is64Bit;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getVendor() {
        return vendor;
    }

    @Override
    public String getFamily() {
        return family;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public boolean is64Bit() {
        return is64Bit;
    }
}
