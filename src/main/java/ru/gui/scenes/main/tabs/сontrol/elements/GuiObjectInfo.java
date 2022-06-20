package ru.gui.scenes.main.tabs.сontrol.elements;

import javafx.stage.Stage;
import ru.dto.control.CommonObjectDTO;
import ru.gui.GuiConstructor;
import ru.gui.elements.GuiYListedObject;
import ru.gui.scenes.main.tabs.сontrol.ControlTab;
import ru.gui.scenes.main.tabs.сontrol.scenes.SceneObjectChange;
import ru.gui.windows.WindowController;
import ru.utils.enums.EnumStatus;

public class GuiObjectInfo extends GuiYListedObject {

    public GuiObjectInfo(ControlTab controlTab, CommonObjectDTO dto) {
        super(0, 5, 5, 600, 31, 5, true);

        root.getChildren().addAll(
                GuiConstructor.createLabel("id: " + dto.getId(), 10, 8, 70, 14),
                GuiConstructor.createLabel("Статус: " + EnumStatus.getEnum(dto.getStatus()).getDisplayName(), 90, 8, 140, 14),
                GuiConstructor.createLabel("Название: " + dto.getDisplayName(), 240, 8, 327, 14),
                GuiConstructor.createButton(e -> new SceneObjectChange(controlTab, new WindowController(new Stage()), dto.getId(), dto.getStatus(), dto.getDisplayName()), "⚙", 570, 3, 27, 25)
        );
    }
}
