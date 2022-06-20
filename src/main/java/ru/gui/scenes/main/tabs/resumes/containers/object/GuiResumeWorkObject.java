package ru.gui.scenes.main.tabs.resumes.containers.object;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import ru.gui.GuiConstructor;
import ru.gui.elements.GuiCloneableObject;
import ru.gui.elements.GuiDateObject;
import ru.utils.Utils;
import ru.utils.objects.Work;

public class GuiResumeWorkObject extends GuiCloneableObject<GuiResumeWorkObject> {

    private final Work work;
    private final Label labelCompany, labelStartDate, labelEndDate, labelPeriod, labelPosition;
    private final TextArea taDescription;

    public GuiResumeWorkObject(double x, double y) {
        super(x, y, 445, 175, true);
        work = new Work();

        root.getChildren().addAll(
                labelCompany = GuiConstructor.createLabel("Компания", 10, 10, 335, 15),
                labelStartDate = GuiConstructor.createLabel("Начало:", 10, 30, 140, 15),
                labelEndDate = GuiConstructor.createLabel("Конец:", 150, 30, 140, 15),
                labelPeriod = GuiConstructor.createLabel("Опыт: ", 10, 50, 430, 15),
                labelPosition = GuiConstructor.createLabel("Должность:", 10, 70, 430, 15),
                taDescription = GuiConstructor.createTextArea("", true, 5, 90, 435, 80)
        );
    }

    public GuiResumeWorkObject(double x, double y, String data) {
        this(x, y);
        load(data);
    }

    @Override
    public GuiResumeWorkObject getClone() {
        return new GuiResumeWorkObject(x, y);
    }

    @Override
    public String save() {
        return work.save();
    }

    @Override
    public void load(String data) {
        work.load(data);
        labelCompany.setText("Компания: " + work.getCompany());
        labelStartDate.setText("Начало: " + work.getStartDate().toString());
        labelEndDate.setText("Конец: " + work.getEndDate().toString());
        labelPeriod.setText("Опыт: " + GuiDateObject.formatDisplayDate(work.getPeriod(), false));
        labelPosition.setText("Должность: " + work.getPosition());
        taDescription.setText("");
        for (String line: Utils.getNotFixedDescription(work.getDescription())) {
            taDescription.appendText(line);
        }
    }

    @Override
    public void clear() {
        work.clear();
        labelCompany.setText("Компания: ");
        labelStartDate.setText("Начало: ");
        labelEndDate.setText("Конец: ");
        labelPeriod.setText("Опыт: ");
        labelPosition.setText("Должность: ");
        taDescription.setText("");
    }
}
