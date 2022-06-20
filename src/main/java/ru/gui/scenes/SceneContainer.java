package ru.gui.scenes;

import ru.gui.windows.WindowController;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import ru.utils.vectors.DVector2;

public class SceneContainer {

    protected final WindowController windowController;
    private final Scene scene;
    protected final AnchorPane root;

    private final String title;
    private final DVector2 size;

    public SceneContainer(WindowController windowController, String title, DVector2 size) {
        this.windowController = windowController;
        root = new AnchorPane();
        scene = new Scene(root);
        this.title = title;
        this.size = new DVector2(size);
    }

    public Scene getScene() {
        return scene;
    }

    public AnchorPane getRoot() {
        return root;
    }

    public String getTitle() {
        return title;
    }

    public DVector2 getSize() {
        return size;
    }

}
