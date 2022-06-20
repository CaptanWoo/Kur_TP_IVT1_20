package ru.gui;

import javafx.scene.layout.AnchorPane;
import ru.utils.vectors.DVector2;

public class GuiObject {

    protected final AnchorPane root;
    private final DVector2 pos;
    private final DVector2 size;

    public GuiObject(DVector2 pos, DVector2 size) {
        root = new AnchorPane();
        this.pos = new DVector2(pos);
        this.size = new DVector2(size);
        root.setLayoutX(pos.x);
        root.setLayoutY(pos.y);
        root.setPrefWidth(size.x);
        root.setPrefHeight(size.y);
    }

    public AnchorPane getRoot() {
        return root;
    }
}
