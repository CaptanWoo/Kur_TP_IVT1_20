package ru.gui.scenes.main.tabs.сontrol.scenes;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import ru.dto.control.CommonObjectDTO;
import ru.gui.GuiConstructor;
import ru.gui.scenes.SceneContainer;
import ru.gui.scenes.main.tabs.сontrol.ControlTab;
import ru.gui.windows.WindowController;
import ru.utils.enums.EnumStatus;
import ru.utils.vectors.DVector2;

public class SceneObjectCreate extends SceneContainer {

    private EnumStatus status = EnumStatus.NOT_STATED;
    private final MenuButton mbStatus;
    private final TextField tfDisplayName;
    private final Label labelStatus;

    public SceneObjectCreate(ControlTab controlTab, WindowController windowController) {
        super(windowController,"Создание объекта", new DVector2(290, 135));

        root.getChildren().addAll(
                GuiConstructor.createLabel("Статус:", 110, 10, 50, 15, Pos.CENTER_RIGHT),
                mbStatus = GuiConstructor.createMenuButton(e -> updateStatus(), EnumStatus.ACTIVE.getDisplayName(), EnumStatus.getDisplayNamesAsList(), 165, 5, 120, 25),
                tfDisplayName = GuiConstructor.createTextField("", 5, 35, 280, 25),
                labelStatus = GuiConstructor.createLabel("", 5, 63, 280, 14, Pos.CENTER),
                GuiConstructor.createButton(e -> {
                    try {
                        controlTab.addObject(new CommonObjectDTO(0, status.getUnlocalizedName(), tfDisplayName.getText()));
                        windowController.close();
                    } catch (Exception ex) {
                        labelStatus.setText(ex.getMessage());
                        ex.printStackTrace();
                    }
                }, "Добавить", 95, 80, 100, 25)
        );

        updateStatus();
        windowController.setScene(this);
        windowController.show();
    }

    private void updateStatus() {
        status = EnumStatus.getEnumByDisplayName(mbStatus.getText());
    }
}

