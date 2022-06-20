package ru.dto.vacancy;

import lombok.Getter;

@Getter
public class VacancyPreviewDTO {

    private final long id;
    private final String displayName;
    private final String salary;
    private final String
            workPeriod, schedule, employmentType, professions, company,
            region, citizenShips, languages, updateDate, creationDate, shortDescription;

    public VacancyPreviewDTO(long id, String displayName, String salary, String workPeriod, String schedule, String employmentType, String professions, String company, String region, String citizenShips, String languages, String updateDate, String creationDate, String shortDescription) {
        this.id = id;
        this.displayName = displayName;
        this.salary = salary;
        this.workPeriod = workPeriod;
        this.schedule = schedule;
        this.employmentType = employmentType;
        this.professions = professions;
        this.company = company;
        this.region = region;
        this.citizenShips = citizenShips;
        this.languages = languages;
        this.updateDate = updateDate;
        this.creationDate = creationDate;
        this.shortDescription = shortDescription;
    }
}
