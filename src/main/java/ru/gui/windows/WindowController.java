package ru.gui.windows;

import ru.gui.scenes.SceneContainer;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WindowController {

    private final Stage window;
    private SceneContainer scene;

    public WindowController(Stage window) {
        this.window = window;
    }

    public void setScene(SceneContainer scene) {
        this.scene = scene;
        window.setScene(scene.getScene());
        window.setTitle(scene.getTitle());
        window.setWidth(scene.getSize().x + 16d);
        window.setHeight(scene.getSize().y + 9d);
        window.setResizable(false);
    }


    public SceneContainer getSceneContainer() {
        return scene;
    }

    public Scene getScene() {
        return scene.getScene();
    }

    public void show() {
        window.show();
    }
    public void close() {
        window.close();
    }
}
