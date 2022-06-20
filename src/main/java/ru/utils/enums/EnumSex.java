package ru.utils.enums;

public enum EnumSex {
    NOT_STATED("NOT_STATED", "Не указано"),
    MALE("MALE", "Мужской"),
    FEMALE("FEMALE", "Женский");

    final String unlocalizedName;
    final String displayName;

    EnumSex(String unlocalizedName, String displayName) {
        this.unlocalizedName = unlocalizedName;
        this.displayName = displayName;
    }

    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static EnumSex getEnum(String unlocalizedName) {
        for (EnumSex enumSex : values())
            if (enumSex.unlocalizedName.equals(unlocalizedName)) return enumSex;
        return NOT_STATED;
    }
}