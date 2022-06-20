package ru.gui.scenes.main.tabs.resumes.elements;

import ru.gui.GuiConstructor;
import ru.gui.elements.GuiCloneableObject;
import ru.gui.elements.GuiYListedObject;
import ru.utils.interfaces.IExecutable;

public class GuiAddableObject<T extends GuiCloneableObject<T>> extends GuiYListedObject {

    private final T guiObject;

    public GuiAddableObject(T guiObject, IExecutable iExecutable, int index, double x, double y, double width, double height) {
        super(index, x, y, width, height, 5);
        this.guiObject = guiObject;

        root.getChildren().addAll(
                guiObject.getRoot(),
                GuiConstructor.createButton(e -> iExecutable.execute(guiObject.save()), "Добавить", width - 90, 5, 85, 25)
        );
    }
}
