package ru.gui.elements;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import ru.gui.GuiConstructor;
import ru.objects.Page;

public class GuiObject {

    protected final AnchorPane root;
    protected final Rectangle border;
    protected final Rectangle rectangle;

    protected double x;
    protected double y;
    protected double width;
    protected double height;

    public GuiObject(double x, double y, double width, double height) {
        this(x, y, width, height, false);
    }

    public GuiObject(double x, double y, double width, double height, boolean hasBorders) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        root = GuiConstructor.createAnchorPane(x, y, width, height);

        if (hasBorders) {
            border = GuiConstructor.createRectangle("#C8C8C8", 0, 0, width, height);
            rectangle = GuiConstructor.createRectangle("#FFFFFF", 1, 1, width-2, height-2);
            root.getChildren().addAll(border, rectangle);
        } else {
            border = GuiConstructor.createRectangle("#C8C8C8", 0, 0, 0, 0);
            rectangle = GuiConstructor.createRectangle("#FFFFFF", 0, 0, width, height);
            root.getChildren().addAll(rectangle);
        }
    }

    public AnchorPane getRoot() {
        return root;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        root.setPrefWidth(width);
        rectangle.setWidth(width);
    }

    public double getHeight() {
        return height;
    }
}
