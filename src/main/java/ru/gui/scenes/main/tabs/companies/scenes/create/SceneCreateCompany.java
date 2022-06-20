package ru.gui.scenes.main.tabs.companies.scenes.create;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import ru.Client;
import ru.Server;
import ru.db.DataBaseTable;
import ru.dto.company.CompanyCreateDTO;
import ru.dto.objects.AddressDTO;
import ru.dto.objects.ContactInfoDTO;
import ru.dto.vacancy.VacancyCreateDTO;
import ru.gui.GuiConstructor;
import ru.gui.scenes.SceneContainer;
import ru.gui.scenes.main.tabs.profile.containers.GuiInfoContainer;
import ru.gui.scenes.main.tabs.profile.containers.object.GuiMenuButtonObject;
import ru.gui.windows.WindowController;
import ru.utils.Utils;
import ru.utils.enums.EnumEmploymentType;
import ru.utils.enums.EnumSchedule;
import ru.utils.enums.EnumStatus;
import ru.utils.objects.Address;
import ru.utils.objects.ContactInfo;
import ru.utils.vectors.DVector2;

import java.util.*;

public class SceneCreateCompany extends SceneContainer {

    private final TextField tfDisplayName, tfWebsite, tfCountry, tfRegion, tfCity, tfStreet, tfHouse,
                            tfContactFio, tfContactEmail, tfContactPhoneNumber, tfContactDescription;
    private final TextArea taDescription;
    private final MenuButton mbCompanyType;
    private final GuiInfoContainer<GuiMenuButtonObject> professionsContainer;

    public SceneCreateCompany(WindowController windowController) {
        super(windowController, "Создание компании", new DVector2(495, 840));

        AnchorPane scrollRoot;
        root.getChildren().addAll(
                GuiConstructor.createScrollPane(scrollRoot = new AnchorPane(), 0, 0, 480, 730),
                GuiConstructor.createButton(e -> createCompany(), "Создать компанию", 170, 760, 155, 25)
        );

        professionsContainer = new GuiInfoContainer<>("Специализации:", new GuiMenuButtonObject(0, 0, 359, 29, Server.PROFESSION_TABLE.getHashNames()), 50, 990, 380, 488, 251, 2, 25, 2, true, true);

        scrollRoot.getChildren().addAll(
                GuiConstructor.createRectangle("#FFFFFF", 0, 0, 480, 1500),
                GuiConstructor.createLabel("Название:", 5, 10, 65, 15, Pos.CENTER),
                tfDisplayName = GuiConstructor.createTextField("", 70, 5, 405, 25),
                GuiConstructor.createLabel("Тип компании:", 5, 40, 95, 15, Pos.CENTER),
                mbCompanyType = GuiConstructor.createMenuButton(e->{}, EnumStatus.NOT_STATED.getDisplayName(), prepareCompanyTypes(), 100, 35, 95, 25),
                GuiConstructor.createLabel("Сайт:", 195, 40, 40, 15, Pos.CENTER),
                tfWebsite = GuiConstructor.createTextField("", 235, 35, 240, 25),

                GuiConstructor.createLabel("Адрес:", 5, 70, 470, 22, Pos.CENTER),
                GuiConstructor.createButton(e -> transferAddress(), "Перенести из профиля", 315, 70, 160, 25),

                GuiConstructor.createLabel("Страна:", 5, 105, 65, 15, Pos.CENTER),
                tfCountry = GuiConstructor.createTextField("", 70, 100, 405, 25),
                GuiConstructor.createLabel("Регион:", 5, 135, 65, 15, Pos.CENTER),
                tfRegion = GuiConstructor.createTextField("", 70, 130, 405, 25),
                GuiConstructor.createLabel("Город:", 5, 165, 65, 15, Pos.CENTER),
                tfCity = GuiConstructor.createTextField("", 70, 160, 405, 25),
                GuiConstructor.createLabel("Улица:", 5, 195, 65, 15, Pos.CENTER),
                tfStreet = GuiConstructor.createTextField("", 70, 190, 295, 25),
                GuiConstructor.createLabel("Дом:", 365, 195, 35, 15, Pos.CENTER),
                tfHouse = GuiConstructor.createTextField("", 400, 190, 75, 25),

                GuiConstructor.createLabel("Контактная информация:", 5, 225, 380, 22, Pos.CENTER),
                GuiConstructor.createButton(e -> transferContactInfo(), "Перенести из профиля", 315, 225, 160, 25),
                GuiConstructor.createLabel("ФИО:", 5, 260, 65, 15, Pos.CENTER),
                tfContactFio = GuiConstructor.createTextField("", 70, 255, 405, 25),
                GuiConstructor.createLabel("Email:", 5, 290, 65, 15, Pos.CENTER),
                tfContactEmail = GuiConstructor.createTextField("", 70, 285, 200, 25),
                GuiConstructor.createLabel("Телефон:", 270, 290, 60, 15, Pos.CENTER),
                tfContactPhoneNumber = GuiConstructor.createTextField("", 330, 285, 145, 25),
                GuiConstructor.createLabel("Описание:", 5, 320, 65, 15, Pos.CENTER),
                tfContactDescription = GuiConstructor.createTextField("", 70, 315, 405, 25),


                GuiConstructor.createLabel("Описание:", 5, 350, 470, 22, Pos.CENTER),
                taDescription = GuiConstructor.createTextArea("", true, 5, 375, 470, 600),

                professionsContainer.getRoot()

        );


        this.windowController.setScene(this);
        this.windowController.show();
    }

    private List<String> prepareCompanyTypes() {
        List<String> list = new ArrayList<>(Server.COMPANY_TYPES_TABLE.getHashNames());
        Collections.sort(list);
        list.add(0, EnumStatus.NOT_STATED.getDisplayName());
        return list;
    }

    private void transferAddress() {
        AddressDTO dto = Server.USERS_TABLE.getUserAddressDTO(Client.authData);
        if (dto != null) {
            tfCountry.setText(dto.getCountry());
            tfRegion.setText(dto.getRegion());
            tfCity.setText(dto.getCity());
            tfStreet.setText(dto.getStreet());
            tfHouse.setText(dto.getHouse());
        }
    }

    private void transferContactInfo() {
        ContactInfoDTO dto = Server.USERS_TABLE.getUserContactInfoDTO(Client.authData);
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

    private void createCompany() {
        try {
            Address address = new Address(tfCountry.getText(), tfRegion.getText(), tfCity.getText(), tfStreet.getText(), tfHouse.getText());
            ContactInfo contactInfo = new ContactInfo(tfContactFio.getText(), tfContactEmail.getText(), tfContactPhoneNumber.getText(), tfContactDescription.getText());
            CompanyCreateDTO dto = new CompanyCreateDTO(
                    tfDisplayName.getText(),
                    mbCompanyType.getText(),
                    tfWebsite.getText(),
                    address.save(),
                    contactInfo.save(),
                    Utils.getFixedDescription(taDescription.getText()),
                    getChosenProfessions()
            );

            Server.COMPANIES_TABLE.createCompany(Client.authData, dto);
            windowController.close();
        } catch (Exception e) {
            System.err.println("Произошла ошибка при создании компании");
            e.printStackTrace();
        }
    }
}
