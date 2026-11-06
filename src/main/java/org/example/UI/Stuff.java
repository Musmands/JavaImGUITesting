package org.example.UI;

import imgui.ImGui;
import imgui.ImGuiWindowClass;
import imgui.flag.ImGuiWindowFlags;

public class Stuff {


    public static void render(){

        int flags = ImGuiWindowFlags.None;

        if (ImGui.begin("READ ME", flags)){
            ImGui.text("""
                    Hello! this is a test! Honestly I have been working on my prose,
                    the flow- variance of my words. I demand elegance. I demand continuity.
                    
                    P.S. The ram thing only tracks heap :)
                    
                    """);

            if(ImGui.collapsingHeader("Child")){
                ImGui.text("Hello");
            }

        }
        ImGui.end();
    }



}
