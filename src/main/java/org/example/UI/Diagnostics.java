package org.example.UI;

import imgui.ImGui;
import org.example.monitoring.HWMonitor;

import static org.example.monitoring.HWTasks.HWT_RAM_USED;
import static org.example.monitoring.MetricType.MT_RAM_USED_MB;

public class Diagnostics {
    private Diagnostics(){
        throw new AssertionError("You cannot make a member of this class");
    }

    private final static double INTERVAL = 100;
    private final static double INTERVAL_MS = INTERVAL/1000;

    private static double timer;

    private static String fpsF = "";
    private static String frameTime = "";
    private static String ramHeapTracked = "";


    public static void render(){

        //init

        if (ImGui.begin("Diagnostics")){

            pollValues();

            ImGui.text(fpsF);
            ImGui.text(frameTime);
            ImGui.text("Diddy sees me");


            ImGui.text("RAM TRACKING:");
            if (ImGui.button("START")){
                System.out.println("Adding ram used");
                HWMonitor.addTask(HWT_RAM_USED);
            }

            ImGui.sameLine();

            if (ImGui.button("STOP")){
                System.out.println("Removing ram used");
                HWMonitor.removeTask(HWT_RAM_USED);

            }

            ImGui.sameLine();

            if (ImGui.button("RESET")){
                System.out.println("Resetting ram");
                HWMonitor.set(MT_RAM_USED_MB, 0.0);
            }

            ImGui.separator();
            ImGui.text("Tracking stats:");
            ImGui.text(ramHeapTracked);

            ImGui.end();
        }
    }

    public static void pollValues(){
        timer += ImGui.getIO().getDeltaTime();
        if (timer >= INTERVAL_MS){
            timer = 0;
            // Get new values
            fpsF = String.format("FPS: %.2f" , ImGui.getIO().getFramerate());
            frameTime = String.format("Frame Time: %.3f ms", 1000.0f / ImGui.getIO().getFramerate());
            ramHeapTracked = String.format("Ram used: %.3f MB", (double) HWMonitor.getData(MT_RAM_USED_MB));
        }
    }


}
