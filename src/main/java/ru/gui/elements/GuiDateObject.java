package ru.gui.elements;

import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import ru.gui.GuiConstructor;
import ru.utils.enums.EnumMonth;
import ru.utils.interfaces.IExecutable;

import java.time.LocalDate;
import java.time.Period;

public class GuiDateObject extends GuiObject {

    private IExecutable iExecutable;
    private final MenuButton mbDay, mbMonth, mbYear;

    public GuiDateObject(double x, double y) {
        super(x, y, 180, 25);
        root.getChildren().addAll(
                mbDay = GuiConstructor.createMenuButton(e -> updateDate(),1, 31, 0, 0, 50, 25),
                mbMonth = GuiConstructor.createMenuButton(e -> updateDate(),1, 12, 55, 0, 50, 25),
                mbYear = GuiConstructor.createMenuButton(e -> updateDate(),1900, 2022, 110, 0, 70, 25)
        );
    }

    public GuiDateObject(IExecutable iExecutable, double x, double y) {
        this(x, y);
        this.iExecutable = iExecutable;
    }

    public void updateDate() {
        int day = Integer.parseInt("0" + mbDay.getText());
        int month = Integer.parseInt("0" + mbMonth.getText());
        int year = Integer.parseInt("0" + mbYear.getText());

        if (mbMonth.getText().equals("")) return;

        if (month == EnumMonth.FEBRUARY.getNumber()) {
            if (year == 0) updateMenuButton(mbDay, e -> updateDate(), 1, 29);
            else updateMenuButton(mbDay, e -> updateDate(), 1, EnumMonth.FEBRUARY.getDays(year));
        } else {
            updateMenuButton(mbDay, e -> updateDate(), 1, EnumMonth.getMonth(month).getDays(year));
        }

        if (iExecutable != null && day != 0 && month != 0 && year != 0) iExecutable.execute("");

    }

    public void updateMenuButton(MenuButton menuButton, IExecutable iExecutable, int min, int max) {
        int value = Integer.parseInt("0" + menuButton.getText());
        if (value < min || value > max) menuButton.setText("");

        menuButton.getItems().clear();
        for (int i = min; i <= max; i++) {
            MenuItem menuItem = new MenuItem(String.valueOf(i));
            menuItem.setMnemonicParsing(false);
            menuItem.setOnAction(e -> {
                menuButton.setText(menuItem.getText());
                iExecutable.execute(menuItem.getText());
            });
            menuButton.getItems().add(menuItem);
        }
    }

    public LocalDate getLocalDate() {
        try {
            return LocalDate.of(Integer.parseInt(mbYear.getText()), Integer.parseInt(mbMonth.getText()), Integer.parseInt(mbDay.getText()));
        } catch (Exception e) {
            return LocalDate.MIN;
        }
    }

    public void setLocalDate(LocalDate localDate) {
        if (!localDate.equals(LocalDate.MIN)) {
            mbYear.setText(String.valueOf(localDate.getYear()));
            mbMonth.setText(String.valueOf(localDate.getMonth().getValue()));
            mbDay.setText(String.valueOf(localDate.getDayOfMonth()));
        } else {
            mbYear.setText("");
            mbMonth.setText("");
            mbDay.setText("");
        }
    }

    public void clear() {
        mbDay.setText("");
        mbMonth.setText("");
        mbYear.setText("");
    }

    public static String formatDisplayDate(String text, boolean dp) {
        return formatDisplayDate(Period.parse(text), dp);
    }

    public static String formatDisplayDate(Period period, boolean dp) {
        return formatDisplayDate(period.getYears(), period.getMonths(), period.getDays(), dp);
    }

    /**
     * Метод форматирования отображения даты
     * @param year год
     * @param month месяц
     * @param day день
     * @param dp нужно ли выводить в дательном падеже
     * @return форматированная строка с датой
     */
    public static String formatDisplayDate(int year, int month, int day, boolean dp) {
        String y, m, d;

        if (year == 0 && month == 0 && day == 0) return "Не указано";

        if (year <= 0) y = "";
        else if (year == 1 && !dp) y = year + " год";
        else if (year < 5 && !dp || dp && year == 1) y = year + " года";
        else y = year + " лет";

        if (month <= 0) m = "";
        else if (month == 1 && !dp) m = month + " месяц";
        else if (month < 5 && !dp || dp && month == 1) m = month + " месяца";
        else m = month + " месяцев";

        if (day <= 0) d = "";
        else if (day == 1 && !dp) d = day + " день";
        else if (day < 5 && !dp || dp && day == 1) d = day + " дня";
        else d = day + " дней";


        return y + " " + m + " " + d;
    }

}
