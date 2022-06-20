package ru.gui.scenes.main.tabs.companies.scenes.info;

import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import ru.Client;
import ru.Server;
import ru.dto.company.CompanyInfoDTO;
import ru.dto.vacancy.VacancyInfoDTO;
import ru.gui.GuiConstructor;
import ru.gui.scenes.SceneContainer;
import ru.gui.scenes.main.tabs.resumes.scenes.info.GuiInfoContainer;
import ru.gui.scenes.main.tabs.resumes.scenes.info.objects.GuiInfoListedObject;
import ru.gui.scenes.main.tabs.resumes.scenes.info.objects.GuiLabelObject;
import ru.gui.windows.WindowController;
import ru.utils.Utils;
import ru.utils.enums.EnumEmploymentType;
import ru.utils.enums.EnumSchedule;
import ru.utils.objects.ContactInfo;
import ru.utils.vectors.DVector2;

import java.util.ArrayList;
import java.util.List;

public class SceneCompanyInfo extends SceneContainer {

    public SceneCompanyInfo(WindowController windowController, long companyId) {
        super(windowController, "Просмотр компании", new DVector2(515, 940));

        AnchorPane scrollRoot;
        root.getChildren().addAll(
                GuiConstructor.createScrollPane(scrollRoot = new AnchorPane(), 0, 0, 500, 900)
        );

        CompanyInfoDTO dto = getCompanyInfoDTO(companyId);

        GuiInfoContainer<GuiInfoListedObject<GuiLabelObject>> professionsContainer = new GuiInfoContainer<>(
                "Специализации:", getGuiLabelObject(dto.getProfessions()), 15, 807, 230, 348
        );

        ContactInfo contactInfo = new ContactInfo(dto.getContactInfo());

        TextArea taDescription;
        scrollRoot.getChildren().addAll(
                GuiConstructor.createRectangle("#FFFFFF", 0, 0, 500, 1165),
                GuiConstructor.createLabel(dto.getDisplayName(), 15, 10, 470, 19),
                GuiConstructor.createLabel("Тип компании: " + dto.getCompanyType(), 15, 32, 470, 15),
                GuiConstructor.createLabel("Вакансии: " + dto.getVacanciesCount(), 15, 55, 100, 15),
                GuiConstructor.createLabel("Вебсайт: " + dto.getWebsite(), 120, 55, 365, 15),
                GuiConstructor.createLabel("Контактная информация", 15, 73, 470, 25, Pos.CENTER),
                GuiConstructor.createLabel("ФИО: " + contactInfo.getFio(), 15, 100, 470, 15),
                GuiConstructor.createLabel("Email: " + contactInfo.getEmail(), 15, 120, 230, 15),
                GuiConstructor.createLabel("Телефон: " + contactInfo.getPhoneNumber(), 255, 120, 230, 15),
                GuiConstructor.createLabel("Описание: " + contactInfo.getDescription(), 15, 140, 470, 15),
                GuiConstructor.createLabel("Адрес: " + dto.getAddress(), 15, 165, 470, 15),

                taDescription = GuiConstructor.createTextArea("", true, false, 15, 190, 470, 600),

                professionsContainer.getRoot()
        );

        taDescription.setText("");
        for (String line: Utils.getNotFixedDescription(dto.getDescription())) {
            taDescription.appendText(line);
        }

        windowController.setScene(this);
        windowController.show();
    }

    public List<GuiInfoListedObject<GuiLabelObject>> getGuiLabelObject(String data) {
        List<GuiInfoListedObject<GuiLabelObject>> list = new ArrayList<>();

        for (String s: data.split(", ")) {
            GuiLabelObject g = new GuiLabelObject(s,0, 0, 205, 20);
            list.add(new GuiInfoListedObject<>(g, 0, 5, 5, g.getWidth(), g.getHeight()));
        }
        return list;
    }

    private CompanyInfoDTO getCompanyInfoDTO(long companyId) {
        return Server.COMPANIES_TABLE.getCompanyInfoDTO(Client.authData, companyId);
    }
}
