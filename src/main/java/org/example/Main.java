package org.example;


import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.ImGuiViewport;
import imgui.ImVec2;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiHoveredFlags;
import imgui.flag.ImGuiViewportFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.internal.ImGuiWindow;
import org.example.UI.Diagnostics;
import org.example.UI.Stuff;
import org.example.monitoring.HWMonitor;

import org.example.monitoring.HWTasks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWNativeWin32;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.windows.POINT;
import org.lwjgl.system.windows.User32;

import java.awt.*;

import static org.example.monitoring.HWTasks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFWNativeWin32.glfwGetWin32Window;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.windows.User32.*;


public class Main{
    private long windowHandle;

    private final ImGuiImplGlfw imguiGLFW = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    long hwnd;

    public static void main(String[] args){
        new Main().run();
    }

    private void mainLoop() {
        //Reused Variables
        short x = 0, y = 0;
        final double INTERVAL = 100;
        final double INTERVAL_MS = INTERVAL/1000;
        double timer = 0;


        glClearColor(.0f,.0f,.0f, 0.0f);

        while (!glfwWindowShouldClose(windowHandle)) {
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            //ImGui Pass through behavior
            ImGuiIO io = ImGui.getIO();
            ImGuiViewport viewport = ImGui.getMainViewport();
            double delta = io.getDeltaTime();
            boolean focused = GLFW.glfwGetWindowAttrib(windowHandle, GLFW.GLFW_FOCUSED) == GLFW.GLFW_TRUE;
            boolean wantInput = io.getWantCaptureMouse();

            if (!wantInput){
                viewport.addFlags(ImGuiViewportFlags.NoInputs);
            }

            boolean inputAllow = viewport.hasFlags(ImGuiViewportFlags.NoInputs);


            // Put imgui stuff here
            imGuiGl3.newFrame();
            imguiGLFW.newFrame();
            ImGui.newFrame();

            // MINI CLASSES
            Diagnostics.run();
            Stuff.run(   x + ", " + y + " " + inputAllow + " ");
            // MINI CLASSES

            //Restore focus make sure to register all imgui UIS
            timer += delta;
            if (timer > INTERVAL_MS){
                timer -= INTERVAL_MS;

                //GET MOUSE XY globally
                try (MemoryStack stack = MemoryStack.stackPush()){
                    POINT mousePos = POINT.malloc(stack);
                    User32.GetCursorPos(mousePos);

                    x = (short)mousePos.x();
                    y = (short)mousePos.y();
                }

                if(Diagnostics.inBounds(x,y) | Stuff.inBounds(x,y)){
                    viewport.removeFlags(ImGuiViewportFlags.NoInputs);
                    if (!focused){
                    GLFW.glfwFocusWindow(windowHandle);
                    }
                }
            }
            //

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
        glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);
        glfwWindowHint(GLFW_FLOATING, GLFW_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

        //Makes the window
        long primaryMonitor = GLFW.glfwGetPrimaryMonitor();
        GLFWVidMode vidMode = GLFW.glfwGetVideoMode(primaryMonitor);

        int width = vidMode.width();
        int height = vidMode.height();

        windowHandle = glfwCreateWindow(width, height-1, "Bare Bones ImGui", 0, 0);
        if (windowHandle == 0) {
            throw new RuntimeException("Failed to create window");
        }

        hwnd = glfwGetWin32Window(windowHandle);

        //PostConfig
        glfwSetWindowAttrib(windowHandle, GLFW_MOUSE_PASSTHROUGH, GLFW_TRUE);
        glfwMakeContextCurrent(windowHandle); //Binds window to CPU thread
        glfwShowWindow(windowHandle); //Makes it visible
        glfwSwapInterval(1); //VSYNC ON
        GL.createCapabilities(); //Hooks onto the correct OPENGL with your GPU

        SetWindowLongPtr(null, hwnd, GWL_EXSTYLE, WS_EX_TOOLWINDOW);

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
        HWMonitor.addTask(HWT_RAM_USED);
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