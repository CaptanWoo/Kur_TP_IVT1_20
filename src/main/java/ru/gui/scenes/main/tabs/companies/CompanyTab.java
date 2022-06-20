package ru.gui.scenes.main.tabs.companies;

import javafx.stage.Stage;
import ru.Client;
import ru.Server;
import ru.controllers.YListController;
import ru.dto.company.CompanyPreviewDTO;
import ru.dto.company.CompanySearchDTO;
import ru.gui.GuiConstructor;
import ru.gui.scenes.main.tabs.TabItem;
import ru.gui.scenes.main.tabs.companies.elements.GuiCompanyObject;
import ru.gui.scenes.main.tabs.companies.elements.GuiCompanySearch;
import ru.gui.scenes.main.tabs.companies.scenes.create.SceneCreateCompany;
import ru.gui.windows.WindowController;
import ru.objects.Page;

public class CompanyTab extends TabItem {

    private final YListController<GuiCompanyObject> companyListController = new YListController<>(360, 15, 820, 810, true);
    private final GuiCompanySearch guiCompanySearch = new GuiCompanySearch(this,20, 85);

    public CompanyTab() {
        super("Компании");

        root.getChildren().addAll(
                GuiConstructor.createButton(e -> new SceneCreateCompany(new WindowController(new Stage())), "Создать компанию", 20, 20, 320, 25),
                GuiConstructor.createButton(e -> findUserCompanies(guiCompanySearch.getPageSize()), "Показать мои компании", 20, 49, 158, 25),
                GuiConstructor.createButton(e -> findAllCompanies(guiCompanySearch.getPageSize()), "Показать все компании", 182, 49, 158, 25),
                guiCompanySearch.getRoot(),
                companyListController.getScrollPane()
        );
    }

    /**
     * Метод поиска резюме по DTO объекту. Получает от сервера Page с данными о резюме определённого размера.
     * Полученные данные компании добавляются с контроллер списка и отображаются в GUI
     * @param companySearchDTO объект с данными поиска компании
     */
    public void findCompanies(CompanySearchDTO companySearchDTO, int pageSize) {
        showVacancies(Server.COMPANIES_TABLE.getVacancyPage(companySearchDTO, pageSize));
    }

    public void findAllCompanies(int pageSize) {
        showVacancies(Server.COMPANIES_TABLE.getAllVacancyPage(pageSize));
    }

    public void findUserCompanies(int pageSize) {
        showVacancies(Server.COMPANIES_TABLE.getUserVacancyPage(Client.authData, pageSize));
    }

    public void showVacancies(Page<CompanyPreviewDTO> page) {
        companyListController.clear();
        for (CompanyPreviewDTO companyPreviewDTO : page.getList()) {
            companyListController.add(new GuiCompanyObject(0, companyPreviewDTO));
        }
    }
}
