package ru.gui.scenes.singUp;

import ru.controllers.Validator;
import javafx.scene.control.PasswordField;
import ru.dto.user.UserCreateDTO;
import ru.gui.GuiConstructor;
import ru.gui.GuiObject;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import ru.gui.elements.GuiDateObject;
import ru.utils.enums.EnumSex;
import ru.utils.enums.EnumStatus;
import ru.utils.vectors.DVector2;

import java.time.LocalDate;

public class ContainerSignUp extends GuiObject {

    private final TextField tfEmail, tfPhoneNumber, tfLastName, tfFirstName, tfMiddleName;
    private final PasswordField pfPassword, pfPassword2;

    private final GuiDateObject birthDate;

    private final CheckBox cbSexMale, cbSexFemale;

    public ContainerSignUp(DVector2 pos) {
        super(pos, new DVector2(300, 300));

        tfEmail = createField("E-mail*", 0, 0);
        tfEmail.setOnKeyReleased(e -> Validator.cleanUpEmail(tfEmail));
        tfPhoneNumber = createField("Телефон*", 1, 0);
        tfPhoneNumber.setText("+");
        tfPhoneNumber.setOnKeyReleased(e -> Validator.cleanUpPhoneNumber(tfPhoneNumber));
        pfPassword = createPasswordField("Пароль*", 2, 0);
        pfPassword2 = createPasswordField("Повторите пароль*", 3, 0);

        tfLastName = createField("Фамилия*", 0, 140);
        tfFirstName = createField("Имя*", 1, 140);
        tfMiddleName = createField("Отчество", 2, 140);

        birthDate = new GuiDateObject(120, 230);

        getRoot().getChildren().addAll(
                GuiConstructor.createLabel("Дата рождения*",0, 236,115, 14, Pos.CENTER_RIGHT),
                birthDate.getRoot(),
                GuiConstructor.createLabel("Пол*", 0, 266, 115, 14, Pos.CENTER_RIGHT),
                cbSexMale = GuiConstructor.createCheckBox(e -> setSelected(true), true, EnumSex.MALE.getDisplayName(), 120, 265),
                cbSexFemale = GuiConstructor.createCheckBox(e -> setSelected(false), false,EnumSex.FEMALE.getDisplayName(), 214, 265)
        );
    }

    public void setSelected(boolean isMale) {
        cbSexMale.setSelected(isMale);
        cbSexFemale.setSelected(!isMale);
    }

    public TextField createField(String text, int index, double startY) {
        TextField tf;
        getRoot().getChildren().addAll(
                GuiConstructor.createLabel(text,0, (startY + 6) + 30*index,115, 14, Pos.CENTER_RIGHT),
                tf = GuiConstructor.createTextField("",120, startY + 30*index,180, 25)
        );
        return tf;
    }
    public PasswordField createPasswordField(String text, int index, double startY) {
        PasswordField pf;
        getRoot().getChildren().addAll(
                GuiConstructor.createLabel(text,0, (startY + 6) + 30*index,115, 14, Pos.CENTER_RIGHT),
                pf = GuiConstructor.createPasswordField(120, startY + 30*index,180, 25)
        );
        return pf;
    }

    /**
     * Метод получения DTO объекта с данными о пользователе для отправки серверу и регистрации нового пользователя
     * @return DTO объект с данными пользователя
     * @throws Exception ошибка в данных
     */
    public UserCreateDTO getUserCreateDTO() throws Exception {
        String sex = EnumStatus.NOT_STATED.getUnlocalizedName();
        if (cbSexMale.isSelected()) sex = EnumSex.MALE.getUnlocalizedName();
        else if (cbSexFemale.isSelected()) sex = EnumSex.FEMALE.getUnlocalizedName();
        LocalDate localDate;
        try {
            localDate = birthDate.getLocalDate();
        } catch (Exception e) {
            throw new Exception("Введена некорректная дата рождения.");
        }
        return new UserCreateDTO(
                Validator.validateEmail(tfEmail.getText()),
                Validator.validatePhoneNumber(tfPhoneNumber.getText()),
                Validator.validatePassword(pfPassword.getText(), pfPassword2.getText()),
                tfLastName.getText(),
                tfFirstName.getText(),
                tfMiddleName.getText(),
                localDate.toString(),
                sex
        );
    }

}
