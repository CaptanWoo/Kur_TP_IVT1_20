package ru.gui.scenes.main.tabs.companies.elements;

import javafx.stage.Stage;
import ru.dto.company.CompanyPreviewDTO;
import ru.gui.GuiConstructor;
import ru.gui.elements.GuiYListedObject;
import ru.gui.scenes.main.tabs.companies.scenes.info.SceneCompanyInfo;
import ru.gui.windows.WindowController;

public class GuiCompanyObject extends GuiYListedObject {

    public GuiCompanyObject(int index, CompanyPreviewDTO dto) {
        super(index, 5, 5, 800, 110, 5, true);

        root.getChildren().addAll(
                GuiConstructor.createLabel(dto.getDisplayName(), 15, 10, 600, 27),

                GuiConstructor.createButton(e -> new SceneCompanyInfo(new WindowController(new Stage()), dto.getCompanyId()), "Посмотреть", 630, 10, 160, 25),

                GuiConstructor.createLabel("Тип: " + dto.getType(), 15, 45, 90, 15),
                GuiConstructor.createLabel("Специализации: " + dto.getProfessions(), 110, 45, 505, 15),
                GuiConstructor.createLabel("Вакансии: " + dto.getVacancies(), 15, 65, 90, 15),
                GuiConstructor.createLabel("Сайт: " + dto.getWebsite(), 110, 65, 505, 15),
                GuiConstructor.createLabel("Регион: " + dto.getAddress(), 15, 85, 600, 15),

                GuiConstructor.createLabel("Создано: " + dto.getCreationDate(), 630, 65, 160, 15),
                GuiConstructor.createLabel("Обновлено: " + dto.getUpdateDate(), 630, 85, 160, 15)
        );
    }
}
