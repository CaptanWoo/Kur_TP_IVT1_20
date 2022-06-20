package ru.gui.scenes.main.tabs;

import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

public class TabItem {

    private final Tab tab;
    protected final AnchorPane root;

    public TabItem(String title) {
        tab = new Tab(title, root = new AnchorPane());
        tab.setClosable(false);
    }

    public Tab getTab() {
        return tab;
    }

    public AnchorPane getRoot() {
        return root;
    }
}
