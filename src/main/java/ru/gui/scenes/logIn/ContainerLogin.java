package ru.gui.scenes.logIn;

import javafx.scene.control.PasswordField;
import ru.gui.GuiConstructor;
import ru.gui.GuiObject;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import ru.utils.vectors.DVector2;

public class ContainerLogin extends GuiObject {

    private final TextField tfLogin;
    private final PasswordField pfPassword;

    public ContainerLogin(DVector2 pos) {
        super(pos, new DVector2(255, 55));
        getRoot().getChildren().addAll(
                GuiConstructor.createLabel("Логин",0, 6,60, 14, Pos.CENTER_RIGHT),
                GuiConstructor.createLabel("Пароль",0, 36,60, 14, Pos.CENTER_RIGHT),
                tfLogin = GuiConstructor.createTextField("",65, 0,190, 25),
                pfPassword = GuiConstructor.createPasswordField(65, 30,190, 25)
        );
    }

    public String getLogin() {
        return tfLogin.getText();
    }

    public String getPassword() {
        return pfPassword.getText();
    }
}
