package ru.dto.company;

import lombok.Data;

@Data
public class CompanyPreviewDTO {

    private final long companyId;
    private final String displayName, type, professions;
    private final int vacancies;
    private final String website, address, updateDate, creationDate;

    public CompanyPreviewDTO(long companyId, String displayName, String type, String professions, int vacancies, String website, String address, String updateDate, String creationDate) {
        this.companyId = companyId;
        this.displayName = displayName;
        this.type = type;
        this.professions = professions;
        this.vacancies = vacancies;
        this.website = website;
        this.address = address;
        this.updateDate = updateDate;
        this.creationDate = creationDate;
    }
}
