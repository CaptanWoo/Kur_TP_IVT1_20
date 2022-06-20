package ru.gui.scenes.main.tabs.profile.containers;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import lombok.Getter;
import ru.dto.user.UserProfileDTO;
import ru.dto.user.UserProfileUpdateDTO;
import ru.gui.GuiConstructor;
import ru.gui.elements.GuiDateObject;
import ru.gui.elements.GuiObject;
import ru.utils.enums.EnumSex;
import ru.utils.objects.Address;

import java.time.LocalDate;

public class GuiMainInfoContainer extends GuiObject {

    @Getter private final TextField
            tfLastName, tfFirstName, tfMiddleName,
            tfEmail, tfPhoneNumber, tfCountry, tfRegion, tfCity, tfStreet, tfHouse;
    @Getter private final CheckBox cbMale, cbFemale;
    @Getter private final GuiDateObject birthDate;

    public GuiMainInfoContainer(double x, double y) {
        super(x, y, 860, 205);

        birthDate = new GuiDateObject(100, 180);
        root.getChildren().addAll(
                GuiConstructor.createLabel("Фамилия:", 0, 5, 60, 15, Pos.CENTER_RIGHT),
                tfLastName = GuiConstructor.createTextField("", 65, 0, 215, 25),
                GuiConstructor.createLabel("Имя:", 0, 35, 60, 15, Pos.CENTER_RIGHT),
                tfFirstName = GuiConstructor.createTextField("", 65, 30, 215, 25),
                GuiConstructor.createLabel("Отчество:", 0, 65, 60, 15, Pos.CENTER_RIGHT),
                tfMiddleName = GuiConstructor.createTextField("", 65, 60, 215, 25),
                GuiConstructor.createLabel("Пол:", 0, 95, 60, 15, Pos.CENTER_RIGHT),
                cbMale = GuiConstructor.createCheckBox(e -> updateCB(0), true, "Мужской", 65, 95),
                cbFemale = GuiConstructor.createCheckBox(e -> updateCB(1), false, "Женский", 185, 95),
                GuiConstructor.createLabel("Email:", 0, 125, 60, 15, Pos.CENTER_RIGHT),
                tfEmail = GuiConstructor.createTextField("", 65, 120, 215, 25),
                GuiConstructor.createLabel("Телефон:", 0, 155, 60, 15, Pos.CENTER_RIGHT),
                tfPhoneNumber = GuiConstructor.createTextField("", 65, 150, 215, 25),
                GuiConstructor.createLabel("Дата рождения:", 0, 185, 100, 15, Pos.CENTER_RIGHT),
                birthDate.getRoot(),
                GuiConstructor.createLabel("Страна:", 280, 5, 50, 15, Pos.CENTER_RIGHT),
                tfCountry = GuiConstructor.createTextField("", 335, 0, 315, 25),
                GuiConstructor.createLabel("Регион:", 280, 35, 50, 15, Pos.CENTER_RIGHT),
                tfRegion = GuiConstructor.createTextField("", 335, 30, 315, 25),
                GuiConstructor.createLabel("Город:", 280, 65, 50, 15, Pos.CENTER_RIGHT),
                tfCity = GuiConstructor.createTextField("", 335, 60, 315, 25),
                GuiConstructor.createLabel("Улица:", 280, 95, 50, 15, Pos.CENTER_RIGHT),
                tfStreet = GuiConstructor.createTextField("", 335, 90, 220, 25),
                GuiConstructor.createLabel("Дом:", 555, 95, 35, 15, Pos.CENTER),
                tfHouse = GuiConstructor.createTextField("", 590, 90, 60, 25)
        );
    }

    private void updateCB(int index) {
        if (index == 0) {
            cbMale.setSelected(true);
            cbFemale.setSelected(false);
        } else {
            cbMale.setSelected(false);
            cbFemale.setSelected(true);
        }
    }

    public void load(UserProfileUpdateDTO dto) {
        tfEmail.setText(dto.getEmail());
        tfPhoneNumber.setText(dto.getPhoneNumber());
        tfLastName.setText(dto.getLastName());
        tfFirstName.setText(dto.getFirstName());
        tfMiddleName.setText(dto.getMiddleName());
        birthDate.setLocalDate(LocalDate.parse(dto.getBirthDate()));
        if (dto.getSex().equals(EnumSex.MALE.getUnlocalizedName())) updateCB(0);
        else updateCB(1);
        Address address = new Address(dto.getAddress());
        tfCountry.setText(address.getCountry());
        tfRegion.setText(address.getRegion());
        tfCity.setText(address.getCity());
        tfStreet.setText(address.getStreet());
        tfHouse.setText(address.getHouse());

    }
}
