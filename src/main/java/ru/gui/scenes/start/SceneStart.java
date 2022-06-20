package ru.gui.scenes.start;

import ru.gui.GuiConstructor;
import ru.gui.scenes.SceneContainer;
import ru.gui.scenes.logIn.SceneLogIn;
import ru.gui.scenes.singUp.SceneSingUp;
import ru.gui.windows.WindowController;
import ru.utils.vectors.DVector2;

public class SceneStart extends SceneContainer {

    public SceneStart(WindowController windowController) {
        super(windowController, "Кадровое агенство", new DVector2(500, 500));

        root.getChildren().addAll(
                GuiConstructor.createButton(e -> windowController.setScene(new SceneLogIn(windowController)), "Войти",175, 190,150, 25),
                GuiConstructor.createButton(e -> windowController.setScene(new SceneSingUp(windowController)),"Зарегистрироваться",175, 220,150, 25)
        );
    }
}
