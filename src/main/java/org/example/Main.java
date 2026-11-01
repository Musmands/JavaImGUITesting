package org.example;


import imgui.ImGui;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.example.monitoring.HWMonitor;

import static org.example.monitoring.MetricType.*;
import static org.example.monitoring.HWTasks.*;

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

        glClearColor(.2f,.2f,.2f, .0f);

        while (!glfwWindowShouldClose(windowHandle)) {
            glfwPollEvents();
            // Put imgui stuff here
            imGuiGl3.newFrame();
            imguiGLFW.newFrame();
            ImGui.newFrame();

            ImGui.begin("Diagnostics");
            ImGui.text(String.format("FPS: %.1f", ImGui.getIO().getFramerate()));
            ImGui.text(String.format("Frame Time: %.3f ms", 1000.0f / ImGui.getIO().getFramerate()));
            ImGui.text("Diddy sees me");


            ImGui.text("RAM TRACKING:");
            if (ImGui.button("START")){
                System.out.println("Adding ram used");
                HWMonitor.addTask(HWT_RAM_USED);
            }
            if (ImGui.button("STOP")){
                System.out.println("Removing ram used");
                HWMonitor.removeTask(HWT_RAM_USED);

            }
            ImGui.separator();
            ImGui.text("Tracking stats:");
            ImGui.text(String.format("Ram used: %.3f MB", (double) HWMonitor.getData(MT_RAM_USED_MB)));


            ImGui.end();


            // Put imgui stuff here
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            ImGui.render();
            imGuiGl3.renderDrawData(ImGui.getDrawData());

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