package ru.gui.scenes;

import ru.controllers.YListController;
import ru.gui.elements.GuiCloneableObject;
import ru.gui.scenes.main.tabs.resumes.elements.GuiAddableObject;
import ru.gui.windows.WindowController;
import ru.utils.vectors.DVector2;

import java.util.List;

public class SceneAddableObjects<T extends GuiCloneableObject<T>>  extends SceneContainer {

    private final YListController<GuiAddableObject<T>> controller;

    public SceneAddableObjects(WindowController windowController, List<GuiAddableObject<T>> objects) {
        super(windowController, "Выбор информации из профиля", new DVector2(515, 800));

        controller = new YListController<>(0, 0, 455, 440, false);
        root.getChildren().addAll(
                controller.getScrollRoot()
        );

        addObjects(objects);

        this.windowController.setScene(this);
        this.windowController.show();
    }

    private void addObjects(List<GuiAddableObject<T>> objects) {
        for (GuiAddableObject<T> object: objects) {
            controller.add(object);
        }
    }
}
