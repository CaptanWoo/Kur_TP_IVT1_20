package ru.dto.resume;

import lombok.Getter;

@Getter
public class ResumeInfoDTO {

    private final String displayName, salary, employmentType, schedule, workPeriod;

    private final String fio, age, sex, email, phoneNumber;
    private final String address;
    private final String description;
    private final String professions, skills, languages, citizenShips;
    private final String educations, works, recommendations;

    public ResumeInfoDTO(String displayName, String salary, String employmentType, String schedule, String workPeriod, String fio, String age, String sex, String email, String phoneNumber, String address, String description, String professions, String skills, String languages, String citizenShips, String educations, String works, String recommendations) {
        this.displayName = displayName;
        this.salary = salary;
        this.employmentType = employmentType;
        this.schedule = schedule;
        this.workPeriod = workPeriod;
        this.fio = fio;
        this.age = age;
        this.sex = sex;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.description = description;
        this.professions = professions;
        this.skills = skills;
        this.languages = languages;
        this.citizenShips = citizenShips;
        this.educations = educations;
        this.works = works;
        this.recommendations = recommendations;
    }
}
