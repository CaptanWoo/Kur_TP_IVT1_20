package ru.gui.scenes.main.tabs.vacancies.scenes.create;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import ru.Client;
import ru.Server;
import ru.db.DataBaseTable;
import ru.dto.objects.AddressDTO;
import ru.dto.objects.ContactInfoDTO;
import ru.dto.resume.ResumeCreateUserInfoDTO;
import ru.dto.user.UserCompaniesDTO;
import ru.dto.vacancy.VacancyCreateDTO;
import ru.gui.GuiConstructor;
import ru.gui.scenes.SceneContainer;
import ru.gui.scenes.main.tabs.profile.containers.GuiInfoContainer;
import ru.gui.scenes.main.tabs.profile.containers.object.GuiMenuButtonObject;
import ru.gui.windows.WindowController;
import ru.objects.AuthData;
import ru.utils.Utils;
import ru.utils.enums.EnumEmploymentType;
import ru.utils.enums.EnumSchedule;
import ru.utils.enums.EnumStatus;
import ru.utils.objects.Address;
import ru.utils.objects.ContactInfo;
import ru.utils.vectors.DVector2;

import java.util.*;

public class SceneCreateVacancy extends SceneContainer {

    private final TextField tfDisplayName, tfMinSalary, tfMaxSalary, tfMinWorkPeriod, tfMaxWorkPeriod;
    private final MenuButton mbSchedule, mbCompany, mbEmploymentType;
    private final TextField tfContactFio, tfContactEmail, tfContactPhoneNumber, tfContactDescription;
    private final TextArea taDescription, taShortDescription;

    private final Button btnTransferAddress, btnTransferContactInfo;

    private final TextField tfCountry, tfRegion, tfCity, tfStreet, tfHouse;

    private final GuiInfoContainer<GuiMenuButtonObject> professionsContainer, skillsContainer, citizenShipsContainer, languagesContainer;

    private final Map<String, Long> companyIdByNameMap = new HashMap<>();

    public SceneCreateVacancy(WindowController windowController) {
        super(windowController, "Создание вакансии", new DVector2(495, 840));

        AnchorPane scrollRoot;
        root.getChildren().addAll(
                GuiConstructor.createScrollPane(scrollRoot = new AnchorPane(), 0, 0, 480, 730),
                GuiConstructor.createButton(e -> createVacancy(), "Создать вакансию", 170, 760, 155, 25)
        );

        updateCompaniesInfo();

        professionsContainer = new GuiInfoContainer<>("Специализации:", new GuiMenuButtonObject(0, 0, 359, 29, Server.PROFESSION_TABLE.getHashNames()), 50, 1335, 380, 488, 251, 2, 25, 2, true, true);
        skillsContainer = new GuiInfoContainer<>("Навыки:", new GuiMenuButtonObject(0, 0, 359, 29,  Server.SKILLS_TABLE.getHashNames()), 50, 1825, 380, 488, 251, 2, 25, 2, true, true);
        languagesContainer = new GuiInfoContainer<>("Языки:", new GuiMenuButtonObject(0, 0, 359, 29,  Server.LANGUAGES_TABLE.getHashNames()),50, 2315, 380, 233, 251, 2, 25, 2, true, true);
        citizenShipsContainer = new GuiInfoContainer<>("Гражданство:", new GuiMenuButtonObject(0, 0, 359, 29,  Server.CITIZEN_SHIPS_TABLE.getHashNames()), 50, 2570, 380, 233, 251, 2, 25, 2, true, true);


        scrollRoot.getChildren().addAll(
                GuiConstructor.createRectangle("#FFFFFF", 0, 0, 480, 2950),
                GuiConstructor.createLabel("Название:", 5, 10, 65, 15, Pos.CENTER),
                tfDisplayName = GuiConstructor.createTextField("", 70, 5, 405, 25),
                GuiConstructor.createLabel("График работы:", 5, 40, 95, 15, Pos.CENTER),
                mbSchedule = GuiConstructor.createMenuButton(e -> {}, "", EnumSchedule.getEnumDisplayNames(), 100, 35, 140, 25),
                GuiConstructor.createLabel("Компания:", 240, 40, 70, 15, Pos.CENTER),
                mbCompany = GuiConstructor.createMenuButton(e -> changeTransferButtonsText(), EnumStatus.NOT_STATED.getDisplayName(), prepareCompanies(), 310, 35, 165, 25),
                GuiConstructor.createLabel("Тип занятости:", 5, 70, 95, 15, Pos.CENTER),
                mbEmploymentType = GuiConstructor.createMenuButton(e -> {}, "", EnumEmploymentType.getEnumDisplayNames(), 100, 65, 170, 25),
                GuiConstructor.createLabel("Зарплата:", 5, 100, 60, 15, Pos.CENTER_RIGHT),
                GuiConstructor.createLabel("от", 65, 100, 20, 15, Pos.CENTER),
                tfMinSalary = GuiConstructor.createTextField("", 85, 95, 85, 25),
                GuiConstructor.createLabel("до", 170, 100, 25, 15, Pos.CENTER),
                tfMaxSalary = GuiConstructor.createTextField("", 195, 95, 85, 25),
                GuiConstructor.createLabel("руб.", 280, 100, 30, 15, Pos.CENTER),

                GuiConstructor.createLabel("Опыт работы:", 5, 130, 80, 15, Pos.CENTER),
                GuiConstructor.createLabel("от", 90, 130, 20, 15, Pos.CENTER),
                tfMinWorkPeriod = GuiConstructor.createTextField("", 110, 125, 60, 25),
                GuiConstructor.createLabel("лет до", 170, 130, 50, 15, Pos.CENTER),
                tfMaxWorkPeriod = GuiConstructor.createTextField("", 220, 125, 60, 25),
                GuiConstructor.createLabel("лет", 280, 130, 30, 15, Pos.CENTER),

                GuiConstructor.createLabel("Адрес:", 5, 165, 470, 22, Pos.CENTER),
                btnTransferAddress = GuiConstructor.createButton(e -> transferAddress(), "Перенести из профиля", 315, 165, 160, 25),

                GuiConstructor.createLabel("Страна:", 5, 200, 65, 15, Pos.CENTER),
                tfCountry = GuiConstructor.createTextField("", 70, 195, 405, 25),
                GuiConstructor.createLabel("Регион:", 5, 230, 65, 15, Pos.CENTER),
                tfRegion = GuiConstructor.createTextField("", 70, 225, 405, 25),
                GuiConstructor.createLabel("Город:", 5, 260, 65, 15, Pos.CENTER),
                tfCity = GuiConstructor.createTextField("", 70, 255, 405, 25),
                GuiConstructor.createLabel("Улица:", 5, 290, 65, 15, Pos.CENTER),
                tfStreet = GuiConstructor.createTextField("", 70, 285, 295, 25),
                GuiConstructor.createLabel("Дом:", 365, 290, 35, 15, Pos.CENTER),
                tfHouse = GuiConstructor.createTextField("", 400, 285, 75, 25),

                GuiConstructor.createLabel("Контактная информация:", 5, 320, 380, 22, Pos.CENTER),
                btnTransferContactInfo = GuiConstructor.createButton(e -> transferContactInfo(), "Перенести из профиля", 315, 320, 160, 25),
                GuiConstructor.createLabel("ФИО:", 5, 355, 65, 15, Pos.CENTER),
                tfContactFio = GuiConstructor.createTextField("", 70, 350, 405, 25),
                GuiConstructor.createLabel("Email:", 5, 385, 65, 15, Pos.CENTER),
                tfContactEmail = GuiConstructor.createTextField("", 70, 380, 200, 25),
                GuiConstructor.createLabel("Телефон:", 270, 385, 60, 15, Pos.CENTER),
                tfContactPhoneNumber = GuiConstructor.createTextField("", 330, 380, 145, 25),
                GuiConstructor.createLabel("Описание:", 5, 415, 65, 15, Pos.CENTER),
                tfContactDescription = GuiConstructor.createTextField("", 70, 410, 405, 25),


                GuiConstructor.createLabel("Описание:", 5, 445, 470, 22, Pos.CENTER),
                taDescription = GuiConstructor.createTextArea("", true, 5, 470, 470, 600),
                GuiConstructor.createLabel("Короткое описание:", 5, 1080, 470, 22, Pos.CENTER),
                taShortDescription = GuiConstructor.createTextArea("", true, 5, 1105, 470, 215),

                professionsContainer.getRoot(),
                skillsContainer.getRoot(),
                languagesContainer.getRoot(),
                citizenShipsContainer.getRoot()

        );


        this.windowController.setScene(this);
        this.windowController.show();
    }

    private void updateCompaniesInfo() {
        UserCompaniesDTO dto = Server.USERS_TABLE.getUserCompaniesDTO(Client.authData);
        String[] sids = dto.getCompaniesId().split(DataBaseTable.numberArraySeparator);
        long[] ids = new long[sids.length];
        for (int i = 0; i < sids.length; i++) ids[i] = Long.parseLong(sids[i]);
        String[] dn = dto.getCompaniesDisplayNames().split(DataBaseTable.objectArraySeparator);

        if (ids.length != dn.length) {
            System.err.println("Ошибка при подготовке companyIdByNameMap. Количество id и displayName компаний не совпадает.");
            return;
        }

        for (int i = 0; i < ids.length; i++) {
            companyIdByNameMap.put(dn[i], ids[i]);
        }
    }

    private List<String> prepareCompanies() {
        List<String> list = new ArrayList<>(companyIdByNameMap.keySet());
        Collections.sort(list);
        list.add(0, EnumStatus.NOT_STATED.getDisplayName());
        return list;
    }

    private void changeTransferButtonsText() {
        if (mbCompany.getText().equals(EnumStatus.NOT_STATED.getDisplayName())) {
            btnTransferAddress.setText("Перенести из профиля");
            btnTransferContactInfo.setText("Перенести из профиля");
        } else {
            btnTransferAddress.setText("Перенести из компании");
            btnTransferContactInfo.setText("Перенести из компании");
        }
    }

    private void transferAddress() {
        AddressDTO dto;

        if (mbCompany.getText().equals(EnumStatus.NOT_STATED.getDisplayName())) dto = Server.USERS_TABLE.getUserAddressDTO(Client.authData);
        else dto = Server.COMPANIES_TABLE.getCompanyAddressDTO(Client.authData, companyIdByNameMap.getOrDefault(mbCompany.getText(), 0L));
        if (dto != null) {
            tfCountry.setText(dto.getCountry());
            tfRegion.setText(dto.getRegion());
            tfCity.setText(dto.getCity());
            tfStreet.setText(dto.getStreet());
            tfHouse.setText(dto.getHouse());
        }
    }

    private void transferContactInfo() {
        ContactInfoDTO dto;

        if (mbCompany.getText().equals(EnumStatus.NOT_STATED.getDisplayName())) dto = Server.USERS_TABLE.getUserContactInfoDTO(Client.authData);
        else dto = Server.COMPANIES_TABLE.getCompanyContactInfoDTO(Client.authData, companyIdByNameMap.getOrDefault(mbCompany.getText(), 0L));
        if (dto != null) {
            tfContactFio.setText(dto.getFio());
            tfContactEmail.setText(dto.getEmail());
            tfContactPhoneNumber.setText(dto.getPhoneNumber());
            tfContactDescription.setText(dto.getDescription());
        }
    }

    private String getChosenProfessions() {
        StringBuilder sb = new StringBuilder();
        for (GuiMenuButtonObject object: professionsContainer.getObjects()) {
            sb.append(object.save()).append(DataBaseTable.objectArraySeparator);
        }
        if (sb.length() > 0) sb.setLength(sb.length()-1);
        return sb.toString();
    }

    private String getChosenSkills() {
        StringBuilder sb = new StringBuilder();
        for (GuiMenuButtonObject object: skillsContainer.getObjects()) {
            sb.append(object.save()).append(DataBaseTable.objectArraySeparator);
        }
        if (sb.length() > 0) sb.setLength(sb.length()-1);
        return sb.toString();
    }

    private String getChosenLanguages() {
        StringBuilder sb = new StringBuilder();
        for (GuiMenuButtonObject object: languagesContainer.getObjects()) {
            sb.append(object.save()).append(DataBaseTable.objectArraySeparator);
        }
        if (sb.length() > 0) sb.setLength(sb.length()-1);
        return sb.toString();
    }
    private String getChosenCitizenShips() {
        StringBuilder sb = new StringBuilder();
        for (GuiMenuButtonObject object: citizenShipsContainer.getObjects()) {
            sb.append(object.save()).append(DataBaseTable.objectArraySeparator);
        }
        if (sb.length() > 0) sb.setLength(sb.length()-1);
        return sb.toString();
    }

    private void createVacancy() {
        try {
            Address address = new Address(tfCountry.getText(), tfRegion.getText(), tfCity.getText(), tfStreet.getText(), tfHouse.getText());
            ContactInfo contactInfo = new ContactInfo(tfContactFio.getText(), tfContactEmail.getText(), tfContactPhoneNumber.getText(), tfContactDescription.getText());
            VacancyCreateDTO dto = new VacancyCreateDTO(
                    tfDisplayName.getText(),
                    EnumSchedule.getEnum(mbSchedule.getText()).getUnlocalizedName(),
                    EnumEmploymentType.getEnum(mbEmploymentType.getText()).getUnlocalizedName(),
                    companyIdByNameMap.getOrDefault(mbCompany.getText(), 0L),
                    Integer.parseInt("0" + tfMinSalary.getText()),
                    Integer.parseInt("0" + tfMaxSalary.getText()),
                    Integer.parseInt("0" + tfMinWorkPeriod.getText()),
                    Integer.parseInt("0" + tfMaxWorkPeriod.getText()),
                    address.save(),
                    contactInfo.save(),
                    Utils.getFixedDescription(taDescription.getText()),
                    Utils.getFixedDescription(taShortDescription.getText()),
                    getChosenProfessions(),
                    getChosenSkills(),
                    getChosenLanguages(),
                    getChosenCitizenShips()
            );

            Server.VACANCIES_TABLE.createVacancy(Client.authData, dto);
            windowController.close();
        } catch (Exception e) {
            System.err.println("Произошла ошибка при создании вакансии");
            e.printStackTrace();
        }
    }
}
