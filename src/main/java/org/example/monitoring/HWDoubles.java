package org.example.monitoring;

public enum HWDoubles {
    MT_RAM_USED_RAW(0.0),
    MT_RAM_USED_KB(0.0),
    MT_RAM_USED_MB(0.0),
    MT_RAM_USED_GB(0.0),
    MT_CPU_USAGE(0.0);


    private final double defaultValue;
    HWDoubles(double defaultValue) {
        this.defaultValue = defaultValue;
    }
    public double getDefaultValue() {
        return defaultValue;
    }
}
