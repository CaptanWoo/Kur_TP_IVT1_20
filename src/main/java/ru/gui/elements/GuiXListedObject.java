package ru.gui.elements;

public class GuiXListedObject extends GuiObject {

    protected int index;
    private final double gap;

    public GuiXListedObject(int index, double x, double y, double width, double height, double gap) {
        super(x, y, width, height);
        this.gap = gap;
    }

    public void setIndex(int index) {
        this.index = index;
        updatePosition();
    }

    public int getIndex() {
        return index;
    }

    protected void updatePosition() {
        root.setLayoutX(x + (width + gap) * index);
    }
}
