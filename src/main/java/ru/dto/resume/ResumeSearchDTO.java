package ru.dto.resume;

import lombok.Getter;

@Getter
public class ResumeSearchDTO {

    private final int minSalary, maxSalary, minWorkPeriod, maxWorkPeriod, minAge, maxAge;
    private final String sex;
    private final String educationLevels, employmentTypes, schedules;
    private final String professions, skills, languages, citizenShips;
    private final boolean educationsAll, professionsAll, skillsAll, languagesAll, citizenShipsAll;

    public ResumeSearchDTO(int minSalary, int maxSalary, int minWorkPeriod, int maxWorkPeriod, int minAge, int maxAge, String sex, String educationLevels, String employmentTypes, String schedules, String professions, String skills, String languages, String citizenShips, boolean educationsAll, boolean professionsAll, boolean skillsAll, boolean languagesAll, boolean citizenShipsAll) {
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.minWorkPeriod = minWorkPeriod;
        this.maxWorkPeriod = maxWorkPeriod;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.sex = sex;
        this.educationLevels = educationLevels;
        this.employmentTypes = employmentTypes;
        this.schedules = schedules;
        this.professions = professions;
        this.skills = skills;
        this.languages = languages;
        this.citizenShips = citizenShips;
        this.educationsAll = educationsAll;
        this.professionsAll = professionsAll;
        this.skillsAll = skillsAll;
        this.languagesAll = languagesAll;
        this.citizenShipsAll = citizenShipsAll;
    }
}
