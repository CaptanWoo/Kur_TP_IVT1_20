package ru.utils.enums;

import java.util.ArrayList;
import java.util.List;

public enum EnumEmploymentType {
    NOT_STATED("NOT_STATED", "Не указано"),
    FULL_TIME("FULL_TIME", "Полная занятость"),
    PART_TIME("PART_TIME", "Частичная занятость"),
    TEMPORARY("TEMPORARY", "Проектная/Временная"),
    VOLUNTEER("VOLUNTEER", "Волонтёрство"),
    INTERNSHIP("INTERNSHIP", "Стажировка");

    final String unlocalizedName;
    final String displayName;

    EnumEmploymentType(String unlocalizedName, String displayName) {
        this.unlocalizedName = unlocalizedName;
        this.displayName = displayName;
    }

    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static EnumEmploymentType getEnum(String unlocalizedName) {
        for (EnumEmploymentType enumEmploymentType: values()) if (enumEmploymentType.unlocalizedName.equals(unlocalizedName)) return enumEmploymentType;
        return NOT_STATED;
    }

    public static EnumEmploymentType getEnumByName(String displayName) {
        for (EnumEmploymentType enumEmploymentType: values()) if (enumEmploymentType.displayName.equals(displayName)) return enumEmploymentType;
        return NOT_STATED;
    }

    public static String getUnlocalizedNameByDisplayName(String displayName) {
        for (EnumEmploymentType enumEmploymentType: values()) if (enumEmploymentType.displayName.equals(displayName)) return enumEmploymentType.getUnlocalizedName();
        return NOT_STATED.getUnlocalizedName();
    }

    public static List<String> getEnumDisplayNames() {
        List<String> list = new ArrayList<>();
        for (EnumEmploymentType enumEmploymentType: values()) list.add(enumEmploymentType.displayName);
        return list;
    }
}
