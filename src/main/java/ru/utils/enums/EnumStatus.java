package ru.utils.enums;

import java.util.ArrayList;
import java.util.List;

public enum EnumStatus {
    NOT_STATED("NOT_STATED", "Не указано"),
    ACTIVE("ACTIVE", "Активно"),
    PASSIVE("PASSIVE", "Пассивно"),
    DELETED("DELETED", "Удалено"),
    BANNED("BANNED", "Заблокировано");

    final String unlocalizedName;
    final String displayName;

    EnumStatus(String unlocalizedName, String displayName) {
        this.unlocalizedName = unlocalizedName;
        this.displayName = displayName;
    }

    public String getUnlocalizedName() {
        return unlocalizedName;
    }
    public String getDisplayName() {
        return displayName;
    }

    public static EnumStatus getEnum(String unlocalizedName) {
        for (EnumStatus enumStatus: values()) if (enumStatus.unlocalizedName.equals(unlocalizedName)) return enumStatus;
        return NOT_STATED;
    }
    public static EnumStatus getEnumByDisplayName(String displayName) {
        for (EnumStatus enumStatus: values()) if (enumStatus.displayName.equals(displayName)) return enumStatus;
        return NOT_STATED;
    }

    public static List<String> getDisplayNamesAsList() {
        List<String> list = new ArrayList<>();
        for (EnumStatus enumStatus: values()) list.add(enumStatus.displayName);
        return list;
    }
}
