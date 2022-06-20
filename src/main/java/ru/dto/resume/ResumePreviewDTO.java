package ru.dto.resume;

import lombok.Getter;

@Getter
public class ResumePreviewDTO {

    private final long resumeId;
    private final String
            displayName, salary, age, workPeriod, schedule, employmentType, professions,
            lastWork, mainEducation, citizenShips, region, languages, creationDate, updateDate;

    public ResumePreviewDTO(long resumeId, String displayName, String salary, String age, String workPeriod, String schedule,
                            String employmentType, String professions, String lastWork, String mainEducation,
                            String citizenShips, String region, String languages, String creationDate, String updateDate) {
        this.resumeId = resumeId;
        this.displayName = displayName;
        this.salary = salary;
        this.age = age;
        this.workPeriod = workPeriod;
        this.schedule = schedule;
        this.employmentType = employmentType;
        this.professions = professions;
        this.lastWork = lastWork;
        this.mainEducation = mainEducation;
        this.citizenShips = citizenShips;
        this.region = region;
        this.languages = languages;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
    }
}
