package ru.gui.scenes.main.tabs.profile.elements;

import ru.Client;
import ru.Server;
import ru.db.DataBaseTable;
import ru.dto.user.UserProfileUpdateDTO;
import ru.gui.elements.GuiObject;
import ru.gui.scenes.main.tabs.profile.containers.GuiInfoContainer;
import ru.gui.scenes.main.tabs.profile.containers.GuiMainInfoContainer;
import ru.gui.scenes.main.tabs.profile.containers.object.GuiEducationObject;
import ru.gui.scenes.main.tabs.profile.containers.object.GuiMenuButtonObject;
import ru.gui.scenes.main.tabs.profile.containers.object.GuiRecommendationObject;
import ru.gui.scenes.main.tabs.profile.containers.object.GuiWorkObject;
import ru.utils.enums.EnumSex;
import ru.utils.objects.Address;

import java.util.HashSet;
import java.util.Set;

public class GuiProfileInfo extends GuiObject {

    private final GuiMainInfoContainer guiMainInfoContainer;
    private final GuiInfoContainer<GuiMenuButtonObject> professionsContainer, skillsContainer, citizenShipsContainer, languagesContainer;
    private final GuiInfoContainer<GuiEducationObject> educationContainer;
    private final GuiInfoContainer<GuiWorkObject> workContainer;
    private final GuiInfoContainer<GuiRecommendationObject> recommendationContainer;

    public GuiProfileInfo(double x, double y) {
        super(x, y, 900, 1740);

        guiMainInfoContainer = new GuiMainInfoContainer(15, 15);
        professionsContainer = new GuiInfoContainer<>("Специализации:", new GuiMenuButtonObject(0, 0, 359, 29, Server.PROFESSION_TABLE.getHashNames()), 15, 235, 380, 488, 251, 2, 25, 2, true, true);
        skillsContainer = new GuiInfoContainer<>("Навыки:", new GuiMenuButtonObject(0, 0, 359, 29,  Server.SKILLS_TABLE.getHashNames()), 15, 725, 380, 488, 251, 2, 25, 2, true, true);
        languagesContainer = new GuiInfoContainer<>("Языки:", new GuiMenuButtonObject(0, 0, 359, 29,  Server.LANGUAGES_TABLE.getHashNames()),15, 1215, 380, 233, 251, 2, 25, 2, true, true);
        citizenShipsContainer = new GuiInfoContainer<>("Гражданство:", new GuiMenuButtonObject(0, 0, 359, 29,  Server.CITIZEN_SHIPS_TABLE.getHashNames()), 15, 1470, 380, 233, 251, 2, 25, 2, true, true);
        educationContainer = new GuiInfoContainer<>("Образование:", new GuiEducationObject(0, 0, 445, 180), 405, 235, 470, 488, 325, 5, 25, 5, true, true);
        workContainer = new GuiInfoContainer<>("Опыт работы:", new GuiWorkObject(0, 0, 445, 195), 405,725, 470, 488, 325, 5, 25, 5, true, true);
        recommendationContainer = new GuiInfoContainer<>("Рекомендации:", new GuiRecommendationObject(0, 0, 445, 210),405,1215, 470, 488, 325, 5,25, 5,  true, true);

        root.getChildren().addAll(
                guiMainInfoContainer.getRoot(),
                professionsContainer.getRoot(),
                skillsContainer.getRoot(),
                languagesContainer.getRoot(),
                citizenShipsContainer.getRoot(),
                educationContainer.getRoot(),
                workContainer.getRoot(),
                recommendationContainer.getRoot()
        );

    }

    /**
     * Метод сохранения новых данных о пользователе в таблицу пользователей на сервере.
     * Для этого создается DTO объект, в который записываются данные из полей ввода в GUI.
     * Затем этот DTO объект отправляется на сервер.
     *
     * После обновления данных на сервере, пользователь получает новый ключ авторизации.
     */
    public void save() {

        String email = guiMainInfoContainer.getTfEmail().getText();
        String phoneNumber = guiMainInfoContainer.getTfPhoneNumber().getText();
        String lastName = guiMainInfoContainer.getTfLastName().getText();
        String firstName = guiMainInfoContainer.getTfFirstName().getText();
        String middleName = guiMainInfoContainer.getTfMiddleName().getText();
        String birthDate = guiMainInfoContainer.getBirthDate().getLocalDate().toString();
        String sex = guiMainInfoContainer.getCbMale().isSelected() ? EnumSex.MALE.getUnlocalizedName() : EnumSex.FEMALE.getUnlocalizedName();
        String address = new Address(
                guiMainInfoContainer.getTfCountry().getText(),
                guiMainInfoContainer.getTfRegion().getText(),
                guiMainInfoContainer.getTfCity().getText(),
                guiMainInfoContainer.getTfStreet().getText(),
                guiMainInfoContainer.getTfHouse().getText()
        ).save();

        StringBuilder sb = new StringBuilder();

        for(GuiMenuButtonObject object: professionsContainer.getObjects()) {
            sb.append(object.save()).append(DataBaseTable.objectArraySeparator);
        }
        String professions = sb.toString();

        sb.setLength(0);
        for(GuiMenuButtonObject object: skillsContainer.getObjects()) {
            sb.append(object.save()).append(DataBaseTable.objectArraySeparator);
        }
        String skills = sb.toString();

        sb.setLength(0);
        for(GuiMenuButtonObject object: languagesContainer.getObjects()) {
            sb.append(object.save()).append(DataBaseTable.objectArraySeparator);
        }
        String languages = sb.toString();

        sb.setLength(0);
        for(GuiMenuButtonObject object: citizenShipsContainer.getObjects()) {
            sb.append(object.save()).append(DataBaseTable.objectArraySeparator);
        }
        String citizenShips = sb.toString();

        sb.setLength(0);
        for(GuiEducationObject object: educationContainer.getObjects()) {
            if (sb.length() > 0) sb.append(DataBaseTable.objectArraySeparator);
            sb.append(object.save());
        }
        String educations = sb.toString();

        sb.setLength(0);
        for(GuiWorkObject object: workContainer.getObjects()) {
            if (sb.length() > 0) sb.append(DataBaseTable.objectArraySeparator);
            sb.append(object.save());
        }
        String works = sb.toString();

        sb.setLength(0);
        for(GuiRecommendationObject object: recommendationContainer.getObjects()) {
            if (sb.length() > 0) sb.append(DataBaseTable.objectArraySeparator);
            sb.append(object.save());
        }
        String recommendations = sb.toString();

        Client.authData = Server.USERS_TABLE.updateUser(Client.authData, new UserProfileUpdateDTO(
                email, phoneNumber, lastName, firstName, middleName, birthDate, sex, address,
                educations, works, recommendations, professions, skills, languages, citizenShips
        ));

    }

    /**
     * Метод загрузки информации о профиле пользователя.
     * На сервер отправляется ключ авторизации, а в ответ клиент получает DTO объект с данными профиля.
     * @throws Exception ошибка, если не удалось загрузить данные о профиле пользователя.
     */
    public void load() throws Exception {
        UserProfileUpdateDTO dto = Server.USERS_TABLE.getUserProfileUpdateDTO(Client.authData);
        if (dto == null) throw new Exception("Не удалось загрузить данные пользователя.");

        guiMainInfoContainer.load(dto);

        String[] data = dto.getProfessions().split(DataBaseTable.objectArraySeparator);
        professionsContainer.prepareObjects(data.length);
        for (int i = 0; i < data.length; i++) professionsContainer.getObject(i).load(data[i]);

        data = dto.getSkills().split(DataBaseTable.objectArraySeparator);
        skillsContainer.prepareObjects(data.length);
        for (int i = 0; i < data.length; i++) skillsContainer.getObject(i).load(data[i]);

        data = dto.getLanguages().split(DataBaseTable.objectArraySeparator);
        languagesContainer.prepareObjects(data.length);
        for (int i = 0; i < data.length; i++) languagesContainer.getObject(i).load(data[i]);

        data = dto.getCitizenShips().split(DataBaseTable.objectArraySeparator);
        citizenShipsContainer.prepareObjects(data.length);
        for (int i = 0; i < data.length; i++) citizenShipsContainer.getObject(i).load(data[i]);

        data = dto.getEducations().split(DataBaseTable.objectArraySeparator);
        educationContainer.prepareObjects(data.length);
        for (int i = 0; i < data.length; i++) educationContainer.getObject(i).load(data[i]);

        data = dto.getWorks().split(DataBaseTable.objectArraySeparator);
        workContainer.prepareObjects(data.length);
        for (int i = 0; i < data.length; i++) workContainer.getObject(i).load(data[i]);

        data = dto.getRecommendations().split(DataBaseTable.objectArraySeparator);
        recommendationContainer.prepareObjects(data.length);
        for (int i = 0; i < data.length; i++) recommendationContainer.getObject(i).load(data[i]);
    }

}
