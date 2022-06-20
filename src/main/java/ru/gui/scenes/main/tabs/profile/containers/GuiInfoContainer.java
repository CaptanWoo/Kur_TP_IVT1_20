package ru.gui.scenes.main.tabs.profile.containers;

import javafx.geometry.Pos;
import ru.controllers.YListController;
import ru.gui.GuiConstructor;
import ru.gui.elements.GuiCloneableObject;
import ru.gui.elements.GuiObject;
import ru.gui.elements.GuiYMoveableObject;

import java.util.ArrayList;
import java.util.List;

public class GuiInfoContainer<T extends GuiCloneableObject<T>> extends GuiObject {

    private final YListController<GuiYMoveableObject<T>> controller;

    public GuiInfoContainer(String title, T object, double containerX, double containerY, double containerWidth, double containerHeight, double buttonsX, double buttonsY, double buttonsWidth, double buttonsGap, boolean isHorizontal, boolean hasAddButton) {
        super(containerX, containerY, containerWidth, containerHeight);

        this.controller = new YListController<>(0, 23, containerWidth - 15, containerHeight - 15, false);
        controller.add(
                new GuiYMoveableObject<>(
                        object, controller,
                        0, 5, 5, object.getWidth(), object.getHeight(), 5,
                        buttonsX, buttonsY, buttonsWidth, buttonsGap, isHorizontal, hasAddButton
                )
        );
        root.getChildren().addAll(
                GuiConstructor.createLabel(title, 0, 0, containerWidth, 22, Pos.CENTER),
                controller.getScrollPane()
        );
    }

    public void prepareObjects(int count) {
        clear();
        for (int i = 1; i < count; i++) {
            controller.get(0).addAfter();
        }
    }

    public T getObject(int index) {
        return controller.get(index).getGuiObject();
    }

    public List<T> getObjects() {
        List<T> list = new ArrayList<>();
        for (GuiYMoveableObject<T> object: controller.getAll()) {
            list.add(object.getGuiObject());
        }
        return list;
    }

    public void clear() {
        controller.clear();
    }
}
