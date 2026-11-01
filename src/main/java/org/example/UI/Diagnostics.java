package org.example.UI;

import imgui.ImGui;
import org.example.monitoring.HWMonitor;

import static org.example.monitoring.HWTasks.HWT_RAM_USED;
import static org.example.monitoring.MetricType.MT_RAM_USED_MB;

public class Diagnostics {
    private Diagnostics(){
        throw new AssertionError("You cannot make a member of this class");
    }
    public static void render(){

        if (ImGui.begin("Diagnostics")){
            ImGui.text(String.format("FPS: %.1f", ImGui.getIO().getFramerate()));
            ImGui.text(String.format("Frame Time: %.3f ms", 1000.0f / ImGui.getIO().getFramerate()));
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
            ImGui.separator();
            ImGui.text("Tracking stats:");
            ImGui.text(String.format("Ram used: %.3f MB", (double) HWMonitor.getData(MT_RAM_USED_MB)));
        }
        ImGui.end();
    }
}
