package ru.gui.elements;

import javafx.scene.control.Label;
import ru.controllers.YListController;
import ru.gui.GuiConstructor;

public class GuiYListedLabel extends GuiYListedObject {

    private final Label label;

    public GuiYListedLabel(YListController<GuiYListedLabel> listController, String text, int index, double x, double y, double width, double height, double gap) {
        super(index, x, y, width, height, gap);
        root.getChildren().addAll(
                label = GuiConstructor.createLabel(text, 2, 0, width - 26, height),
                GuiConstructor.createButton(e -> listController.remove(index), "x", width - 26, 2, 22, 25)
        );
    }

    public String getLabelText() {
        return label.getText();
    }
}
