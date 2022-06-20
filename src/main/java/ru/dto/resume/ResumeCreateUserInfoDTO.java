package ru.dto.resume;

import lombok.Getter;

@Getter
public class ResumeCreateUserInfoDTO {

    private final String professions, skills, educations, works, recommendations;

    public ResumeCreateUserInfoDTO(String professions, String skills, String educations, String works, String recommendations) {
        this.professions = professions;
        this.skills = skills;
        this.educations = educations;
        this.works = works;
        this.recommendations = recommendations;
    }
}
