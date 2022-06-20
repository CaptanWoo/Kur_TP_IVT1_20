package ru;

import ru.gui.scenes.start.SceneStart;
import ru.gui.windows.WindowController;
import javafx.application.Application;
import javafx.stage.Stage;
import ru.objects.AuthData;

public class Client extends Application {

    public static AuthData authData; //Ключ авторизации клиента
    public static WindowController windowController; //Контроллер GUI окна

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        windowController = new WindowController(primaryStage);
        windowController.setScene(new SceneStart(windowController));
        primaryStage.setScene(windowController.getScene());
        primaryStage.show();
    }
}
