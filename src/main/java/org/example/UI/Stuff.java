package org.example.UI;

import imgui.ImGui;

public class Stuff {


    public static void render(){
        if (ImGui.begin("READ ME")){
            ImGui.text("""
                    Hello! this is a test! Honestly I have been working on my prose,
                    the flow- variance of my words. I demand elegance. I demand continuity.
                    
                    P.S. The ram thing only tracks heap :)
                    
                    """);
        ImGui.end();
        }
    }

}
