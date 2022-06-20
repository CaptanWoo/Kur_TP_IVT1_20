package ru.gui.scenes.main.tabs.сontrol;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import ru.Client;
import ru.Server;
import ru.controllers.YListController;
import ru.dto.control.CommonObjectDTO;
import ru.gui.GuiConstructor;
import ru.gui.scenes.main.tabs.TabItem;
import ru.gui.scenes.main.tabs.сontrol.elements.GuiObjectInfo;
import ru.gui.scenes.main.tabs.сontrol.scenes.SceneObjectCreate;
import ru.gui.windows.WindowController;

import java.util.List;

public class ControlTab extends TabItem {

    private String listName;
    private Label labelListName;
    private final YListController<GuiObjectInfo> controller = new YListController<>(285, 50, 610, 780, true);
    public ControlTab() {
        super("Панель управления");

        root.getChildren().addAll(
                GuiConstructor.createButton(e -> {}, "Пользователи", 20, 20, 120, 25),
                GuiConstructor.createButton(e -> setListName("Типы компаний"), "Типы компаний", 145, 20, 120, 25),
                GuiConstructor.createButton(e -> setListName("Специализации"), "Специализации", 145, 50, 120, 25),
                GuiConstructor.createButton(e -> setListName("Навыки"), "Навыки", 145, 80, 120, 25),
                GuiConstructor.createButton(e -> setListName("Языки"), "Языки", 145, 110, 120, 25),
                GuiConstructor.createButton(e -> setListName("Гражданства"), "Гражданства", 145, 140, 120, 25),
                labelListName = GuiConstructor.createLabel("Не выбрано", 285, 19, 520, 27, Pos.CENTER),
                GuiConstructor.createButton(e -> new SceneObjectCreate(this, new WindowController(new Stage())), "Добавить", 725, 20, 80, 25),
                GuiConstructor.createButton(e -> update(), "Обновить", 810, 20, 80, 25),
                controller.getScrollPane()
        );
    }

    private void setListName(String listName) {
        this.listName = listName;
        labelListName.setText(listName);
        update();
    }

    public void replaceObject(CommonObjectDTO dto) throws Exception {
        switch (listName) {
            case "Типы компаний": Server.COMPANY_TYPES_TABLE.replaceObject(Client.authData, dto); break;
            case "Специализации": Server.PROFESSION_TABLE.replaceObject(Client.authData, dto); break;
            case "Навыки": Server.SKILLS_TABLE.replaceObject(Client.authData, dto); break;
            case "Языки": Server.LANGUAGES_TABLE.replaceObject(Client.authData, dto); break;
            case "Гражданства": Server.CITIZEN_SHIPS_TABLE.replaceObject(Client.authData, dto); break;
            default: break;
        }
        update();
    }

    public void addObject(CommonObjectDTO dto) throws Exception {
        switch (listName) {
            case "Типы компаний": Server.COMPANY_TYPES_TABLE.addObject(Client.authData, dto); break;
            case "Специализации": Server.PROFESSION_TABLE.addObject(Client.authData, dto); break;
            case "Навыки": Server.SKILLS_TABLE.addObject(Client.authData, dto); break;
            case "Языки": Server.LANGUAGES_TABLE.addObject(Client.authData, dto); break;
            case "Гражданства": Server.CITIZEN_SHIPS_TABLE.addObject(Client.authData, dto); break;
            default: break;
        }
        update();
    }

    private void update() {
        List<CommonObjectDTO> list = null;
        switch (listName) {
            case "Типы компаний": list = Server.COMPANY_TYPES_TABLE.getObjectList(Client.authData); break;
            case "Специализации": list = Server.PROFESSION_TABLE.getObjectList(Client.authData); break;
            case "Навыки": list = Server.SKILLS_TABLE.getObjectList(Client.authData); break;
            case "Языки": list = Server.LANGUAGES_TABLE.getObjectList(Client.authData); break;
            case "Гражданства": list = Server.CITIZEN_SHIPS_TABLE.getObjectList(Client.authData); break;
            default: break;
        }

        controller.clear();
        if (list == null) return;
        for (CommonObjectDTO dto: list) {
            controller.add(new GuiObjectInfo(this, dto));
        }
    }

}
