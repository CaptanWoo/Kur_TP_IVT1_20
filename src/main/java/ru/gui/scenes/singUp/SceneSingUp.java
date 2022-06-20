package ru.gui.scenes.singUp;

import ru.Server;
import ru.gui.GuiConstructor;
import ru.gui.scenes.SceneContainer;
import ru.gui.scenes.logIn.SceneLogIn;
import ru.gui.scenes.start.SceneStart;
import ru.gui.windows.WindowController;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import ru.utils.vectors.DVector2;

public class SceneSingUp extends SceneContainer {

    private final ContainerSignUp containerSignUp;

    private final Label labelStatus;

    public SceneSingUp(WindowController windowController) {
        super(windowController, "Регистрация аккаунта", new DVector2(500, 500));

        containerSignUp = new ContainerSignUp(new DVector2(40, 70));
        root.getChildren().addAll(
                GuiConstructor.createButton(e -> windowController.setScene(new SceneStart(windowController)),"←",10, 10,30, 25),
                GuiConstructor.createLabel("Регистрация аккаунта",160, 45,190, 14, Pos.CENTER),
                containerSignUp.getRoot(),
                labelStatus = GuiConstructor.createLabel("",40, 370,420, 14, Pos.CENTER),
                GuiConstructor.createButton(e -> signUp(), "Зарегистрироваться",175, 420,150, 25)
        );
    }

    private void signUp() {
        try {
            Server.USERS_TABLE.createUser(containerSignUp.getUserCreateDTO());
            windowController.setScene(new SceneLogIn(windowController));
        } catch (Exception e) {
            labelStatus.setText(e.getMessage());
            e.printStackTrace();
        }
    }
}
