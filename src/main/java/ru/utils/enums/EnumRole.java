package ru.utils.enums;

public enum EnumRole {
    USER(0, "USER"),
    MODERATOR(1, "MODERATOR"),
    ADMIN(2, "ADMIN"),
    CREATOR(3, "CREATOR");

    private final int level;
    private final String unlocalizedName;
    EnumRole(int level, String unlocalizedName) {
        this.level = level;
        this.unlocalizedName = unlocalizedName;
    }

    public int getLevel() {
        return level;
    }

    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public boolean hasAccess(EnumRole minRole) {
        return this.level >= minRole.level;
    }

    public static EnumRole getEnumByUnlocalizedName(String unlocalizedName) {
        for (EnumRole enumRole: values()) if (enumRole.unlocalizedName.equals(unlocalizedName)) return enumRole;
        return null;
    }
}
