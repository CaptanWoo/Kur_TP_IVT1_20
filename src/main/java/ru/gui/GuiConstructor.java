package ru.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import ru.utils.interfaces.IExecutable;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class GuiConstructor {

    public static AnchorPane createAnchorPane(double x, double y, double width, double height) {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setLayoutX(x);
        anchorPane.setLayoutY(y);
        anchorPane.setPrefWidth(width);
        anchorPane.setPrefHeight(height);
        return anchorPane;
    }

    public static ScrollPane createScrollPane(AnchorPane scrollRoot, double x, double y, double width, double height) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setLayoutX(x);
        scrollPane.setLayoutY(y);
        scrollPane.setPrefViewportWidth(width);
        scrollPane.setPrefViewportHeight(height);
        scrollPane.setContent(scrollRoot);
        return scrollPane;
    }

    public static AnchorPane createRoot(double x, double y, double width, double height) {
        AnchorPane root = new AnchorPane();
        root.setLayoutX(x);
        root.setLayoutY(y);
        root.setPrefWidth(width);
        root.setPrefHeight(height);
        return root;
    }

    public static Label createLabel(String text, double x, double y, double width, double height) {
        Label label = new Label(text);
        label.setLayoutX(x);
        label.setLayoutY(y);
        label.setPrefWidth(width);
        label.setPrefHeight(height);
        return label;
    }


    public static Label createLabel(String text, double x, double y, double width, double height, Pos pos) {
        Label label = new Label(text);
        label.setLayoutX(x);
        label.setLayoutY(y);
        label.setPrefWidth(width);
        label.setPrefHeight(height);
        label.setAlignment(pos);
        return label;
    }

    public static TextField createTextField(String text, double x, double y, double width, double height) {
        TextField textField = new TextField(text);
        textField.setLayoutX(x);
        textField.setLayoutY(y);
        textField.setPrefWidth(width);
        textField.setPrefHeight(height);
        return textField;
    }

    public static PasswordField createPasswordField(double x, double y, double width, double height) {
        PasswordField passwordField = new PasswordField();
        passwordField.setLayoutX(x);
        passwordField.setLayoutY(y);
        passwordField.setPrefWidth(width);
        passwordField.setPrefHeight(height);
        return passwordField;
    }

    public static TextArea createTextArea(String text, boolean isWrapped, double x, double y, double width, double height) {
        TextArea textArea = new TextArea(text);
        textArea.setWrapText(isWrapped);
        textArea.setLayoutX(x);
        textArea.setLayoutY(y);
        textArea.setPrefWidth(width);
        textArea.setPrefHeight(height);
        return textArea;
    }
    public static TextArea createTextArea(String text, boolean isWrapped, boolean isEditable, double x, double y, double width, double height) {
        TextArea textArea = new TextArea(text);
        textArea.setWrapText(isWrapped);
        textArea.setEditable(isEditable);
        textArea.setLayoutX(x);
        textArea.setLayoutY(y);
        textArea.setPrefWidth(width);
        textArea.setPrefHeight(height);
        return textArea;
    }

    public static Button createButton(EventHandler<ActionEvent> e, String text, double x, double y, double width, double height) {
        Button button = new Button(text);
        button.setOnAction(e);
        button.setLayoutX(x);
        button.setLayoutY(y);
        button.setPrefWidth(width);
        button.setPrefHeight(height);
        return button;
    }

    public static MenuButton createAddMenuButton(IExecutable iExecutable, String text, List<String> list, double x, double y, double width, double height) {
        MenuButton menuButton = new MenuButton(text);
        menuButton.setMnemonicParsing(false);

        for (String s: list) {
            MenuItem menuItem = new MenuItem(s);
            menuItem.setMnemonicParsing(false);
            menuItem.setOnAction(e -> iExecutable.execute(menuItem.getText()));
            menuButton.getItems().add(menuItem);
        }

        menuButton.setLayoutX(x);
        menuButton.setLayoutY(y);
        menuButton.setPrefWidth(width);
        menuButton.setPrefHeight(height);
        return menuButton;
    }

    public static MenuButton createMenuButton(IExecutable iExecutable, String text, int min, int max, double x, double y, double width, double height) {
        MenuButton mb = createMenuButton(iExecutable, min, max, x, y, width, height);
        mb.setText(text);
        return mb;
    }
    public static MenuButton createMenuButton(IExecutable iExecutable, int min, int max, double x, double y, double width, double height) {
        MenuButton menuButton = new MenuButton();
        menuButton.setMnemonicParsing(false);

        for (int i = min; i <= max; i++) {
            MenuItem menuItem = new MenuItem(String.valueOf(i));
            menuItem.setMnemonicParsing(false);
            menuItem.setOnAction(e -> {
                menuButton.setText(menuItem.getText());
                iExecutable.execute(menuItem.getText());
            });
            menuButton.getItems().add(menuItem);
        }

        menuButton.setLayoutX(x);
        menuButton.setLayoutY(y);
        menuButton.setPrefWidth(width);
        menuButton.setPrefHeight(height);
        return menuButton;
    }

    public static MenuButton createMenuButton(IExecutable iExecutable, String text, List<String> collection, double x, double y, double width, double height) {
        MenuButton menuButton = new MenuButton(text);
        menuButton.setMnemonicParsing(false);

        if (collection != null) {
            for (String s : collection) {
                MenuItem menuItem = new MenuItem(s);
                menuItem.setMnemonicParsing(false);
                menuItem.setOnAction(e -> {
                    menuButton.setText(menuItem.getText());
                    iExecutable.execute(menuItem.getText());
                });
                menuButton.getItems().add(menuItem);
            }
        }

        menuButton.setLayoutX(x);
        menuButton.setLayoutY(y);
        menuButton.setPrefWidth(width);
        menuButton.setPrefHeight(height);
        return menuButton;
    }

    public static CheckBox createCheckBox(EventHandler<ActionEvent> e, boolean isSelected, String text, double x, double y) {
        CheckBox checkBox = new CheckBox(text);
        checkBox.setSelected(isSelected);
        checkBox.setOnAction(e);
        checkBox.setLayoutX(x);
        checkBox.setLayoutY(y);
        return checkBox;
    }
    public static Rectangle createRectangle(String color, double x, double y, double width, double height) {
        Rectangle rectangle = new Rectangle();
        rectangle.setLayoutX(x);
        rectangle.setLayoutY(y);
        rectangle.setWidth(width);
        rectangle.setHeight(height);
        if (color != null) rectangle.setFill(Paint.valueOf(color));
        return rectangle;
    }
}
