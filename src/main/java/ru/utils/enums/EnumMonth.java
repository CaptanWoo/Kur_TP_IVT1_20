package ru.utils.enums;

public enum EnumMonth {

    JANUARY(1, 31),
    FEBRUARY(2, 28),
    MARCH(3, 31),
    APRIL(4, 30),
    MAY(5, 31),
    JUNE(6, 30),
    JULY(7, 31),
    AUGUST(8, 31),
    SEPTEMBER(9, 30),
    OCTOBER(10, 31),
    NOVEMBER(11, 30),
    DECEMBER(12, 31);

    private final int number, days;
    EnumMonth(int number, int days) {
        this.number = number;
        this.days = days;
    }

    public int getNumber() {
        return number;
    }

    public static EnumMonth getMonth(int number) {
        for (EnumMonth month: values()) {
            if (month.number == number) return month;
        }
        return JANUARY;
    }

    public int getNotLeapDays() {
        return days;
    }

    public int getDays(int year) {
        if (this == FEBRUARY) {
            if (isLeapYear(year)) return 29;
            else return FEBRUARY.days;
        } else return this.days;
    }

    public static boolean isLeapYear(int year) {
        if (year % 4 != 0) return false;
        else if (year % 400 == 0) return true;
        else return year % 100 != 0;
    }

}
