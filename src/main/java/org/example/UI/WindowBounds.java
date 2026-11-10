package org.example.UI;

import imgui.ImGui;
import imgui.ImVec2;

public class WindowBounds {
    private final ImVec2 winPos = new ImVec2();
    private final ImVec2 winSize = new ImVec2();

    public void update() {
        ImGui.getWindowPos(winPos);
        ImGui.getWindowSize(winSize);
    }

    public boolean inBounds(short x, short y) {
        return x >= winPos.x && x <= winPos.x + winSize.x &&
                y >= winPos.y && y <= winPos.y + winSize.y;
    }
}
