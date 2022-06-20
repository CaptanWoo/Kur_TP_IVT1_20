package ru.dto.vacancy;

import lombok.Getter;

@Getter
public class VacancyCreateDTO {

    private final String displayName, schedule, employmentType;
    private final long companyId;
    private final int minSalary, maxSalary, minWorkPeriod, maxWorkPeriod;
    private final String address, contactInfo, description, shortDescription, professions, skills, languages, citizenShips;

    public VacancyCreateDTO(String displayName, String schedule, String employmentType, long companyId, int minSalary, int maxSalary, int minWorkPeriod, int maxWorkPeriod, String address, String contactInfo, String description, String shortDescription, String professions, String skills, String languages, String citizenShips) {
        this.displayName = displayName;
        this.schedule = schedule;
        this.employmentType = employmentType;
        this.companyId = companyId;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.minWorkPeriod = minWorkPeriod;
        this.maxWorkPeriod = maxWorkPeriod;
        this.address = address;
        this.contactInfo = contactInfo;
        this.description = description;
        this.shortDescription = shortDescription;
        this.professions = professions;
        this.skills = skills;
        this.languages = languages;
        this.citizenShips = citizenShips;
    }
}
