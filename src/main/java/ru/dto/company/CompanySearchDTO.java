package ru.dto.company;

import lombok.Getter;

@Getter
public class CompanySearchDTO {

    private final String name, address, companyTypes, professions;
    private boolean professionsAll;

    public CompanySearchDTO(String name, String address, String companyTypes, String professions, boolean professionsAll) {
        this.name = name;
        this.address = address;
        this.companyTypes = companyTypes;
        this.professions = professions;
        this.professionsAll = professionsAll;
    }
}
