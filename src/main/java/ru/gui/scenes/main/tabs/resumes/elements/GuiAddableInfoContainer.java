package ru.gui.scenes.main.tabs.resumes.elements;

import javafx.geometry.Pos;
import javafx.stage.Stage;
import ru.controllers.YListController;
import ru.gui.GuiConstructor;
import ru.gui.elements.GuiCloneableObject;
import ru.gui.elements.GuiObject;
import ru.gui.elements.GuiYMoveableObject;
import ru.gui.scenes.SceneAddableObjects;
import ru.gui.windows.WindowController;
import ru.utils.interfaces.IGetGuiAddableObjects;

import java.util.ArrayList;
import java.util.List;

public class GuiAddableInfoContainer<T extends GuiCloneableObject<T>> extends GuiObject {

    private final YListController<GuiYMoveableObject<T>> controller;

    public GuiAddableInfoContainer(String title, T object, List<GuiAddableObject<T>> objects, double containerX, double containerY, double containerWidth, double containerHeight, double buttonsX, double buttonsY, double buttonsWidth, double buttonsGap, boolean isHorizontal, boolean hasAddButton) {
        super(containerX, containerY, containerWidth, containerHeight);

        this.controller = new YListController<>(0, 33, containerWidth - 15, containerHeight - 15, true);
        //controller.add(new GuiYMoveableObject<>(object, controller, 0, 5, 5, object.getWidth(), object.getHeight(), 5, buttonsX, buttonsY, buttonsWidth, buttonsGap, isHorizontal, hasAddButton));
        root.getChildren().addAll(
                GuiConstructor.createLabel(title, 0, 0, containerWidth, 33, Pos.CENTER),
                GuiConstructor.createButton(e -> {
                    new SceneAddableObjects<>(new WindowController(new Stage()), objects);
                }, "Добавить", 346, 4, 120, 25),
                controller.getScrollPane()
        );
    }

    public void addObject(GuiYMoveableObject<T> object) {
        controller.add(object);
    }

    public void prepareObjects(int count) {
        clear();
        for (int i = 1; i < count; i++) {
            controller.get(0).addAfter();
        }
    }

    public YListController<GuiYMoveableObject<T>> getController() {
        return controller;
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
