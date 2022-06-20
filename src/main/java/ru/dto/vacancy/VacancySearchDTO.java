package ru.dto.vacancy;

import lombok.Getter;

@Getter
public class VacancySearchDTO {

    private final int minSalary, maxSalary, minWorkPeriod, maxWorkPeriod;
    private final String employmentTypes, schedules;
    private final String professions, skills, languages, citizenShips;
    private final boolean professionsAll, skillsAll, languagesAll, citizenShipsAll;

    public VacancySearchDTO(int minSalary, int maxSalary, int minWorkPeriod, int maxWorkPeriod, String employmentTypes, String schedules, String professions, String skills, String languages, String citizenShips, boolean professionsAll, boolean skillsAll, boolean languagesAll, boolean citizenShipsAll) {
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.minWorkPeriod = minWorkPeriod;
        this.maxWorkPeriod = maxWorkPeriod;
        this.employmentTypes = employmentTypes;
        this.schedules = schedules;
        this.professions = professions;
        this.skills = skills;
        this.languages = languages;
        this.citizenShips = citizenShips;
        this.professionsAll = professionsAll;
        this.skillsAll = skillsAll;
        this.languagesAll = languagesAll;
        this.citizenShipsAll = citizenShipsAll;
    }
}
