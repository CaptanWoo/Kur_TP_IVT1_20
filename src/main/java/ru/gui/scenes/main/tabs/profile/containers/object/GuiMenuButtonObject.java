package ru.gui.scenes.main.tabs.profile.containers.object;

import javafx.scene.control.MenuButton;
import ru.gui.GuiConstructor;
import ru.gui.elements.GuiCloneableObject;

import java.util.Collection;
import java.util.List;

public class GuiMenuButtonObject extends GuiCloneableObject<GuiMenuButtonObject> {

    private final MenuButton menuButton;
    private final List<String> list;

    public GuiMenuButtonObject(double x, double y, double width, double height, List<String> list) {
        super(x, y, width, height, true);
        this.list = list;

        root.getChildren().add(
                menuButton = GuiConstructor.createMenuButton(e -> {}, "", list, 2, 2, 247, 25)
        );
    }

    @Override
    public GuiMenuButtonObject getClone() {
        return new GuiMenuButtonObject(x, y, width, height, list);
    }

    @Override
    public String save() {
        return menuButton.getText();
    }

    @Override
    public void load(String data) {
        menuButton.setText(data);
    }

    @Override
    public void clear() {
        menuButton.setText("");
    }
}
