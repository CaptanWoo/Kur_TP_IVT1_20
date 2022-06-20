package ru.dto.user;

import lombok.Getter;

@Getter
public class UserCreateDTO {

    private final String email, phoneNumber, password, lastName, firstName, middleName, birthDate, sex;

    public UserCreateDTO(String email, String phoneNumber, String password,
                         String lastName, String firstName, String middleName,
                         String birthDate, String sex) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.sex = sex;
    }
}
