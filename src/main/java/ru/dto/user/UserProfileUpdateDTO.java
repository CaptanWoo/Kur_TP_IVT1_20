package ru.dto.user;

import lombok.Getter;

@Getter
public class UserProfileUpdateDTO {

    private final String email, phoneNumber, lastName, firstName, middleName, birthDate, sex, address;
    private final String educations, works, recommendations;
    private final String professions, skills, languages, citizenShips;

    public UserProfileUpdateDTO(String email, String phoneNumber, String lastName, String firstName, String middleName, String birthDate, String sex, String address, String educations, String works, String recommendations, String professions, String skills, String languages, String citizenShips) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.sex = sex;
        this.address = address;
        this.educations = educations;
        this.works = works;
        this.recommendations = recommendations;
        this.professions = professions;
        this.skills = skills;
        this.languages = languages;
        this.citizenShips = citizenShips;
    }
}
