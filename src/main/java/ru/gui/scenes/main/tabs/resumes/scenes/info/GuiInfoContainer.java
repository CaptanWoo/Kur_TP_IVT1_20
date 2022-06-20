package ru.gui.scenes.main.tabs.resumes.scenes.info;

import javafx.geometry.Pos;
import ru.controllers.YListController;
import ru.gui.GuiConstructor;
import ru.gui.GuiObject;
import ru.gui.elements.GuiYListedObject;
import ru.utils.vectors.DVector2;

import java.util.List;

public class GuiInfoContainer<T extends GuiYListedObject> extends GuiObject {

    private final YListController<T> controller;

    public GuiInfoContainer(String title, List<T> objects, double x, double y, double width, double height) {
        super(new DVector2(x, y), new DVector2(width, height));

        controller = new YListController<>(0, 33, width - 15, height - 15 - 33, true);

        for (T object: objects) controller.add(object);

        root.getChildren().addAll(
                GuiConstructor.createLabel(title, 0, 0, width, 33, Pos.CENTER),
                controller.getScrollPane()
        );
    }
}
