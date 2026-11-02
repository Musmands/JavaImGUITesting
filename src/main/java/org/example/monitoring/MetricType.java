package org.example.monitoring;

public enum MetricType {
    MT_RAM_USED_RAW(0l),
    MT_RAM_USED_KB(0.0),
    MT_RAM_USED_MB(0.0),
    MT_RAM_USED_GB(0.0),
    MT_CPU_USAGE(0.0);


    private final Object defaultValue;
    MetricType(Object defaultValue) {
        this.defaultValue = defaultValue;
    }
    public Object getDefaultValue() {
        return defaultValue;
    }
}
