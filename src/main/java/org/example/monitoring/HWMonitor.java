package org.example.monitoring;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * This class monitors hardware, it can be used or not used
 * */
public class HWMonitor {
    private static final HWMonitor INSTANCE = new HWMonitor();

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final List<Runnable> hwTasks = new CopyOnWriteArrayList<>();
    private final AtomicReferenceArray<Object> hwValues = new AtomicReferenceArray<>(MetricType.values().length);

    private long pollingRate = 1000;

    // INIT
    private HWMonitor(){
        //Starts the loop
        scheduler.scheduleAtFixedRate(this::hwTasklist, 0, pollingRate, TimeUnit.MILLISECONDS);
    }
    private void hwTasklist(){
        for(Runnable task : hwTasks){
            try {
                task.run();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private static HWMonitor obj(){
        return INSTANCE;
    } // Static access key
    // INIT

    public static void addTask(HWTasks key){
        if (key != null && !INSTANCE.hwTasks.contains(key)){
            INSTANCE.hwTasks.add(key::poll);
        }
    }
    public static void runTask(HWTasks key){
        if (key != null){
            key.poll();
        }
    }
    public static List getTasks(){
        return INSTANCE.hwTasks;
    }

    public static void stop(){
        if (INSTANCE.scheduler != null){
            INSTANCE.scheduler.shutdown();
        }
    }

    public static void set(MetricType key, Object value) {
        INSTANCE.hwValues.set(key.ordinal(), value);
    }

    //Formatters
    public static double byteToKB(long input){
        return (double)input/1024;
    }
    public static double byteToMB(long input){
        return (double)input/1048576;
    }
    public static double byteToGB(long input){
        return (double)input/1073741824;
    }

    //Getters
    @SuppressWarnings("unchecked")
    public static Object getData(MetricType key){
        return INSTANCE.hwValues.get(key.ordinal());
    }
    //Setters
    public static void setPollingRate(int rate){
        INSTANCE.pollingRate = rate;
    }

    /**
     * @deprecated
     * RemoveTask, AddTask
     * This is no longer used please use the ENUM method
     * */
    @Deprecated
    public static void addTask(Runnable function){
        if (function != null){
            INSTANCE.hwTasks.add(function);
        }
    }
    @Deprecated
    public static void removeTask(Runnable function){
        if (function != null) {
            boolean removed = INSTANCE.hwTasks.remove(function);
            if (!removed) {
                throw new RuntimeException("Task wasn't found in the task list.");
            }
        }
    }

}
