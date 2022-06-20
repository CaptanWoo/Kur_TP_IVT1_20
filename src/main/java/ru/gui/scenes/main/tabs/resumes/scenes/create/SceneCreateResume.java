package ru.gui.scenes.main.tabs.resumes.scenes.create;

import javafx.geometry.Pos;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import ru.Client;
import ru.Server;
import ru.db.DataBaseTable;
import ru.dto.objects.AddressDTO;
import ru.dto.resume.ResumeCreateDTO;
import ru.dto.resume.ResumeCreateUserInfoDTO;
import ru.gui.GuiConstructor;
import ru.gui.elements.GuiYMoveableObject;
import ru.gui.scenes.SceneContainer;
import ru.gui.scenes.main.tabs.profile.containers.GuiInfoContainer;
import ru.gui.scenes.main.tabs.profile.containers.object.GuiMenuButtonObject;
import ru.gui.scenes.main.tabs.resumes.containers.object.GuiResumeEducationObject;
import ru.gui.scenes.main.tabs.resumes.containers.object.GuiResumeRecommendationObject;
import ru.gui.scenes.main.tabs.resumes.containers.object.GuiResumeWorkObject;
import ru.gui.scenes.main.tabs.resumes.elements.GuiAddableInfoContainer;
import ru.gui.scenes.main.tabs.resumes.elements.GuiAddableObject;
import ru.gui.windows.WindowController;
import ru.utils.Utils;
import ru.utils.enums.EnumEmploymentType;
import ru.utils.enums.EnumSchedule;
import ru.utils.vectors.DVector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SceneCreateResume extends SceneContainer {

    private final ScrollPane scrollPane;
    private final AnchorPane scrollRoot;

    private final TextField tfDisplayName, tfSalary;
    private final MenuButton mbSchedule, mbEmploymentType;
    private final TextArea taDescription;

    private final TextField tfCountry, tfRegion, tfCity, tfStreet, tfHouse;

    private final GuiInfoContainer<GuiMenuButtonObject> professionsContainer;
    private final GuiInfoContainer<GuiMenuButtonObject> skillsContainer;
    private final GuiAddableInfoContainer<GuiResumeEducationObject> educationsContainer;
    private final GuiAddableInfoContainer<GuiResumeWorkObject> worksContainer;
    private final GuiAddableInfoContainer<GuiResumeRecommendationObject> recommendationsContainer;

    private ResumeCreateUserInfoDTO dto;


    public SceneCreateResume(WindowController windowController) {
        super(windowController, "Создание резюме", new DVector2(495, 840));

        root.getChildren().addAll(
                scrollPane = GuiConstructor.createScrollPane(scrollRoot = new AnchorPane(), 0, 0, 480, 730),
                GuiConstructor.createButton(e -> createResume(), "Создать резюме", 170, 760, 155, 25)
        );

        updateDTO();

        professionsContainer = new GuiInfoContainer<>("Специализации:", new GuiMenuButtonObject(0, 0, 359, 29,  getProfessions()), 50, 890, 380, 200, 251, 2, 25, 2, true, true);
        skillsContainer = new GuiInfoContainer<>("Навыки:", new GuiMenuButtonObject(0, 0, 359, 29,  getSkills()), 50, 1140, 380, 240, 251, 2, 25, 2, true, true);
        educationsContainer = new GuiAddableInfoContainer<>("Образование:", new GuiResumeEducationObject(0, 0), getGuiEducationObjects(), 5, 1430, 470, 488, 355, 5, 25, 5, true, false);
        worksContainer = new GuiAddableInfoContainer<>("Опыт работы:", new GuiResumeWorkObject(0, 0), getGuiWorkObjects(), 5, 1935, 470, 488, 355, 5, 25, 5, true, false);
        recommendationsContainer = new GuiAddableInfoContainer<>("Рекомендации:", new GuiResumeRecommendationObject(0, 0), getGuiRecommendationObjects(), 5, 2440, 470, 488, 355, 5, 25, 5, true, false);


        scrollRoot.getChildren().addAll(
                GuiConstructor.createRectangle("#FFFFFF", 0, 0, 480, 2950),
                GuiConstructor.createLabel("Название:", 5, 10, 65, 15, Pos.CENTER),
                tfDisplayName = GuiConstructor.createTextField("", 70, 5, 405, 25),
                GuiConstructor.createLabel("График работы:", 5, 40, 95, 15, Pos.CENTER),
                mbSchedule = GuiConstructor.createMenuButton(e -> {}, "", EnumSchedule.getEnumDisplayNames(), 100, 35, 170, 25),
                GuiConstructor.createLabel("Тип занятости:", 5, 70, 95, 15, Pos.CENTER),
                mbEmploymentType = GuiConstructor.createMenuButton(e -> {}, "", EnumEmploymentType.getEnumDisplayNames(), 100, 65, 170, 25),
                GuiConstructor.createLabel("Зарплата:", 270, 40, 65, 15, Pos.CENTER),
                tfSalary = GuiConstructor.createTextField("", 335, 35, 110, 25),
                GuiConstructor.createLabel("руб.", 445, 40, 30, 15, Pos.CENTER),

                GuiConstructor.createLabel("Адрес:", 5, 95, 470, 22, Pos.CENTER),
                GuiConstructor.createButton(e -> transferAddressFromProfile(), "Перенести из профиля", 315, 95, 160, 25),

                GuiConstructor.createLabel("Страна:", 5, 130, 65, 15, Pos.CENTER),
                tfCountry = GuiConstructor.createTextField("", 70, 125, 405, 25),
                GuiConstructor.createLabel("Регион:", 5, 160, 65, 15, Pos.CENTER),
                tfRegion = GuiConstructor.createTextField("", 70, 155, 405, 25),
                GuiConstructor.createLabel("Город:", 5, 190, 65, 15, Pos.CENTER),
                tfCity = GuiConstructor.createTextField("", 70, 185, 405, 25),
                GuiConstructor.createLabel("Улица:", 5, 220, 65, 15, Pos.CENTER),
                tfStreet = GuiConstructor.createTextField("", 70, 215, 295, 25),
                GuiConstructor.createLabel("Дом:", 365, 220, 35, 15, Pos.CENTER),
                tfHouse = GuiConstructor.createTextField("", 400, 215, 75, 25),

                GuiConstructor.createLabel("Описание:", 5, 250, 470, 22, Pos.CENTER),
                taDescription = GuiConstructor.createTextArea("", true, 5, 275, 470, 600),

                professionsContainer.getRoot(),
                skillsContainer.getRoot(),
                educationsContainer.getRoot(),
                worksContainer.getRoot(),
                recommendationsContainer.getRoot()

        );


        this.windowController.setScene(this);
        this.windowController.show();
    }

    private void transferAddressFromProfile() {
        AddressDTO dto = Server.USERS_TABLE.getUserAddressDTO(Client.authData);
        if (dto != null) {
            tfCountry.setText(dto.getCountry());
            tfRegion.setText(dto.getRegion());
            tfCity.setText(dto.getCity());
            tfStreet.setText(dto.getStreet());
            tfHouse.setText(dto.getHouse());
        }
    }

    private List<String> getProfessions() {
        if (dto == null) return null;
        if (dto.getProfessions().equals("null")) return null;
        return Arrays.asList(dto.getProfessions().split(DataBaseTable.objectArraySeparator));
    }

    private String getChosenProfessions() {
        StringBuilder sb = new StringBuilder();
        for (GuiMenuButtonObject object: professionsContainer.getObjects()) {
            sb.append(object.save()).append(DataBaseTable.objectArraySeparator);
        }
        if (sb.length() > 0) sb.setLength(sb.length()-1);
        return sb.toString();
    }

    private List<String> getSkills() {
        if (dto == null) return null;
        if (dto.getSkills().equals("null")) return null;
        return Arrays.asList(dto.getSkills().split(DataBaseTable.objectArraySeparator));
    }

    private String getChosenSkills() {
        StringBuilder sb = new StringBuilder();
        for (GuiMenuButtonObject object: skillsContainer.getObjects()) {
            sb.append(object.save()).append(DataBaseTable.objectArraySeparator);
        }
        if (sb.length() > 0) sb.setLength(sb.length()-1);
        return sb.toString();
    }

    private Collection<String> getEducations() {
        if (dto == null) return null;
        return Arrays.asList(dto.getEducations().split(DataBaseTable.objectArraySeparator));
    }

    private String getChosenEducations() {
        StringBuilder sb = new StringBuilder();
        for (GuiResumeEducationObject object: educationsContainer.getObjects()) {
            sb.append(object.save()).append(DataBaseTable.objectArraySeparator);
        }
        if (sb.length() > 0) sb.setLength(sb.length()-1);
        return sb.toString();
    }

    private Collection<String> getWorks() {
        if (dto == null) return null;
        return Arrays.asList(dto.getWorks().split(DataBaseTable.objectArraySeparator));
    }

    private String getChosenWorks() {
        StringBuilder sb = new StringBuilder();
        for (GuiResumeWorkObject object: worksContainer.getObjects()) {
            sb.append(object.save()).append(DataBaseTable.objectArraySeparator);
        }
        if (sb.length() > 0) sb.setLength(sb.length()-1);
        return sb.toString();
    }

    private Collection<String> getRecommendations() {
        if (dto == null) return null;
        return Arrays.asList(dto.getRecommendations().split(DataBaseTable.objectArraySeparator));
    }

    private String getChosenRecommendations() {
        StringBuilder sb = new StringBuilder();
        for (GuiResumeRecommendationObject object: recommendationsContainer.getObjects()) {
            sb.append(object.save()).append(DataBaseTable.objectArraySeparator);
        }
        if (sb.length() > 0) sb.setLength(sb.length()-1);
        return sb.toString();
    }

    private void addResumeEducation(String data) {
        GuiResumeEducationObject g = new GuiResumeEducationObject(0, 0, data);
        educationsContainer.addObject(new GuiYMoveableObject<>(g, educationsContainer.getController(), 0, 5, 5, g.getWidth(), g.getHeight(), 5, 355, 5, 25, 5, true, false));
    }

    private void addResumeWork(String data) {
        GuiResumeWorkObject g = new GuiResumeWorkObject(0, 0, data);
        worksContainer.addObject(new GuiYMoveableObject<>(g, worksContainer.getController(), 0, 5, 5, g.getWidth(), g.getHeight(), 5, 355, 5, 25, 5, true, false));
    }

    private void addResumeRecommendation(String data) {
        GuiResumeRecommendationObject g = new GuiResumeRecommendationObject(0, 0, data);
        recommendationsContainer.addObject(new GuiYMoveableObject<>(g, recommendationsContainer.getController(), 0, 5, 5, g.getWidth(), g.getHeight(), 5, 355, 5, 25, 5, true, false));
    }

    private List<GuiAddableObject<GuiResumeEducationObject>> getGuiEducationObjects() {
        List<GuiAddableObject<GuiResumeEducationObject>> list = new ArrayList<>();

        int index = 0;
        Collection<String> educ = getEducations();
        if (educ == null) return list;
        for (String d: educ) {
            GuiResumeEducationObject g = new GuiResumeEducationObject(0, 0);
            g.load(d);
            GuiAddableObject<GuiResumeEducationObject> o = new GuiAddableObject<>(g, this::addResumeEducation, index, 5, 5, g.getWidth(), g.getHeight());
            index++;
            list.add(o);
        }
        return list;
    }

    private List<GuiAddableObject<GuiResumeWorkObject>> getGuiWorkObjects() {
        List<GuiAddableObject<GuiResumeWorkObject>> list = new ArrayList<>();

        int index = 0;
        Collection<String> educ = getWorks();
        if (educ == null) return list;
        for (String d: educ) {
            GuiResumeWorkObject g = new GuiResumeWorkObject(0, 0);
            g.load(d);
            GuiAddableObject<GuiResumeWorkObject> o = new GuiAddableObject<>(g, this::addResumeWork, index, 5, 5, g.getWidth(), g.getHeight());
            index++;
            list.add(o);
        }
        return list;
    }

    private List<GuiAddableObject<GuiResumeRecommendationObject>> getGuiRecommendationObjects() {
        List<GuiAddableObject<GuiResumeRecommendationObject>> list = new ArrayList<>();

        int index = 0;
        Collection<String> educ = getRecommendations();
        if (educ == null) return list;
        for (String d: educ) {
            GuiResumeRecommendationObject g = new GuiResumeRecommendationObject(0, 0);
            g.load(d);
            GuiAddableObject<GuiResumeRecommendationObject> o = new GuiAddableObject<>(g, this::addResumeRecommendation, index,5, 5, g.getWidth(), g.getHeight());
            index++;
            list.add(o);
        }
        return list;
    }

    private void updateDTO() {
        dto = Server.USERS_TABLE.getResumeCreateUserInfoDTO(Client.authData);
    }

    private void createResume() {
        try {
            ResumeCreateDTO dto = new ResumeCreateDTO(
                    tfDisplayName.getText(),
                    getChosenProfessions(),
                    Utils.getFixedDescription(taDescription.getText()),
                    Integer.parseInt("0" + tfSalary.getText()),
                    getChosenEducations(),
                    getChosenWorks(),
                    getChosenSkills(),
                    getChosenRecommendations(),
                    EnumEmploymentType.getUnlocalizedNameByDisplayName(mbEmploymentType.getText()),
                    EnumSchedule.getUnlocalizedNameByDisplayName(mbSchedule.getText())
            );

            Server.RESUME_TABLE.createResume(Client.authData, dto);
            windowController.close();
        } catch (Exception e) {
            System.err.println("Произошла ошибка при создании резюме");
            e.printStackTrace();
        }
    }
}
