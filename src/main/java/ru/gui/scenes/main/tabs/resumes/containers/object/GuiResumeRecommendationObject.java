package ru.gui.scenes.main.tabs.resumes.containers.object;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import ru.gui.GuiConstructor;
import ru.gui.elements.GuiCloneableObject;
import ru.utils.Utils;
import ru.utils.objects.Recommendation;

public class GuiResumeRecommendationObject extends GuiCloneableObject<GuiResumeRecommendationObject> {

    private final Recommendation recommendation;
    private final Label labelFio, labelPhoneNumber, labelYear, labelCompany, labelPosition;
    private final TextArea taDescription;

    public GuiResumeRecommendationObject(double x, double y) {
        super(x, y, 445, 175, true);
        recommendation = new Recommendation();

        root.getChildren().addAll(
                labelFio = GuiConstructor.createLabel("ФИО:", 10, 10, 310, 15),
                labelPhoneNumber = GuiConstructor.createLabel("Телефон:", 10, 30, 200, 15),
                labelYear = GuiConstructor.createLabel("Год:", 260, 30, 90, 15),
                labelCompany = GuiConstructor.createLabel("Компания:", 10, 50, 430, 15),
                labelPosition = GuiConstructor.createLabel("Должность:", 10, 70, 430, 15),
                taDescription = GuiConstructor.createTextArea("", true, 5, 90, 435, 80)
        );
    }

    public GuiResumeRecommendationObject(double x, double y, String data) {
        this(x, y);
        load(data);
    }

    @Override
    public GuiResumeRecommendationObject getClone() {
        return new GuiResumeRecommendationObject(x, y);
    }

    @Override
    public String save() {
        return recommendation.save();
    }

    @Override
    public void load(String data) {
        recommendation.load(data);
        labelFio.setText("ФИО: " + recommendation.getFio());
        labelPhoneNumber.setText("Телефон: " + recommendation.getPhoneNumber());
        labelYear.setText("Год: " + recommendation.getYear());
        labelCompany.setText("Компания: " + recommendation.getCompany());
        labelPosition.setText("Должность: " + recommendation.getPosition());
        taDescription.setText("");
        for (String line: Utils.getNotFixedDescription(recommendation.getDescription())) {
            taDescription.appendText(line);
        }
    }

    @Override
    public void clear() {
        recommendation.clear();
        labelFio.setText("ФИО: ");
        labelPhoneNumber.setText("Телефон: ");
        labelYear.setText("Год: ");
        labelCompany.setText("Компания: ");
        labelPosition.setText("Должность: ");
        taDescription.setText("");
    }
}

