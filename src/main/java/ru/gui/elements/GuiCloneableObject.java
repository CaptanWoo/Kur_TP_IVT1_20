package ru.gui.elements;

import ru.utils.interfaces.ICloneable;
import ru.utils.interfaces.IData;

public abstract class GuiCloneableObject<T> extends GuiObject implements ICloneable<T>, IData {

    public GuiCloneableObject(double x, double y, double width, double height, boolean hasBorders) {
        super(x, y, width, height, hasBorders);
    }
}
