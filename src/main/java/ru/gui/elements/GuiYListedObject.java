package ru.gui.elements;

import ru.utils.interfaces.ICleareable;

public class GuiYListedObject extends GuiObject implements ICleareable {

    protected int index;
    protected final double gap;

    public GuiYListedObject(int index, double x, double y, double width, double height, double gap, boolean hasBorders) {
        super(x, y, width, height, hasBorders);
        this.gap = gap;
        setIndex(index);
    }

    public GuiYListedObject(int index, double x, double y, double width, double height, double gap) {
        super(x, y, width, height);
        this.gap = gap;
        setIndex(index);
    }

    public void setIndex(int index) {
        this.index = index;
        updatePosition();
    }

    public int getIndex() {
        return index;
    }

    protected void updatePosition() {
        root.setLayoutY(y + (height + gap) * index);
    }

    @Override
    public void clear() {

    }
}
