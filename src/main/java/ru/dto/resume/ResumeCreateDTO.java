package ru.dto.resume;

import lombok.Getter;

@Getter
public class ResumeCreateDTO {

    private final String displayName, professions, description;
    private final int salary;
    private final String educations, works, skills, recommendations, employmentType, schedule;

    public ResumeCreateDTO(String displayName, String professions, String description, int salary, String educations, String works, String skills, String recommendations, String employmentType, String schedule) {
        this.displayName = displayName;
        this.professions = professions;
        this.description = description;
        this.salary = salary;
        this.educations = educations;
        this.works = works;
        this.skills = skills;
        this.recommendations = recommendations;
        this.employmentType = employmentType;
        this.schedule = schedule;
    }
}
