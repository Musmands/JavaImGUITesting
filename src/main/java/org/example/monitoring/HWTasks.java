package org.example.monitoring;

public enum HWTasks {
    HWT_RAM_USED {
        @Override
        public void poll() {
            Runtime runtime = Runtime.getRuntime();
            long usedHeap = runtime.totalMemory() - runtime.freeMemory();
            double usedKB = HWMonitor.byteToKB(usedHeap);
            double usedMB = HWMonitor.byteToMB(usedHeap);
            double usedGB = HWMonitor.byteToGB(usedHeap);

            // Sends the data back to the monitor using 'this' as the key
            HWMonitor.set(HWDoubles.MT_RAM_USED_RAW, (double) usedHeap);
            HWMonitor.set(HWDoubles.MT_RAM_USED_KB, usedKB);
            HWMonitor.set(HWDoubles.MT_RAM_USED_MB, usedMB);
            HWMonitor.set(HWDoubles.MT_RAM_USED_GB, usedGB);
        }
    };

    public abstract void poll();
}
