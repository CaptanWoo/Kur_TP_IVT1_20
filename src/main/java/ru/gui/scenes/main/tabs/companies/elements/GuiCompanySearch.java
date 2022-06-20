package ru.gui.scenes.main.tabs.companies.elements;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import ru.Server;
import ru.db.DataBaseTable;
import ru.dto.company.CompanySearchDTO;
import ru.gui.GuiConstructor;
import ru.gui.elements.GuiList;
import ru.gui.elements.GuiObject;
import ru.gui.elements.GuiYListedLabel;
import ru.gui.scenes.main.tabs.companies.CompanyTab;
import ru.utils.objects.Address;

public class GuiCompanySearch extends GuiObject {

    private final CompanyTab companyTab;
    private static final String[] types0 = new String[]{"ИЛИ"};
    private static final String[] types1 = new String[]{"И", "ИЛИ"};

    private final GuiList professionList = new GuiList("Специализации:", types1, Server.PROFESSION_TABLE.getHashNames(),5, 5);
    private final GuiList companyTypesList = new GuiList("Типы компаний:", types0, Server.COMPANY_TYPES_TABLE.getHashNames(),5, 260);

    private final TextField tfName, tfCountry, tfRegion, tfCity;
    private final TextField tfMaxPageSize;

    public GuiCompanySearch(CompanyTab companyTab, double x, double y) {
        super(x, y, 320, 740, true);
        this.companyTab = companyTab;

        AnchorPane scrollRoot;
        root.getChildren().addAll(
                GuiConstructor.createLabel("Поиск компаний:", 0, 0, width, 30, Pos.CENTER),

                GuiConstructor.createLabel("Название:", 10, 35, 60, 15, Pos.CENTER),
                tfName = GuiConstructor.createTextField("", 70, 30, 235, 25),
                GuiConstructor.createLabel("Страна:", 10, 65, 60, 15, Pos.CENTER),
                tfCountry = GuiConstructor.createTextField("", 70, 60, 235, 25),
                GuiConstructor.createLabel("Регион:", 10, 95, 60, 15, Pos.CENTER),
                tfRegion = GuiConstructor.createTextField("", 70, 90, 235, 25),
                GuiConstructor.createLabel("Город:", 10, 125, 60, 15, Pos.CENTER),
                tfCity = GuiConstructor.createTextField("", 70, 120, 235, 25),

                GuiConstructor.createScrollPane(scrollRoot = new AnchorPane(), 5, 155, 295, 495),

                GuiConstructor.createLabel("Показывать на странице:", 5, 675, 155, 15, Pos.CENTER),
                tfMaxPageSize = GuiConstructor.createTextField("20", 160, 670, 75, 25),
                GuiConstructor.createLabel("компаний", 235, 675, 65, 15, Pos.CENTER),
                GuiConstructor.createButton(e -> findCompany(), "Найти компании", 5, 705, 140, 25),
                GuiConstructor.createButton(e -> clear(), "Очистить", 160, 705, 140, 25)

        );

        scrollRoot.getChildren().addAll(
                professionList.getRoot(),
                companyTypesList.getRoot()
        );
    }

    public int getPageSize() {
        return Integer.parseInt("0" + tfMaxPageSize.getText());
    }


    private void clear() {
        professionList.clear();
        companyTypesList.clear();

        tfName.setText("");
        tfCountry.setText("");
        tfRegion.setText("");
        tfCity.setText("");
        tfMaxPageSize.setText("20");
    }

    /**
     * Метод поиска резюме. В нём создаётся объект DTO с данными из полей ввода в GUI.
     * Если данные заполнены некорректно, то высвечивается сообщение об этом.
     * Если данные корректны, то запускается поиск резюме
     */
    private void findCompany() {
        try {
            String name = tfName.getText();
            Address address = new Address(
                    tfCountry.getText(),
                    tfRegion.getText(),
                    tfCity.getText(),
                    "",
                    ""
            );

            StringBuilder sb = new StringBuilder();

            for (GuiYListedLabel object: professionList.getAll()) {
                sb.append(object.getLabelText()).append(DataBaseTable.objectArraySeparator);
            }
            if (sb.length() > 0) sb.setLength(sb.length()-1);
            String professions = sb.toString();
            sb.setLength(0);

            for (GuiYListedLabel object: companyTypesList.getAll()) {
                sb.append(object.getLabelText()).append(DataBaseTable.objectArraySeparator);
            }
            if (sb.length() > 0) sb.setLength(sb.length()-1);
            String companyTypes = sb.toString();
            sb.setLength(0);


            companyTab.findCompanies(new CompanySearchDTO(
                    name,
                    address.save(),
                    companyTypes,
                    professions,
                    professionList.getType()
            ), getPageSize());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
