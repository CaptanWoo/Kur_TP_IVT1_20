package ru.gui.scenes.logIn;

import ru.Client;
import ru.Server;
import ru.gui.GuiConstructor;
import ru.gui.scenes.SceneContainer;
import ru.gui.scenes.main.tabs.SceneMain;
import ru.gui.scenes.start.SceneStart;
import ru.gui.windows.WindowController;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import ru.utils.vectors.DVector2;

public class SceneLogIn extends SceneContainer {

    private final ContainerLogin containerLogin;

    private final Label labelStatus;

    public SceneLogIn(WindowController windowController) {
        super(windowController, "Вход в аккаунт", new DVector2(500, 500));

        containerLogin = new ContainerLogin(new DVector2(90, 160));
        root.getChildren().addAll(
                GuiConstructor.createButton(e -> windowController.setScene(new SceneStart(windowController)),"←", 10, 10,30, 25),
                GuiConstructor.createLabel("Вход в аккаунт",155, 130,190, 14, Pos.CENTER),
                containerLogin.getRoot(),
                labelStatus = GuiConstructor.createLabel("", 40, 220,420, 14, Pos.CENTER),
                GuiConstructor.createButton(e -> logIn(), "Войти",195, 240,110, 25)
                //GuiConstructor.createButton(e -> {}, "Забыл пароль",195, 270, 110, 25)
        );
    }

    private void logIn() {
        try {
            String login = containerLogin.getLogin();
            String password = containerLogin.getPassword();
            Client.authData = Server.USERS_TABLE.logIn(login, password);
            windowController.setScene(new SceneMain(windowController));
        } catch (Exception e) {
            labelStatus.setText(e.getMessage());
            e.printStackTrace();
        }

    }
}
