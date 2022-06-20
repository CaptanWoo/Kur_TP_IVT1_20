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

public class SceneObjectChange extends SceneContainer {

    private EnumStatus status = EnumStatus.NOT_STATED;
    private final MenuButton mbStatus;
    private final TextField tfDisplayName;
    private final Label labelStatus;

    public SceneObjectChange(ControlTab controlTab, WindowController windowController, long id, String status, String displayName) {
        super(windowController,"Создание объекта", new DVector2(290, 135));

        root.getChildren().addAll(
                GuiConstructor.createLabel("id: " + id, 10, 10, 100, 15),
                GuiConstructor.createLabel("Статус:", 110, 10, 50, 15, Pos.CENTER_RIGHT),
                mbStatus = GuiConstructor.createMenuButton(
                        e -> updateStatus(),
                        EnumStatus.getEnum(status).getDisplayName(),
                        EnumStatus.getDisplayNamesAsList(),
                        165, 5, 120, 25),
                tfDisplayName = GuiConstructor.createTextField(displayName, 5, 35, 280, 25),
                labelStatus = GuiConstructor.createLabel("", 5, 63, 280, 14, Pos.CENTER),
                GuiConstructor.createButton(e -> {
                    try {
                        controlTab.replaceObject(new CommonObjectDTO(id, this.status.getUnlocalizedName(), tfDisplayName.getText()));
                        windowController.close();
                    } catch (Exception ex) {
                        labelStatus.setText(ex.getMessage());
                        ex.printStackTrace();
                    }
                }, "Изменить", 95, 80, 100, 25)
        );

        updateStatus();
        windowController.setScene(this);
        windowController.show();
    }

    private void updateStatus() {
        status = EnumStatus.getEnumByDisplayName(mbStatus.getText());
    }
}
