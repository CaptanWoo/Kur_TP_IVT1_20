package ru.gui.scenes.main.tabs.resumes.scenes.info.objects;

import ru.gui.elements.GuiCloneableObject;
import ru.gui.elements.GuiYListedObject;

public class GuiInfoListedObject<T extends GuiCloneableObject<T>> extends GuiYListedObject {

    private final T guiObject;

    public GuiInfoListedObject(T guiObject, int index, double x, double y, double width, double height) {
        super(index, x, y, width, height, 5);
        this.guiObject = guiObject;

        root.getChildren().addAll(
                guiObject.getRoot()
        );
    }
}
