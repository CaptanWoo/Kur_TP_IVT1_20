package ru.gui.scenes.main.tabs;

import javafx.scene.control.TabPane;
import ru.Client;
import ru.Server;
import ru.gui.scenes.SceneContainer;
import ru.gui.scenes.main.tabs.companies.CompanyTab;
import ru.gui.scenes.main.tabs.profile.ProfileTab;
import ru.gui.scenes.main.tabs.resumes.ResumeTab;
import ru.gui.scenes.main.tabs.vacancies.VacancyTab;
import ru.gui.scenes.main.tabs.сontrol.ControlTab;
import ru.gui.windows.WindowController;
import ru.utils.enums.EnumRole;
import ru.utils.vectors.DVector2;

public class SceneMain extends SceneContainer {

    public SceneMain(WindowController windowController) {
        super(windowController, "Приложение", new DVector2(1200, 900));

        TabPane tabPane = new TabPane();

        //Добавляем вкладки в GUI
        tabPane.getTabs().addAll(
                new ProfileTab().getTab(),
                new ResumeTab().getTab(),
                new VacancyTab().getTab(),
                new CompanyTab().getTab()
        );

        //Если у пользователя есть роль "Модератор" и выше, то добавляем вкладку "Панель управления"
        if (Server.USERS_TABLE.hasAccess(Client.authData, EnumRole.MODERATOR)) {
            tabPane.getTabs().add(new ControlTab().getTab());
        }

        root.getChildren().addAll(
                tabPane
        );
    }

}
