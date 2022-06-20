package ru.gui.scenes.main.tabs.profile.containers.object;

import javafx.geometry.Pos;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import ru.gui.GuiConstructor;
import ru.gui.elements.GuiCloneableObject;
import ru.utils.Utils;
import ru.utils.enums.EnumEducationLevel;
import ru.utils.objects.Education;

public class GuiEducationObject extends GuiCloneableObject<GuiEducationObject> {

    private final MenuButton mbLevel, mbYear;
    private final TextField tfInstitute, tfProfession;
    private final TextArea taDescription;

    public GuiEducationObject(double x, double y, double width, double height) {
        super(x, y, width, height, true);

        root.getChildren().addAll(
                GuiConstructor.createLabel("Уровень:", 5, 10, 75, 15, Pos.CENTER),
                mbLevel = GuiConstructor.createMenuButton(e -> {}, EnumEducationLevel.NOT_STATED.getDisplayName(), EnumEducationLevel.getEnumDisplayNames(), 80, 5, 145, 25),
                GuiConstructor.createLabel("Заведение:", 5, 40, 75, 15, Pos.CENTER),
                tfInstitute = GuiConstructor.createTextField("", 80, 35, 360, 25),
                GuiConstructor.createLabel("Профессия:", 5, 70, 75, 15, Pos.CENTER),
                tfProfession = GuiConstructor.createTextField("", 80, 65, 360, 25),
                GuiConstructor.createLabel("Год:", 225, 10, 35, 15, Pos.CENTER),
                mbYear = GuiConstructor.createMenuButton(e -> {},1900, 2022, 260, 5, 60, 25),
                taDescription = GuiConstructor.createTextArea("", true,5, 95, 435, 80)
        );
    }

    @Override
    public GuiEducationObject getClone() {
        return new GuiEducationObject(x, y, width, height);
    }

    @Override
    public String save() {
        return new Education(
                EnumEducationLevel.getEnumByName(mbLevel.getText()),
                tfInstitute.getText(),
                tfProfession.getText(),
                Integer.parseInt("0" + mbYear.getText()),
                Utils.getFixedDescription(taDescription.getText())
        ).save();
    }

    @Override
    public void load(String data) {
        Education education = new Education(data);
        mbLevel.setText(education.getLevel().getDisplayName());
        tfInstitute.setText(education.getInstitute());
        tfProfession.setText(education.getProfession());
        mbYear.setText(String.valueOf(education.getYear()));
        taDescription.setText("");
        for (String line: Utils.getNotFixedDescription(education.getDescription())) {
            taDescription.appendText(line);
        }
    }

    @Override
    public void clear() {
        mbLevel.setText(EnumEducationLevel.NOT_STATED.getDisplayName());
        tfInstitute.setText("");
        tfProfession.setText("");
        mbYear.setText("");
        taDescription.setText("");
    }
}
