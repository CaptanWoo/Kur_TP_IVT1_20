package ru.gui.scenes.main.tabs.resumes.scenes.info.objects;

import javafx.scene.control.Label;
import ru.gui.GuiConstructor;
import ru.gui.elements.GuiCloneableObject;

public class GuiLabelObject extends GuiCloneableObject<GuiLabelObject> {

    private final Label label;

    public GuiLabelObject(String text, double x, double y, double width, double height) {
        super(x, y, width, height, true);

        root.getChildren().add(
                label = GuiConstructor.createLabel(text, 5, 2, width - 10, 16)
        );
    }

    @Override
    public GuiLabelObject getClone() {
        return null;
    }

    @Override
    public String save() {
        return null;
    }

    @Override
    public void load(String data) {
        label.setText(data);
    }

    @Override
    public void clear() {
        label.setText("");
    }
}
