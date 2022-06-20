package ru.gui.scenes.main.tabs.vacancies.elements;

import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import ru.db.DataBaseTable;
import ru.dto.vacancy.VacancyPreviewDTO;
import ru.gui.GuiConstructor;
import ru.gui.elements.GuiYListedObject;
import ru.gui.scenes.main.tabs.vacancies.scenes.info.SceneVacancyInfo;
import ru.gui.windows.WindowController;
import ru.utils.Utils;
import ru.utils.enums.EnumEmploymentType;
import ru.utils.enums.EnumSchedule;

public class GuiVacancyObject extends GuiYListedObject {

    public GuiVacancyObject(int index, VacancyPreviewDTO dto) {
        super(index, 5, 5, 800, 320, 5, true);

        TextArea taShortDescription;
        root.getChildren().addAll(
                GuiConstructor.createLabel(dto.getDisplayName(), 15, 10, 635, 27),

                GuiConstructor.createButton(e -> new SceneVacancyInfo(new WindowController(new Stage()), dto.getId()), "Посмотреть", 670, 10, 120, 25),

                GuiConstructor.createLabel("Зарплата: " + dto.getSalary(), 15, 45, 270, 15),
                GuiConstructor.createLabel("Опыт работы: " + dto.getWorkPeriod(), 290, 45, 495, 15),
                GuiConstructor.createLabel("График: " + EnumSchedule.getEnum(dto.getSchedule()).getDisplayName(), 15, 65, 270, 15),
                GuiConstructor.createLabel("Тип трудоустройства: " + EnumEmploymentType.getEnum(dto.getEmploymentType()).getDisplayName(), 290, 65, 495, 15),

                GuiConstructor.createLabel("Специализации: " + getDisplayData(dto.getProfessions()), 15, 95, 770, 15),
                GuiConstructor.createLabel("Компания: " + dto.getCompany(), 15, 115, 595, 15),
                GuiConstructor.createLabel("Регион: " + dto.getRegion(), 15, 135, 595, 15),
                GuiConstructor.createLabel("Гражданство: " + getDisplayData(dto.getCitizenShips()), 15, 155, 280, 15),
                GuiConstructor.createLabel("Знание языков: " + getDisplayData(dto.getLanguages()), 290, 155, 320, 15),

                GuiConstructor.createLabel("Создано: " + dto.getCreationDate(), 625, 135, 160, 15),
                GuiConstructor.createLabel("Обновлено: " + dto.getUpdateDate(), 625, 155, 160, 15),

                taShortDescription = GuiConstructor.createTextArea("", true, 10, 180, 780, 130)
        );
        taShortDescription.setText("");
        for (String line: Utils.getNotFixedDescription(dto.getShortDescription())) {
            taShortDescription.appendText(line);
        }
    }

    public String getDisplayData(String input) {
        StringBuilder sb = new StringBuilder();

        for (String d : input.split(DataBaseTable.objectArraySeparator)) {
            sb.append(d).append(", ");
        }

        if (sb.length() > 2) sb.setLength(sb.length() - 2);
        return sb.toString();
    }

}
