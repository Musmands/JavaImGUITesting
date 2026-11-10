package org.example.UI;

import imgui.ImGui;
import imgui.ImGuiWindowClass;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.flag.ImGuiWindowFlags;

public class Stuff {
    private static final Stuff INSTANCE = new Stuff();
    private final WindowBounds windowBounds = new WindowBounds();


    public static void run(String text){
        INSTANCE.render(text);
    }
    public static boolean inBounds(short x, short y){
        return INSTANCE.windowBounds.inBounds(x, y);
    }

    public void render(String text){

        int flags = ImGuiWindowFlags.None;

        if (ImGui.begin("READ ME", flags)){
            if(ImGui.collapsingHeader("Child", ImGuiTreeNodeFlags.DefaultOpen)){
                ImGui.text(text);
            }

            ImGui.text("""
                    Hello! this is a test! Honestly I have been working on my prose,
                    the flow- variance of my words. I demand elegance. I demand continuity.
                    
                    P.S. The ram thing only tracks heap :)
                    
                    """);
        }
        ImGui.end();
    }



}
