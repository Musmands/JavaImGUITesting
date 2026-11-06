package org.example.UI;

import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.type.ImString;
import org.example.monitoring.HWMonitor;
import oshi.hardware.NetworkIF;
import oshi.jna.platform.mac.SystemB;

import static org.example.monitoring.HWTasks.HWT_RAM_USED;
import static org.example.monitoring.HWDoubles.MT_RAM_USED_MB;

public class Diagnostics {
    private Diagnostics(){
        throw new AssertionError("You cannot make a member of this class");
    }

    private final static double INTERVAL = 50;
    private final static double INTERVAL_MS = INTERVAL/1000;

    private static double timer;

    private static String fpsF = "";
    private static String frameTime = "";
    private static String ramHeapTracked = "";

    private static final ImString input = new ImString(64);


    public static void render(){

        //init

        if (ImGui.begin("Diagnostics")){

            pollValues();

            if (ImGui.collapsingHeader("FPS MONITORING", ImGuiTreeNodeFlags.DefaultOpen)){
                fpsChild();
            }

            if (ImGui.collapsingHeader("RAM MONITORING", ImGuiTreeNodeFlags.DefaultOpen)){
                ramChild();
            }

            ImGui.inputText("##hiddenInput", input);
            ImGui.sameLine();

            if (ImGui.button("Click to enter")){
                String text = input.get();
                System.out.println(text);
            }

        }
        ImGui.end();
    }

    public static void fpsChild(){
        ImGui.text(fpsF);
        ImGui.text(frameTime);
    }
    public static void ramChild(){
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
    }

    public static void pollValues(){
        timer += ImGui.getIO().getDeltaTime();
        if (timer >= INTERVAL_MS){
            timer -= INTERVAL_MS;

            // Get new values
            fpsF = String.format("FPS: %.2f" , ImGui.getIO().getFramerate());
            frameTime = String.format("Frame Time: %.3f ms", 1000.0f / ImGui.getIO().getFramerate());
            ramHeapTracked = String.format("Ram used: %.3f MB", HWMonitor.getData(MT_RAM_USED_MB));
        }
    }


}
