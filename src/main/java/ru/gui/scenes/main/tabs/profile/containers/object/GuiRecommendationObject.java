package ru.gui.scenes.main.tabs.profile.containers.object;

import javafx.geometry.Pos;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import ru.gui.GuiConstructor;
import ru.gui.elements.GuiCloneableObject;
import ru.utils.Utils;
import ru.utils.objects.Recommendation;

public class GuiRecommendationObject extends GuiCloneableObject<GuiRecommendationObject> {

    private final TextField tfFio, tfPosition, tfCompany, tfPhoneNumber;
    private final MenuButton mbYear;
    private final TextArea taDescription;

    public GuiRecommendationObject(double x, double y, double width, double height) {
        super(x, y, width, height, true);

        root.getChildren().addAll(
                GuiConstructor.createLabel("ФИО:", 5, 10, 75, 15, Pos.CENTER),
                tfFio = GuiConstructor.createTextField("", 80, 5, 240, 25),
                GuiConstructor.createLabel("Телефон:", 5, 40, 75, 15, Pos.CENTER),
                tfPhoneNumber = GuiConstructor.createTextField("", 80, 35, 240, 25),
                GuiConstructor.createLabel("Год:", 320, 40, 35, 15, Pos.CENTER),
                mbYear = GuiConstructor.createMenuButton(e -> {}, 1900, 2022, 355 ,35, 85, 25),
                GuiConstructor.createLabel("Компания:", 5, 70, 75, 15, Pos.CENTER),
                tfCompany = GuiConstructor.createTextField("", 80, 65, 360, 25),
                GuiConstructor.createLabel("Должность:", 5, 100, 75, 15, Pos.CENTER),
                tfPosition = GuiConstructor.createTextField("", 80, 95, 360, 25),
                taDescription = GuiConstructor.createTextArea("", true, 5, 125, 435, 80)
        );
    }

    @Override
    public GuiRecommendationObject getClone() {
        return new GuiRecommendationObject(x, y, width, height);
    }

    @Override
    public String save() {
        return new Recommendation(
                tfFio.getText(),
                tfPhoneNumber.getText(),
                Integer.parseInt("0" + mbYear.getText()),
                tfCompany.getText(),
                tfPosition.getText(),
                Utils.getFixedDescription(taDescription.getText())
        ).save();
    }

    @Override
    public void load(String data) {
        Recommendation recommendation = new Recommendation(data);
        tfFio.setText(recommendation.getFio());
        tfPhoneNumber.setText(recommendation.getPhoneNumber());
        mbYear.setText(String.valueOf(recommendation.getYear()));
        tfCompany.setText(recommendation.getCompany());
        tfPosition.setText(recommendation.getPosition());
        taDescription.setText("");
        for (String line: Utils.getNotFixedDescription(recommendation.getDescription())) {
            taDescription.appendText(line);
        }
    }

    @Override
    public void clear() {
        tfFio.setText("");
        tfPhoneNumber.setText("+");
        mbYear.setText("0");
        tfCompany.setText("");
        tfPosition.setText("");
        taDescription.setText("");
    }
}
