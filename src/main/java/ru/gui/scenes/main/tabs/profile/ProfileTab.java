package ru.gui.scenes.main.tabs.profile;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import ru.gui.GuiConstructor;
import ru.gui.scenes.main.tabs.TabItem;
import ru.gui.scenes.main.tabs.profile.elements.GuiProfileInfo;

public class ProfileTab extends TabItem {

    private final ScrollPane scrollPane;
    private final AnchorPane scrollRoot;
    private final GuiProfileInfo guiProfileInfo;

    public ProfileTab() {
        super("Профиль");

        guiProfileInfo = new GuiProfileInfo(0, 0);

        root.getChildren().addAll(
                scrollPane = GuiConstructor.createScrollPane(scrollRoot = new AnchorPane(), 143, 47, 900, 704),
                GuiConstructor.createButton(e -> guiProfileInfo.save(), "Сохранить изменения", 435, 780, 150, 25),
                GuiConstructor.createButton(e -> {
                    try {
                        guiProfileInfo.load();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }, "Отменить изменения", 615, 780, 150, 25)
        );

        try {
            guiProfileInfo.load();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        scrollRoot.getChildren().add(guiProfileInfo.getRoot());

    }

}
