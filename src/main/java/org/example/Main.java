package org.example;

import imgui.ImGui;
import imgui.app.Application;
import imgui.app.Configuration;

public class Main extends Application {

    @Override
    protected void configure(Configuration config) {
    }

    @Override
    public void process() {
        // This runs every single frame. Write your UI code here!
        ImGui.begin("Control Panel");

        ImGui.text("Hello, ImGui is working!");

        if (ImGui.button("Click Me")) {
            System.out.println("Button was clicked!");
        }

        ImGui.end();
    }


    public static void main(String[] args) {
        launch(new Main());
    }
}