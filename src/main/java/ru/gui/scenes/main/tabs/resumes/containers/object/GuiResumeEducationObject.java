package ru.gui.scenes.main.tabs.resumes.containers.object;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import ru.gui.GuiConstructor;
import ru.gui.elements.GuiCloneableObject;
import ru.utils.Utils;
import ru.utils.enums.EnumEducationLevel;
import ru.utils.objects.Education;

public class GuiResumeEducationObject extends GuiCloneableObject<GuiResumeEducationObject> {

    private final Education education;
    private final Label labelLevel, labelYear, labelInstitute, labelProfession;
    private final TextArea taDescription;

    public GuiResumeEducationObject(double x, double y) {
        super(x, y, 445, 155, true);
        education = new Education();

        root.getChildren().addAll(
                labelLevel = GuiConstructor.createLabel("Уровень: " + EnumEducationLevel.NOT_STATED.getDisplayName(), 10, 10, 240, 15),
                labelYear = GuiConstructor.createLabel("Год:", 260, 10, 90, 15),
                labelInstitute = GuiConstructor.createLabel("Заведение:", 10, 30, 430, 15),
                labelProfession = GuiConstructor.createLabel("Специализации:", 10, 50, 430, 15),
                taDescription = GuiConstructor.createTextArea("", true, false,5, 70, 435, 80)
        );
    }

    public GuiResumeEducationObject(double x, double y, String data) {
        this(x, y);
        load(data);
    }

    @Override
    public GuiResumeEducationObject getClone() {
        return new GuiResumeEducationObject(x, y);
    }

    @Override
    public String save() {
        return education.save();
    }

    @Override
    public void load(String data) {
        education.load(data);
        labelLevel.setText("Уровень: " + education.getLevel().getDisplayName());
        labelYear.setText("Год: " + education.getYear());
        labelInstitute.setText("Заведение: " + education.getInstitute());
        labelProfession.setText("Специализации: " + education.getProfession());
        taDescription.setText("");
        for (String line: Utils.getNotFixedDescription(education.getDescription())) {
            taDescription.appendText(line);
        }
    }

    @Override
    public void clear() {
        education.clear();
        labelLevel.setText("Уровень: " + EnumEducationLevel.NOT_STATED.getDisplayName());
        labelYear.setText("Год: ");
        labelInstitute.setText("Заведение: ");
        labelProfession.setText("Специализации: ");
        taDescription.setText("");
    }
}
