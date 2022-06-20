package ru.gui.elements;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import ru.controllers.YListController;
import ru.gui.GuiConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class GuiList extends GuiObject {

    private final MenuButton mbType;
    private final YListController<GuiYListedLabel> listController;

    public GuiList(String title, String[] types, List<String> list, double x, double y) {
        super(x, y, 285, 250, true);
        listController = new YListController<>(5, 35,260, 195, true);

        root.getChildren().addAll(
                GuiConstructor.createLabel(title, 10, 10, 100, 15, Pos.CENTER),
                mbType = GuiConstructor.createMenuButton(e->{}, types[0], Arrays.asList(types), 115, 5, 60, 15),
                GuiConstructor.createAddMenuButton(this::add,"Добавить", list, 180, 5, 100, 25),
                listController.getScrollPane()
        );
    }

    public boolean getType() {
        return mbType.getText().equals("И");
    }

    public void add(String text) {
        for (GuiYListedLabel object: listController.getObjects()) {
            if (object.getLabelText().equals(text)) return;
        }
        listController.add(new GuiYListedLabel(listController, text,0,2, 2, 255, 29, 2));
    }

    public List<GuiYListedLabel> getAll() {
        return listController.getObjects();
    }

    public void clear() {
        listController.clear();
    }
}
