package ru.utils.enums;

public enum EnumDataPaths {
    USERS_AVATARS_DIR("data\\avatars\\"),

    USERS_TABLE("data\\database\\users_table.csv"),
    COMPANIES_TABLE("data\\database\\companies_table.csv"),
    RESUMES_TABLE("data\\database\\resumes_table.csv"),
    VACANCIES_TABLE("data\\database\\vacancies_table.csv"),
    SKILLS_TABLE("data\\database\\skills_table.csv"),
    PROFESSIONS_TABLE("data\\database\\professions_table.csv"),
    LANGUAGES_TABLE("data\\database\\languages_table.csv"),
    CITIZEN_SHIPS_TABLE("data\\database\\citizen_ships_table.csv"),
    COMPANY_TYPES_TABLE("data\\database\\company_types_table.csv");

    final String path;

    EnumDataPaths(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
