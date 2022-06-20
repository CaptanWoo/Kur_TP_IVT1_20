package ru.utils.enums;

import java.util.ArrayList;
import java.util.List;

public enum EnumEducationLevel {
    NOT_STATED("NOT_STATED", "Не указано"),
    HIGHER("HIGHER", "Высшее"),
    BACHELOR("BACHELOR", "Бакалавр"),
    MASTER("MASTER", "Магистр"),
    INCOMPLETE_HIGHER("INCOMPLETE_HIGHER", "Незаконченное высшее"),
    SECONDARY("SECONDARY", "Среднее"),
    SPECIALIZED_SECONDARY("SPECIALIZED_SECONDARY", "Среднее специальное");

    final String unlocalizedName;
    final String displayName;

    EnumEducationLevel(String unlocalizedName, String displayName) {
        this.unlocalizedName = unlocalizedName;
        this.displayName = displayName;
    }

    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static EnumEducationLevel getEnum(String unlocalizedName) {
        for (EnumEducationLevel enumEducationLevel: values()) if (enumEducationLevel.unlocalizedName.equals(unlocalizedName)) return enumEducationLevel;
        return NOT_STATED;
    }

    public static EnumEducationLevel getEnumByName(String displayName) {
        for (EnumEducationLevel enumEducationLevel: values()) if (enumEducationLevel.displayName.equals(displayName)) return enumEducationLevel;
        return NOT_STATED;
    }

    public static String getUnlocalizedNameByDisplayName(String displayName) {
        for (EnumEducationLevel enumEducationLevel: values()) if (enumEducationLevel.displayName.equals(displayName)) return enumEducationLevel.getUnlocalizedName();
        return NOT_STATED.getUnlocalizedName();
    }

    public static List<String> getEnumDisplayNames() {
        List<String> list = new ArrayList<>();
        for (EnumEducationLevel enumEducationLevel: values()) list.add(enumEducationLevel.displayName);
        return list;
    }
}
