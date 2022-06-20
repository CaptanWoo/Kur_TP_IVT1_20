package ru.controllers;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import ru.gui.GuiConstructor;
import ru.gui.elements.GuiYListedObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class YListController <T extends GuiYListedObject> {

    private final List<T> list;
    private final ScrollPane scrollPane;
    private final AnchorPane scrollRoot;
    private final boolean canRemoveFirst;

    public YListController(double x, double y, double width, double height, boolean canRemoveFirst) {
        list = new ArrayList<>();
        scrollPane = GuiConstructor.createScrollPane(scrollRoot = new AnchorPane(), x, y, width, height);
        this.canRemoveFirst = canRemoveFirst;
    }

    public T get(int index) {
        return list.get(index);
    }

    public List<T> getAll() {
        return list;
    }

    public void add(T object) {
        list.add(object);
        object.setIndex(list.size()-1);
        scrollRoot.getChildren().add(object.getRoot());
    }

    public void addAfter(T object) {
        object.setIndex(object.getIndex() + 1);
        list.add(object.getIndex(), object);
        scrollRoot.getChildren().add(object.getRoot());

        for (int i = 0; i < list.size(); i++) {
            list.get(i).setIndex(i);
        }
    }

    public void moveUp(int index) {
        if (index > 0) {
            Collections.swap(list, index, index-1);
            list.get(index-1).setIndex(index-1);
            list.get(index).setIndex(index);
        }
    }

    public void moveDown(int index) {
        if (index < list.size()-1) {
            Collections.swap(list, index, index+1);
            list.get(index+1).setIndex(index+1);
            list.get(index).setIndex(index);
        }
    }

    public void remove(int index) {
        if (list.size() == 0) return;
        if (index == 0 && list.size() == 1) {
            if (canRemoveFirst) {
                scrollRoot.getChildren().remove(list.get(index).getRoot());
                list.remove(index);
            } else list.get(index).clear();
        }
        if (list.size() == 1) return;
        if (list.size() <= index) return;

        scrollRoot.getChildren().remove(list.get(index).getRoot());
        list.remove(index);
        for (int i = index; i < list.size(); i++) {
            list.get(i).setIndex(i);
        }
    }

    public void clear() {
        while (list.size() > 1) {
            remove(list.size()-1);
        }
        remove(0);
    }

    public List<T> getObjects() {
        return list;
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public AnchorPane getScrollRoot() {
        return scrollRoot;
    }
}
