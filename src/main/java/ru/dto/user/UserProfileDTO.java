package ru.dto.user;

import lombok.Getter;

@Getter
public class UserProfileDTO {

    private final String status, email, phoneNumber, lastName, firstName, middleName, birthDate, creationDate, sex, address;
    private final String educations, works, recommendations;
    private final String citizenShipsId, languagesId, skillsId, resumesId, companiesId, vacanciesId;

    public UserProfileDTO(String status, String email, String phoneNumber, String lastName, String firstName, String middleName, String birthDate, String creationDate, String sex, String address, String educations, String works, String recommendations, String citizenShipsId, String languagesId, String skillsId, String resumesId, String companiesId, String vacanciesId) {
        this.status = status;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.creationDate = creationDate;
        this.sex = sex;
        this.address = address;
        this.educations = educations;
        this.works = works;
        this.recommendations = recommendations;
        this.citizenShipsId = citizenShipsId;
        this.languagesId = languagesId;
        this.skillsId = skillsId;
        this.resumesId = resumesId;
        this.companiesId = companiesId;
        this.vacanciesId = vacanciesId;
    }
}
