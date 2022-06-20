package ru.dto.company;

import lombok.Getter;

@Getter
public class CompanyCreateDTO {

    private final String displayName, companyType, website, address, contactInfo, description, professions;

    public CompanyCreateDTO(String displayName, String companyType, String website, String address, String contactInfo, String description, String professions) {
        this.displayName = displayName;
        this.companyType = companyType;
        this.website = website;
        this.address = address;
        this.contactInfo = contactInfo;
        this.description = description;
        this.professions = professions;
    }
}
