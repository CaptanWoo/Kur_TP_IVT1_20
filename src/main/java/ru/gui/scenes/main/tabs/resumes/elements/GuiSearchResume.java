package ru.gui.scenes.main.tabs.resumes.elements;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import ru.Server;
import ru.db.DataBaseTable;
import ru.dto.resume.ResumeSearchDTO;
import ru.gui.GuiConstructor;
import ru.gui.elements.GuiList;
import ru.gui.elements.GuiObject;
import ru.gui.elements.GuiYListedLabel;
import ru.gui.scenes.main.tabs.resumes.ResumeTab;
import ru.utils.enums.EnumEducationLevel;
import ru.utils.enums.EnumEmploymentType;
import ru.utils.enums.EnumSchedule;
import ru.utils.enums.EnumSex;

public class GuiSearchResume extends GuiObject {

    private final ResumeTab resumeTab;
    private static final String[] types0 = new String[]{"ИЛИ"};
    private static final String[] types1 = new String[]{"И", "ИЛИ"};

    private final GuiList professionList = new GuiList("Специализации:", types1, Server.PROFESSION_TABLE.getHashNames(),5, 5);
    private final GuiList educationLevelList = new GuiList("Образование:", types1, EnumEducationLevel.getEnumDisplayNames(),5, 260);
    private final GuiList employmentTypeList = new GuiList("Тип занятости:", types0, EnumEmploymentType.getEnumDisplayNames(),5, 515);
    private final GuiList scheduleList = new GuiList("График работы:", types0, EnumSchedule.getEnumDisplayNames(),5, 770);
    private final GuiList skillList = new GuiList("Навыки:", types1, Server.SKILLS_TABLE.getHashNames(),5, 1025);
    private final GuiList languageList = new GuiList("Знание языков:", types1, Server.LANGUAGES_TABLE.getHashNames(),5, 1280);
    private final GuiList citizenShipList = new GuiList("Гражданство:", types1, Server.CITIZEN_SHIPS_TABLE.getHashNames(),5, 1535);

    private final TextField tfMinSalary, tfMaxSalary, tfMinWorkPeriod, tfMaxWorkPeriod, tfMinAge, tfMaxAge;
    private final CheckBox cbSexAny, cbSexMale, cbSexFemale;
    private final TextField tfMaxPageSize;

    public GuiSearchResume(ResumeTab resumeTab, double x, double y) {
        super(x, y, 320, 740, true);
        this.resumeTab = resumeTab;

        AnchorPane scrollRoot;
        root.getChildren().addAll(
                GuiConstructor.createLabel("Поиск резюме:", 0, 0, width, 30, Pos.CENTER),
                GuiConstructor.createLabel("Зарплата:", 10, 35, 60, 15),
                GuiConstructor.createLabel("от", 65, 35, 20, 15, Pos.CENTER),
                tfMinSalary = GuiConstructor.createTextField("", 85, 30, 85, 25),
                GuiConstructor.createLabel("до", 170, 35, 25, 15, Pos.CENTER),
                tfMaxSalary = GuiConstructor.createTextField("", 195, 30, 85, 25),
                GuiConstructor.createLabel("руб.", 280, 35, 30, 15, Pos.CENTER),

                GuiConstructor.createLabel("Опыт работы:", 10, 65, 80, 15),
                GuiConstructor.createLabel("от", 90, 65, 20, 15, Pos.CENTER),
                tfMinWorkPeriod = GuiConstructor.createTextField("", 110, 60, 60, 25),
                GuiConstructor.createLabel("лет до", 170, 65, 50, 15, Pos.CENTER),
                tfMaxWorkPeriod = GuiConstructor.createTextField("", 220, 60, 60, 25),
                GuiConstructor.createLabel("лет", 280, 65, 30, 15, Pos.CENTER),

                GuiConstructor.createLabel("Возраст:", 10, 95, 80, 15),
                GuiConstructor.createLabel("от", 90, 95, 20, 15, Pos.CENTER),
                tfMinAge = GuiConstructor.createTextField("", 110, 90, 60, 25),
                GuiConstructor.createLabel("лет до", 170, 95, 50, 15, Pos.CENTER),
                tfMaxAge = GuiConstructor.createTextField("", 220, 90, 60, 25),
                GuiConstructor.createLabel("лет", 280, 95, 30, 15, Pos.CENTER),

                GuiConstructor.createLabel("Пол:", 10, 125, 30, 15),
                cbSexAny = GuiConstructor.createCheckBox(e -> updateSexCB(0), true, "Любой", 50, 125),
                cbSexMale = GuiConstructor.createCheckBox(e -> updateSexCB(1), false, "Мужской", 140, 125),
                cbSexFemale = GuiConstructor.createCheckBox(e -> updateSexCB(2), false, "Женский", 230, 125),

                GuiConstructor.createScrollPane(scrollRoot = new AnchorPane(), 5, 150, 295, 500),

                GuiConstructor.createLabel("Показывать на странице:", 5, 675, 155, 15, Pos.CENTER),
                tfMaxPageSize = GuiConstructor.createTextField("20", 160, 670, 85, 25),
                GuiConstructor.createLabel("резюме", 245, 675, 55, 15, Pos.CENTER),
                GuiConstructor.createButton(e -> findResume(), "Найти резюме", 5, 705, 140, 25),
                GuiConstructor.createButton(e -> clear(), "Очистить", 160, 705, 140, 25)

        );

        scrollRoot.getChildren().addAll(
                professionList.getRoot(),
                educationLevelList.getRoot(),
                employmentTypeList.getRoot(),
                scheduleList.getRoot(),
                skillList.getRoot(),
                languageList.getRoot(),
                citizenShipList.getRoot()
        );
    }

    public int getPageSize() {
        return Integer.parseInt("0" + tfMaxPageSize.getText());
    }

    private void updateSexCB(int num) {
        cbSexAny.setSelected(false);
        cbSexMale.setSelected(false);
        cbSexFemale.setSelected(false);
        switch (num) {
            case 0: cbSexAny.setSelected(true); break;
            case 1: cbSexMale.setSelected(true); break;
            case 2: cbSexFemale.setSelected(true); break;
        }
    }

    private void clear() {
        professionList.clear();
        educationLevelList.clear();
        employmentTypeList.clear();
        scheduleList.clear();
        skillList.clear();
        citizenShipList.clear();
        languageList.clear();

        tfMinSalary.setText("");
        tfMaxSalary.setText("");
        tfMinWorkPeriod.setText("");
        tfMaxWorkPeriod.setText("");
        tfMinAge.setText("");
        tfMaxAge.setText("");
        cbSexAny.setSelected(true);
        cbSexMale.setSelected(false);
        cbSexFemale.setSelected(false);
        tfMaxPageSize.setText("20");
    }

    /**
     * Метод поиска резюме. В нём создаётся объект DTO с данными из полей ввода в GUI.
     * Если данные заполнены некорректно, то высвечивается сообщение об этом.
     * Если данные корректны, то запускается поиск резюме
     */
    private void findResume() {
        try {
            int minSalary = Integer.parseInt("0" + tfMinSalary.getText());
            int maxSalary = Integer.parseInt("0" + tfMaxSalary.getText());
            int minWorkPeriod = Integer.parseInt("0" + tfMinWorkPeriod.getText());
            int maxWorkPeriod = Integer.parseInt("0" + tfMaxWorkPeriod.getText());
            int minAge = Integer.parseInt("0" + tfMinAge.getText());
            int maxAge = Integer.parseInt("0" + tfMaxAge.getText());
            String sex = EnumSex.NOT_STATED.getUnlocalizedName();
            if (cbSexMale.isSelected()) sex = EnumSex.MALE.getUnlocalizedName();
            else if (cbSexFemale.isSelected()) sex = EnumSex.FEMALE.getUnlocalizedName();

            StringBuilder sb = new StringBuilder();

            for (GuiYListedLabel object: professionList.getAll()) {
                sb.append(object.getLabelText()).append(DataBaseTable.objectArraySeparator);
            }
            if (sb.length() > 0) sb.setLength(sb.length()-1);
            String professions = sb.toString();
            sb.setLength(0);

            for (GuiYListedLabel object: educationLevelList.getAll()) {
                sb.append(EnumEducationLevel.getUnlocalizedNameByDisplayName(object.getLabelText())).append(DataBaseTable.objectArraySeparator);
            }
            if (sb.length() > 0) sb.setLength(sb.length()-1);
            String educationLevels = sb.toString();
            sb.setLength(0);


            for (GuiYListedLabel object: employmentTypeList.getAll()) {
                sb.append(EnumEmploymentType.getUnlocalizedNameByDisplayName(object.getLabelText())).append(DataBaseTable.objectArraySeparator);
            }
            if (sb.length() > 0) sb.setLength(sb.length()-1);
            String employmentTypes = sb.toString();
            sb.setLength(0);


            for (GuiYListedLabel object: scheduleList.getAll()) {
                sb.append(EnumSchedule.getUnlocalizedNameByDisplayName(object.getLabelText())).append(DataBaseTable.objectArraySeparator);
            }
            if (sb.length() > 0) sb.setLength(sb.length()-1);
            String schedules = sb.toString();
            sb.setLength(0);

            for (GuiYListedLabel object: skillList.getAll()) {
                sb.append(object.getLabelText()).append(DataBaseTable.objectArraySeparator);
            }
            if (sb.length() > 0) sb.setLength(sb.length()-1);
            String skills = sb.toString();
            sb.setLength(0);

            for (GuiYListedLabel object: citizenShipList.getAll()) {
                sb.append(object.getLabelText()).append(DataBaseTable.objectArraySeparator);
            }
            if (sb.length() > 0) sb.setLength(sb.length()-1);
            String citizenShips = sb.toString();
            sb.setLength(0);

            for (GuiYListedLabel object: languageList.getAll()) {
                sb.append(object.getLabelText()).append(DataBaseTable.objectArraySeparator);
            }
            if (sb.length() > 0) sb.setLength(sb.length()-1);
            String languages = sb.toString();
            sb.setLength(0);

            resumeTab.findResume(new ResumeSearchDTO(
                    minSalary, maxSalary, minWorkPeriod, maxWorkPeriod, minAge, maxAge, sex,
                    educationLevels, employmentTypes, schedules, professions, skills, languages, citizenShips,
                    educationLevelList.getType(), professionList.getType(), skillList.getType(),
                    languageList.getType(), citizenShipList.getType()
            ), getPageSize());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
