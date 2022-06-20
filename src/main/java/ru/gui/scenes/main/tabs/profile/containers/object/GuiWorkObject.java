package ru.gui.scenes.main.tabs.profile.containers.object;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import ru.gui.GuiConstructor;
import ru.gui.elements.GuiCloneableObject;
import ru.gui.elements.GuiDateObject;
import ru.utils.Utils;
import ru.utils.objects.Work;

import java.time.Period;

public class GuiWorkObject extends GuiCloneableObject<GuiWorkObject> {

    private final TextField tfCompany;
    private final Label labelWorkPeriod;
    private final GuiDateObject startDate, endDate;
    private final TextField tfPosition;
    private final TextArea taDescription;

    public GuiWorkObject(double x, double y, double width, double height) {
        super(x, y, width, height, true);

        startDate = new GuiDateObject(e -> updateWorkPeriod(),5, 50);
        endDate = new GuiDateObject(e -> updateWorkPeriod(),260, 50);
        root.getChildren().addAll(
                GuiConstructor.createLabel("Компания:", 5, 10, 75, 15, Pos.CENTER),
                tfCompany = GuiConstructor.createTextField("", 80, 5, 240, 25),
                labelWorkPeriod = GuiConstructor.createLabel("Нет опыта работы", 125, 33, 195, 15, Pos.CENTER),
                GuiConstructor.createLabel("Начало:", 5, 33, 180, 15, Pos.CENTER),
                startDate.getRoot(),
                GuiConstructor.createLabel("Конец:", 260, 33, 180, 15, Pos.CENTER),
                endDate.getRoot(),
                GuiConstructor.createLabel("Должность:", 5, 85, 75, 15, Pos.CENTER),
                tfPosition = GuiConstructor.createTextField("", 80, 80, 360, 25),
                taDescription = GuiConstructor.createTextArea("", true, 5, 110, 435, 80)
        );
    }

    public void updateWorkPeriod() {
        try {
            Period period = Period.between(startDate.getLocalDate(), endDate.getLocalDate());
            if (period.getYears() < 0 || period.getMonths() < 0 || period.getDays() < 0)
                labelWorkPeriod.setText("Конечная дата меньше начальной");
            else if (period.getYears() == 0 && period.getMonths() == 0 && period.getDays() == 0)
                labelWorkPeriod.setText("Нет опыта работы");
            else labelWorkPeriod.setText(GuiDateObject.formatDisplayDate(period, false));
        } catch (Exception e) {
            labelWorkPeriod.setText("Нет опыта работы");
        }
    }

    @Override
    public GuiWorkObject getClone() {
        return new GuiWorkObject(x, y, width, height);
    }

    @Override
    public String save() {
        return new Work(startDate.getLocalDate(), endDate.getLocalDate(), tfPosition.getText(), tfCompany.getText(), taDescription.getText()).save();
    }

    @Override
    public void load(String data) {
        Work work = new Work(data);
        startDate.setLocalDate(work.getStartDate());
        endDate.setLocalDate(work.getEndDate());
        tfPosition.setText(work.getPosition());
        tfCompany.setText(work.getCompany());
        taDescription.setText("");
        for (String line: Utils.getNotFixedDescription(work.getDescription())) {
            taDescription.appendText(line);
        }
        updateWorkPeriod();
    }

    @Override
    public void clear() {
        labelWorkPeriod.setText("Нет опыта работы");
        startDate.clear();
        endDate.clear();
        tfPosition.setText("");
        tfCompany.setText("");
        taDescription.setText("");
    }
}
