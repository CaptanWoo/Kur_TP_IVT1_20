package ru.gui.elements;

import ru.gui.GuiConstructor;

public class GuiYControlButtons extends GuiObject {

    private final double buttonsWidth;
    private final double buttonsGap;
    private final boolean isHorizontal;
    private final boolean hasAddButton;

    public GuiYControlButtons(double x, double y, double buttonsWidth, double buttonsGap, boolean isHorizontal, boolean hasAddButton, GuiYMoveableObject<?> object) {
        super(x, y, isHorizontal ? (buttonsWidth+buttonsGap)*3 + buttonsWidth : 25, isHorizontal ? buttonsWidth : (25+buttonsGap)*4);
        this.buttonsWidth = buttonsWidth;
        this.buttonsGap = buttonsGap;
        this.isHorizontal = isHorizontal;
        this.hasAddButton = hasAddButton;

        double dif = 0;

        if (hasAddButton) {
            root.getChildren().add(
                    GuiConstructor.createButton(e -> object.addAfter(), "+", 0, 0, buttonsWidth, 25)
            );
        } else {
            dif -= (buttonsWidth + buttonsGap);
        }

        setWidth(getWidth() + dif);

        if (isHorizontal) {
            root.getChildren().addAll(

                    GuiConstructor.createButton(e -> object.moveUp(), "↑", (buttonsWidth + buttonsGap) * 1 + dif, 0, buttonsWidth, 25),
                    GuiConstructor.createButton(e -> object.moveDown(), "↓", (buttonsWidth + buttonsGap) * 2 + dif, 0, buttonsWidth, 25),
                    GuiConstructor.createButton(e -> object.remove(), "x", (buttonsWidth + buttonsGap) * 3 + dif, 0, buttonsWidth, 25)
            );
        } else {
            root.getChildren().addAll(
                    GuiConstructor.createButton(e -> object.moveUp(), "↑", 0, (25 + buttonsGap) * 1 + dif, buttonsWidth, 25),
                    GuiConstructor.createButton(e -> object.moveDown(), "↓", 0, (25 + buttonsGap) * 2 + dif, buttonsWidth, 25),
                    GuiConstructor.createButton(e -> object.remove(), "x", 0, (25 + buttonsGap) * 3 + dif, buttonsWidth, 25)
            );
        }
    }

    public double getButtonsWidth() {
        return buttonsWidth;
    }

    public double getButtonsGap() {
        return buttonsGap;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    public boolean isHasAddButton() {
        return hasAddButton;
    }
}
