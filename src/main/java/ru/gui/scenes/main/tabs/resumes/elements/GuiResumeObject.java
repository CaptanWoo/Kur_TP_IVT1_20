package ru.gui.scenes.main.tabs.resumes.elements;

import javafx.stage.Stage;
import ru.db.DataBaseTable;
import ru.dto.resume.ResumePreviewDTO;
import ru.gui.GuiConstructor;
import ru.gui.elements.GuiYListedObject;
import ru.gui.scenes.main.tabs.resumes.scenes.info.SceneResumeInfo;
import ru.gui.windows.WindowController;

public class GuiResumeObject extends GuiYListedObject {

    public GuiResumeObject(int index, ResumePreviewDTO dto) {
        super(index, 5, 5, 800, 235, 5, true);

        root.getChildren().addAll(
                GuiConstructor.createLabel(dto.getDisplayName(), 15, 10, 770, 27),

                GuiConstructor.createButton(e -> new SceneResumeInfo(new WindowController(new Stage()), dto.getResumeId()), "Посмотреть", 670, 10, 120, 25),

                GuiConstructor.createLabel("Зарплата: " + dto.getSalary() + " руб.", 15, 45, 200, 15),
                GuiConstructor.createLabel("Возраст: " + dto.getAge(), 15, 65, 175, 15),
                GuiConstructor.createLabel("Опыт работы: " + dto.getWorkPeriod(), 200, 65, 400, 15),
                GuiConstructor.createLabel("График: " + dto.getSchedule(), 15, 85, 175, 15),
                GuiConstructor.createLabel("Тип трудоустройства: " + dto.getEmploymentType(), 200, 85, 400, 15),

                GuiConstructor.createLabel("Специализации: " + getDisplayData(dto.getProfessions()), 15, 115, 770, 15),
                GuiConstructor.createLabel("Последнее место работы: " + dto.getLastWork(), 15, 135, 770, 15),
                GuiConstructor.createLabel("Основное образование: " + dto.getMainEducation(), 15, 155, 770, 15),

                GuiConstructor.createLabel("Гражданство: " + getDisplayData(dto.getCitizenShips()), 15, 185, 250, 15),
                GuiConstructor.createLabel("Регион: " + dto.getRegion(), 270, 185, 340, 15),
                GuiConstructor.createLabel("Знание языков: " + getDisplayData(dto.getLanguages()), 15, 205, 595, 15),

                GuiConstructor.createLabel("Создано: " + dto.getCreationDate(), 625, 185, 160, 15),
                GuiConstructor.createLabel("Обновлено: " + dto.getUpdateDate(), 625, 205, 160, 15)
        );
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
