package ru.gui.scenes.main.tabs.vacancies.scenes.info;

import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import ru.Client;
import ru.Server;
import ru.dto.vacancy.VacancyInfoDTO;
import ru.gui.GuiConstructor;
import ru.gui.scenes.SceneContainer;
import ru.gui.scenes.main.tabs.resumes.scenes.info.GuiInfoContainer;
import ru.gui.scenes.main.tabs.resumes.scenes.info.objects.GuiInfoListedObject;
import ru.gui.scenes.main.tabs.resumes.scenes.info.objects.GuiLabelObject;
import ru.gui.windows.WindowController;
import ru.utils.Utils;
import ru.utils.enums.EnumEmploymentType;
import ru.utils.enums.EnumSchedule;
import ru.utils.vectors.DVector2;

import java.util.ArrayList;
import java.util.List;

public class SceneVacancyInfo extends SceneContainer {

    private final VacancyInfoDTO dto;

    public SceneVacancyInfo(WindowController windowController, long vacancyId) {
        super(windowController, "Просмотр резюме", new DVector2(515, 940));

        AnchorPane scrollRoot;
        root.getChildren().addAll(
                GuiConstructor.createScrollPane(scrollRoot = new AnchorPane(), 0, 0, 500, 900)
        );

        dto = getVacancyInfoDTO(vacancyId);

        GuiInfoContainer<GuiInfoListedObject<GuiLabelObject>> professionsContainer = new GuiInfoContainer<>(
                "Специализации:", getGuiLabelObject(dto.getProfessions()), 15, 787, 230, 348
        );
        GuiInfoContainer<GuiInfoListedObject<GuiLabelObject>> skillsContainer = new GuiInfoContainer<>(
                "Навыки:", getGuiLabelObject(dto.getSkills()), 255, 787, 230, 348
        );
        GuiInfoContainer<GuiInfoListedObject<GuiLabelObject>> citizenShipsContainer = new GuiInfoContainer<>(
                "Гражданство:", getGuiLabelObject(dto.getCitizenShips()), 15, 1145, 230, 248
        );
        GuiInfoContainer<GuiInfoListedObject<GuiLabelObject>> languagesContainer = new GuiInfoContainer<>(
                "Языки:", getGuiLabelObject(dto.getLanguages()), 255, 1145, 230, 248
        );

        TextArea taDescription;
        scrollRoot.getChildren().addAll(
                GuiConstructor.createRectangle("#FFFFFF", 0, 0, 500, 1450),
                GuiConstructor.createLabel(dto.getDisplayName(), 15, 10, 470, 19),
                GuiConstructor.createLabel("Компания: " + dto.getCompany(), 15, 32, 470, 15),
                GuiConstructor.createLabel("Зарплата: " + dto.getSalary(), 15, 55, 230, 15),
                GuiConstructor.createLabel("График работы: " + EnumSchedule.getEnum(dto.getSchedule()).getDisplayName(), 255, 55, 230, 15),
                GuiConstructor.createLabel("Опыт работы: " + dto.getWorkPeriod(), 15, 75, 230, 15),
                GuiConstructor.createLabel("Тип занятости: " + EnumEmploymentType.getEnum(dto.getEmploymentType()).getDisplayName(), 255, 75, 230, 15),
                GuiConstructor.createLabel("Контактная информация", 15, 98, 470, 25, Pos.CENTER),
                GuiConstructor.createLabel("ФИО: " + dto.getContactFio(), 15, 125, 470, 15),
                GuiConstructor.createLabel("Email: " + dto.getContactEmail(), 15, 145, 230, 15),
                GuiConstructor.createLabel("Телефон: " + dto.getContactPhoneNumber(), 255, 145, 230, 15),
                GuiConstructor.createLabel("Описание: " + dto.getContactDescription(), 15, 165, 470, 15),
                GuiConstructor.createLabel("Адрес: " + dto.getAddress(), 15, 195, 470, 15),

                taDescription = GuiConstructor.createTextArea("", true, false, 15, 215, 470, 600),

                professionsContainer.getRoot(),
                skillsContainer.getRoot(),
                citizenShipsContainer.getRoot(),
                languagesContainer.getRoot()
        );

        taDescription.setText("");
        for (String line: Utils.getNotFixedDescription(dto.getDescription())) {
            taDescription.appendText(line);
        }

        windowController.setScene(this);
        windowController.show();
    }

    public List<GuiInfoListedObject<GuiLabelObject>> getGuiLabelObject(String data) {
        List<GuiInfoListedObject<GuiLabelObject>> list = new ArrayList<>();

        for (String s: data.split(", ")) {
            GuiLabelObject g = new GuiLabelObject(s,0, 0, 205, 20);
            list.add(new GuiInfoListedObject<>(g, 0, 5, 5, g.getWidth(), g.getHeight()));
        }
        return list;
    }

    private VacancyInfoDTO getVacancyInfoDTO(long vacancyId) {
        return Server.VACANCIES_TABLE.getVacancyInfo(Client.authData, vacancyId);
    }
}
