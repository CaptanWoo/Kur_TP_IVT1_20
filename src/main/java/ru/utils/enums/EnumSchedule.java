package ru.utils.enums;

import java.util.ArrayList;
import java.util.List;

public enum EnumSchedule {
    NOT_STATED("NOT_STATED", "Не указано"),
    FULL_DAY("FULL_DAY", "Полный день"),
    SHIFT("SHIFT", "Сменный график"),
    FLEXIBLE("FLEXIBLE", "Гибкий график"),
    DISTANT("DISTANT", "Удалённая работа"),
    WATCH("WATCH", "Вахтовый метод");

    final String unlocalizedName;
    final String displayName;

    EnumSchedule(String unlocalizedName, String displayName) {
        this.unlocalizedName = unlocalizedName;
        this.displayName = displayName;
    }

    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static EnumSchedule getEnum(String unlocalizedName) {
        for (EnumSchedule enumSchedule: values()) if (enumSchedule.unlocalizedName.equals(unlocalizedName)) return enumSchedule;
        return NOT_STATED;
    }

    public static EnumSchedule getEnumByName(String displayName) {
        for (EnumSchedule enumSchedule: values()) if (enumSchedule.displayName.equals(displayName)) return enumSchedule;
        return NOT_STATED;
    }

    public static String getUnlocalizedNameByDisplayName(String displayName) {
        for (EnumSchedule enumSchedule: values()) if (enumSchedule.displayName.equals(displayName)) return enumSchedule.getUnlocalizedName();
        return NOT_STATED.getUnlocalizedName();
    }

    public static List<String> getEnumDisplayNames() {
        List<String> list = new ArrayList<>();
        for (EnumSchedule enumSchedule: values()) list.add(enumSchedule.displayName);
        return list;
    }
}
