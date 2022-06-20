package ru.gui.scenes.main.tabs.resumes;

import javafx.stage.Stage;
import ru.Client;
import ru.Server;
import ru.controllers.YListController;
import ru.dto.resume.ResumePreviewDTO;
import ru.dto.resume.ResumeSearchDTO;
import ru.gui.GuiConstructor;
import ru.gui.scenes.main.tabs.TabItem;
import ru.gui.scenes.main.tabs.resumes.elements.GuiResumeObject;
import ru.gui.scenes.main.tabs.resumes.elements.GuiSearchResume;
import ru.gui.scenes.main.tabs.resumes.scenes.create.SceneCreateResume;
import ru.gui.windows.WindowController;
import ru.objects.Page;

public class ResumeTab extends TabItem {

    private final YListController<GuiResumeObject> resumeListController = new YListController<>(360, 15, 820, 810, true);
    private final GuiSearchResume guiSearchResume = new GuiSearchResume(this,20, 85);

    public ResumeTab() {
        super("Резюме");

        root.getChildren().addAll(
                GuiConstructor.createButton(e -> new SceneCreateResume(new WindowController(new Stage())), "Создать резюме", 20, 20, 320, 25),
                GuiConstructor.createButton(e -> findUserResume(guiSearchResume.getPageSize()), "Показать мои резюме", 20, 49, 158, 25),
                GuiConstructor.createButton(e -> findAllResume(guiSearchResume.getPageSize()), "Показать все резюме", 182, 49, 158, 25),
                guiSearchResume.getRoot(),
                resumeListController.getScrollPane()

        );
    }

    /**
     * Метод поиска резюме по DTO объекту. Получает от сервера Page с данными о резюме определённого размера.
     * Полученные данные резюме добавляются с контроллер списка и отображаются в GUI
     * @param resumeSearchDTO объект с данными поиска резюме
     */
    public void findResume(ResumeSearchDTO resumeSearchDTO, int pageSize) {
        showResumes(Server.RESUME_TABLE.getResumePage(resumeSearchDTO, pageSize));
    }

    public void findAllResume(int pageSize) {
        showResumes(Server.RESUME_TABLE.getAllResumePage(pageSize));
    }

    public void findUserResume(int pageSize) {
        showResumes(Server.RESUME_TABLE.getUserResumePage(Client.authData, pageSize));
    }

    public void showResumes(Page<ResumePreviewDTO> page) {
        resumeListController.clear();
        for (ResumePreviewDTO resumePreviewDTO : page.getList()) {
            resumeListController.add(new GuiResumeObject(0, resumePreviewDTO));
        }
    }
}
