package ru.gui.scenes.main.tabs.resumes.scenes.info;

import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import ru.Client;
import ru.Server;
import ru.db.DataBaseTable;
import ru.dto.resume.ResumeInfoDTO;
import ru.gui.GuiConstructor;
import ru.gui.scenes.SceneContainer;
import ru.gui.scenes.main.tabs.resumes.containers.object.GuiResumeEducationObject;
import ru.gui.scenes.main.tabs.resumes.containers.object.GuiResumeRecommendationObject;
import ru.gui.scenes.main.tabs.resumes.containers.object.GuiResumeWorkObject;
import ru.gui.scenes.main.tabs.resumes.scenes.info.objects.GuiInfoListedObject;
import ru.gui.scenes.main.tabs.resumes.scenes.info.objects.GuiLabelObject;
import ru.gui.windows.WindowController;
import ru.utils.Utils;
import ru.utils.vectors.DVector2;

import java.util.ArrayList;
import java.util.List;

public class SceneResumeInfo extends SceneContainer {

    private final ResumeInfoDTO dto;

    public SceneResumeInfo(WindowController windowController, long resumeId) {
        super(windowController, "Просмотр резюме", new DVector2(515, 940));

        AnchorPane scrollRoot;
        root.getChildren().addAll(
                GuiConstructor.createScrollPane(scrollRoot = new AnchorPane(), 0, 0, 500, 900)
        );

        dto = getResumeInfoDTO(resumeId);

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
        GuiInfoContainer<GuiInfoListedObject<GuiResumeEducationObject>> educationsContainer = new GuiInfoContainer<>(
                "Образование:", getGuiEducations(), 15, 1410, 470, 488
        );
        GuiInfoContainer<GuiInfoListedObject<GuiResumeWorkObject>> worksContainer = new GuiInfoContainer<>(
                "Опыт работы: " + dto.getWorkPeriod(), getGuiWorks(), 15, 1915, 470, 488
        );
        GuiInfoContainer<GuiInfoListedObject<GuiResumeRecommendationObject>> recommendationsObjects = new GuiInfoContainer<>(
                "Рекомендации:", getGuiRecommendations(), 15, 2420, 470, 488
        );

        TextArea taDescription;
        scrollRoot.getChildren().addAll(
                GuiConstructor.createRectangle("#FFFFFF", 0, 0, 500, 2923),
                GuiConstructor.createLabel(dto.getDisplayName(), 15, 10, 470, 19),
                GuiConstructor.createLabel("Зарплата: " + dto.getSalary(), 15, 35, 230, 15),
                GuiConstructor.createLabel("График работы: " + dto.getSchedule(), 255, 35, 230, 15),
                GuiConstructor.createLabel("Опыт работы: " + dto.getWorkPeriod(), 15, 55, 230, 15),
                GuiConstructor.createLabel("Тип занятости: " + dto.getEmploymentType(), 255, 55, 230, 15),
                GuiConstructor.createLabel("ФИО: " + dto.getFio(), 15, 80, 470, 15),
                GuiConstructor.createLabel("Возраст: " + dto.getAge(), 15, 100, 230, 15),
                GuiConstructor.createLabel("Пол: " + dto.getSex(), 255, 100, 230, 15),
                GuiConstructor.createLabel("Email: " + dto.getEmail(), 15, 120, 230, 15),
                GuiConstructor.createLabel("Телефон: " + dto.getPhoneNumber(), 255, 120, 230, 15),
                GuiConstructor.createLabel("Адрес: " + dto.getAddress(), 15, 145, 470, 15),

                taDescription = GuiConstructor.createTextArea("", true, false, 15, 170, 470, 600),

                professionsContainer.getRoot(),
                skillsContainer.getRoot(),
                citizenShipsContainer.getRoot(),
                languagesContainer.getRoot(),
                educationsContainer.getRoot(),
                worksContainer.getRoot(),
                recommendationsObjects.getRoot()
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

        for (String s: data.split(DataBaseTable.objectArraySeparator)) {
            GuiLabelObject g = new GuiLabelObject(s,0, 0, 205, 20);
            list.add(new GuiInfoListedObject<>(g, 0, 5, 5, g.getWidth(), g.getHeight()));
        }
        return list;
    }

    public List<GuiInfoListedObject<GuiResumeEducationObject>> getGuiEducations() {
        List<GuiInfoListedObject<GuiResumeEducationObject>> list = new ArrayList<>();

        for (String s: dto.getEducations().split(DataBaseTable.objectArraySeparator)) {
            GuiResumeEducationObject g = new GuiResumeEducationObject(0, 0, s);
            list.add(new GuiInfoListedObject<>(g, 0, 5, 5, g.getWidth(), g.getHeight()));
        }
        return list;
    }
    public List<GuiInfoListedObject<GuiResumeWorkObject>> getGuiWorks() {
        List<GuiInfoListedObject<GuiResumeWorkObject>> list = new ArrayList<>();

        for (String s: dto.getWorks().split(DataBaseTable.objectArraySeparator)) {
            GuiResumeWorkObject g = new GuiResumeWorkObject(0, 0, s);
            list.add(new GuiInfoListedObject<>(g, 0, 5, 5, g.getWidth(), g.getHeight()));
        }
        return list;
    }
    public List<GuiInfoListedObject<GuiResumeRecommendationObject>> getGuiRecommendations() {
        List<GuiInfoListedObject<GuiResumeRecommendationObject>> list = new ArrayList<>();

        for (String s: dto.getRecommendations().split(DataBaseTable.objectArraySeparator)) {
            GuiResumeRecommendationObject g = new GuiResumeRecommendationObject(0, 0, s);
            list.add(new GuiInfoListedObject<>(g, 0, 5, 5, g.getWidth(), g.getHeight()));
        }
        return list;
    }

    private ResumeInfoDTO getResumeInfoDTO(long resumeId) {
        return Server.RESUME_TABLE.getResumeInfo(Client.authData, resumeId);
    }
}
