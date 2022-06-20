package ru.dto.objects;

import lombok.Getter;

@Getter
public class ContactInfoDTO {

    private final String fio, email, phoneNumber, description;

    public ContactInfoDTO(String fio, String email, String phoneNumber, String description) {
        this.fio = fio;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.description = description;
    }
}
