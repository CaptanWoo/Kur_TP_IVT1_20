package ru.dto.user;

import lombok.Getter;

@Getter
public class UserCompaniesDTO {

    private final String companiesId, companiesDisplayNames;

    public UserCompaniesDTO(String companiesId, String companiesDisplayNames) {
        this.companiesId = companiesId;
        this.companiesDisplayNames = companiesDisplayNames;
    }
}
