package ru.gui.scenes.main.tabs.vacancies;

import javafx.stage.Stage;
import ru.Client;
import ru.Server;
import ru.controllers.YListController;
import ru.dto.vacancy.VacancyPreviewDTO;
import ru.dto.vacancy.VacancySearchDTO;
import ru.gui.GuiConstructor;
import ru.gui.scenes.main.tabs.TabItem;
import ru.gui.scenes.main.tabs.vacancies.elements.GuiVacancyObject;
import ru.gui.scenes.main.tabs.vacancies.elements.GuiVacancySearch;
import ru.gui.scenes.main.tabs.vacancies.scenes.create.SceneCreateVacancy;
import ru.gui.windows.WindowController;
import ru.objects.Page;

public class VacancyTab extends TabItem {

    private final YListController<GuiVacancyObject> vacancyListController = new YListController<>(360, 15, 820, 810, true);
    private final GuiVacancySearch guiVacancySearch = new GuiVacancySearch(this,20, 85);

    public VacancyTab() {
        super("Вакансии");

        root.getChildren().addAll(
                GuiConstructor.createButton(e -> new SceneCreateVacancy(new WindowController(new Stage())), "Создать вакансию", 20, 20, 320, 25),
                GuiConstructor.createButton(e -> findUserVacancies(guiVacancySearch.getPageSize()), "Показать мои вакансии", 20, 49, 158, 25),
                GuiConstructor.createButton(e -> findAllVacancies(guiVacancySearch.getPageSize()), "Показать все вакансии", 182, 49, 158, 25),
                guiVacancySearch.getRoot(),
                vacancyListController.getScrollPane()
        );
    }

    /**
     * Метод поиска резюме по DTO объекту. Получает от сервера Page с данными о резюме определённого размера.
     * Полученные данные резюме добавляются с контроллер списка и отображаются в GUI
     * @param vacancySearchDTO объект с данными поиска резюме
     */
    public void findVacancies(VacancySearchDTO vacancySearchDTO, int pageSize) {
        showVacancies(Server.VACANCIES_TABLE.getVacancyPage(vacancySearchDTO, pageSize));
    }

    public void findAllVacancies(int pageSize) {
        showVacancies(Server.VACANCIES_TABLE.getAllVacancyPage(pageSize));
    }

    public void findUserVacancies(int pageSize) {
        showVacancies(Server.VACANCIES_TABLE.getUserVacancyPage(Client.authData, pageSize));
    }

    public void showVacancies(Page<VacancyPreviewDTO> page) {
        vacancyListController.clear();
        for (VacancyPreviewDTO vacancyPreviewDTO : page.getList()) {
            vacancyListController.add(new GuiVacancyObject(0, vacancyPreviewDTO));
        }
    }
}
