package ru.dto.company;

import lombok.Getter;

@Getter
public class CompanyInfoDTO {

    private final long id;
    private final int vacanciesCount;
    private final String displayName, companyType, website, address, contactInfo, description, professions;

    public CompanyInfoDTO(long id, int vacanciesCount, String displayName, String companyType, String website, String address, String contactInfo, String description, String professions) {
        this.id = id;
        this.vacanciesCount = vacanciesCount;
        this.displayName = displayName;
        this.companyType = companyType;
        this.website = website;
        this.address = address;
        this.contactInfo = contactInfo;
        this.description = description;
        this.professions = professions;
    }
}
