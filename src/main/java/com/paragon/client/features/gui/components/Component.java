package com.paragon.client.features.gui.components;

public abstract class Component {
    public float offset;

    public abstract void renderComponent(int mouseX, int mouseY);
    public void updateComponent(int mouseX, int mouseY) {}
    public void mouseClicked(int mouseX, int mouseY, int button) {}
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {}
    public void keyTyped(char typedChar, int key) {}
    public void setOff(double newOff) {}
    public int getHeight() {return 0;}
}
