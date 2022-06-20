package ru.dto.vacancy;

import lombok.Getter;

@Getter
public class VacancyInfoDTO {

    private final String displayName, company, salary, workPeriod, schedule, employmentType,
                contactFio, contactEmail, contactPhoneNumber, contactDescription, address;
    private final String description;
    private final String professions, skills, citizenShips, languages;

    public VacancyInfoDTO(String displayName, String company, String salary, String workPeriod, String schedule, String employmentType, String contactFio, String contactEmail, String contactPhoneNumber, String contactDescription, String address, String description, String professions, String skills, String citizenShips, String languages) {
        this.displayName = displayName;
        this.company = company;
        this.salary = salary;
        this.workPeriod = workPeriod;
        this.schedule = schedule;
        this.employmentType = employmentType;
        this.contactFio = contactFio;
        this.contactEmail = contactEmail;
        this.contactPhoneNumber = contactPhoneNumber;
        this.contactDescription = contactDescription;
        this.address = address;
        this.description = description;
        this.professions = professions;
        this.skills = skills;
        this.citizenShips = citizenShips;
        this.languages = languages;
    }
}
