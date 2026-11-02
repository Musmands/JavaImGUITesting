package org.example;


import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.example.UI.Diagnostics;
import org.example.UI.Stuff;
import org.example.monitoring.HWMonitor;

import org.example.monitoring.HWTasks;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


public class Main{
    private long windowHandle;

    private final ImGuiImplGlfw imguiGLFW = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    public static void main(String[] args){
        new Main().run();
    }

    private void mainLoop() {

        glClearColor(.1f,.1f,.1f, 0.0f);

        while (!glfwWindowShouldClose(windowHandle)) {
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // Put imgui stuff here
            imGuiGl3.newFrame();
            imguiGLFW.newFrame();
            ImGui.newFrame();

            // MINI CLASSES
            Diagnostics.render();
            Stuff.render();
            // MINI CLASSES

            ImGui.render();
            imGuiGl3.renderDrawData(ImGui.getDrawData());
            // Put imgui stuff here

            glfwSwapBuffers(windowHandle);
        }
    }


    //Boiler Plate
    private void initWindow(){
        if (!glfwInit()){
            throw new IllegalStateException("Unable to start GLFW");
        }

        //Sets the correct openGl version
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3); //Selects OpenGL version 3.3
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3); //
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE); // Selects BareBones OpenGL 3.3

        //PreConfig
        glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, GLFW_TRUE); // Allows the frame to be transparent

        //Makes the window
        windowHandle = glfwCreateWindow(700, 500, "Bare Bones ImGui", 0, 0);
        if (windowHandle == 0) {
            throw new RuntimeException("Failed to create window");
        }

        //PostConfig
        glfwMakeContextCurrent(windowHandle); //Binds window to CPU thread
        glfwShowWindow(windowHandle); //Makes it visible
        glfwSwapInterval(1); //VSYNC ON
        GL.createCapabilities(); //Hooks onto the correct OPENGL with your GPU

        //Blending for semi transparent
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        System.out.println("Started Window with no problems");
    }
    private void initImGui() {
        ImGui.createContext();
        imguiGLFW.init(windowHandle, true);
        imGuiGl3.init("#version 330");

        ImGui.getStyle().setColor(ImGuiCol.TitleBg, 0.15f, 0.25f, 0.35f, 1.0f);

        System.out.println("Started ImGui correctly, waiting for bindings:");
    }
    private void initHwMonitor(){
    }
    public void run(){
        initWindow();
        initHwMonitor();
        initImGui();
        mainLoop();
        cleanup();
    }
    private void cleanup() {
        System.out.println("Starting Shutdown Sequence");

        imGuiGl3.shutdown(); //Shutdown OPENGL
        imguiGLFW.shutdown(); //Shutdown GLFW
        ImGui.destroyContext();
        glfwDestroyWindow(windowHandle);
        glfwTerminate();

        HWMonitor.stop();

        System.out.println("Shutdown completed successfully");
    }
}